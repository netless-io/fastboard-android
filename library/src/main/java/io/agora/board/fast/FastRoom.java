package io.agora.board.fast;

import static io.agora.board.fast.FastException.ROOM_DISCONNECT_ERROR;
import static io.agora.board.fast.FastException.ROOM_JOIN_ERROR;
import static io.agora.board.fast.FastException.ROOM_KICKED;
import static io.agora.board.fast.FastException.SDK_SETUP_ERROR;

import android.view.View;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import com.herewhite.sdk.CommonCallback;
import com.herewhite.sdk.ConverterCallbacks;
import com.herewhite.sdk.Room;
import com.herewhite.sdk.RoomListener;
import com.herewhite.sdk.WhiteSdk;
import com.herewhite.sdk.WhiteSdkConfiguration;
import com.herewhite.sdk.WhiteboardView;
import com.herewhite.sdk.converter.ConvertType;
import com.herewhite.sdk.converter.ConverterV5;
import com.herewhite.sdk.converter.ProjectorQuery;
import com.herewhite.sdk.domain.ConversionInfo;
import com.herewhite.sdk.domain.ConvertException;
import com.herewhite.sdk.domain.ConvertedFiles;
import com.herewhite.sdk.domain.ImageInformation;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.Promise;
import com.herewhite.sdk.domain.Region;
import com.herewhite.sdk.domain.RoomPhase;
import com.herewhite.sdk.domain.RoomState;
import com.herewhite.sdk.domain.WindowOriginSize;
import com.herewhite.sdk.domain.SDKError;
import com.herewhite.sdk.domain.Scene;
import com.herewhite.sdk.domain.WindowAppParam;
import com.herewhite.sdk.domain.DispatchDocsEventResult;
import com.herewhite.sdk.domain.WindowDocsEvent;
import com.herewhite.sdk.domain.WindowPageStateOptions;
import com.herewhite.sdk.domain.UnifiedPageState;
import com.herewhite.sdk.domain.SlidePageState;
import com.herewhite.sdk.domain.WindowPrefersColorScheme;
import com.herewhite.sdk.domain.ApplianceInitLoadingChangeEvent;
import com.herewhite.sdk.domain.BackgroundImageLoadEvent;
import com.herewhite.sdk.window.SlideListener;
import com.herewhite.sdk.window.UnifiedPageStateListener;
import io.agora.board.fast.internal.RoomLifecycle;
import io.agora.board.fast.internal.OnceResult;
import java.util.ArrayList;

import io.agora.board.fast.extension.ErrorHandler;
import io.agora.board.fast.extension.FastResource;
import io.agora.board.fast.extension.FastResult;
import io.agora.board.fast.extension.OverlayHandler;
import io.agora.board.fast.extension.OverlayManager;
import io.agora.board.fast.extension.RoomPhaseHandler;
import io.agora.board.fast.internal.FastConvertor;
import io.agora.board.fast.internal.FastErrorHandler;
import io.agora.board.fast.internal.FastOverlayHandler;
import io.agora.board.fast.internal.FastOverlayManager;
import io.agora.board.fast.internal.FastRoomContext;
import io.agora.board.fast.internal.FastRoomPhaseHandler;
import io.agora.board.fast.internal.PromiseResultAdapter;
import io.agora.board.fast.internal.Util;
import io.agora.board.fast.internal.WhiteboardViewManager;
import io.agora.board.fast.model.ConverterType;
import io.agora.board.fast.model.DocPage;
import io.agora.board.fast.model.FastAppliance;
import io.agora.board.fast.model.FastInsertDocParams;
import io.agora.board.fast.model.FastRedoUndo;
import io.agora.board.fast.model.FastRegisterAppParams;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.model.FastWindowBoxState;
import io.agora.board.fast.ui.ErrorHandleLayout;
import io.agora.board.fast.ui.FastRoomController;
import io.agora.board.fast.ui.FastUiSettings;
import io.agora.board.fast.ui.LoadingLayout;
import io.agora.board.fast.ui.OverlayLayout;
import io.agora.board.fast.ui.RoomControllerGroup;

import java.util.List;
import java.util.UUID;
import org.json.JSONObject;

public class FastRoom {

    private final FastboardView fastboardView;

    private final FastRoomOptions fastRoomOptions;

    private final FastRoomContext fastRoomContext;

    private WhiteSdk whiteSdk;

    private Room room;
    private final RoomLifecycle lifecycle = new RoomLifecycle();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final List<SessionResult<?>> pendingResults = new ArrayList<>();
    private final List<SdkResult<?>> pendingSdkResults = new ArrayList<>();
    private final List<FastResult<Object>> leaveResults = new ArrayList<>();
    // URL interruption remains synchronous on the SDK bridge thread.
    private volatile CommonCallback externalCommonCallback;
    private SlideListener slideListener;
    private UnifiedPageStateListener unifiedPageStateListener;

    private void onMain(Runnable action) {
        if (Looper.myLooper() == Looper.getMainLooper()) action.run();
        else mainHandler.post(action);
    }

    private static void requireMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("FastRoom must be used on the main thread");
        }
    }

    /** Exactly-once result owned by the room session that started the operation. */
    private final class SessionResult<T> implements FastResult<T> {
        final Room owner = room;
        final long generation = lifecycle.generation();
        final OnceResult<T> delivery;
        SessionResult(FastResult<T> target) {
            this.delivery = new OnceResult<>(target);
            pendingResults.add(this);
        }
        boolean current() {
            return !delivery.isCompleted() && owner != null && owner == room && isReady()
                && generation == lifecycle.generation();
        }
        void finish(T value, Exception error) {
            if (delivery.isCompleted()) return;
            if (!current()) error = cancelledError();
            delivery.finish(value, error, () -> pendingResults.remove(this));
        }
        @Override public void onSuccess(T value) { onMain(() -> finish(value, null)); }
        @Override public void onError(Exception error) { onMain(() -> finish(null, error)); }
        void cancel() { finish(null, cancelledError()); }
    }

    private static FastException cancelledError() {
        return FastException.createRoom(FastException.ROOM_OPERATION_CANCELLED, "Room operation cancelled");
    }

    private void cancelPendingResults() {
        for (SessionResult<?> result : new ArrayList<>(pendingResults)) result.cancel();
    }

    /** SDK-scoped operations may start before joining, and survive a normal room leave. */
    private final class SdkResult<T> {
        final OnceResult<T> delivery;
        SdkResult(FastResult<T> target) {
            delivery = new OnceResult<>(target);
            pendingSdkResults.add(this);
        }
        void finish(T value, Exception error) {
            requireMainThread();
            if (requiresRecreation()) error = cancelledError();
            delivery.finish(value, error, () -> pendingSdkResults.remove(this));
        }
    }

    private void cancelSdkResults() {
        for (SdkResult<?> result : new ArrayList<>(pendingSdkResults)) result.finish(null, cancelledError());
    }

    private interface CommonEvent { void dispatch(CommonCallback callback); }

    private void notifyCommon(CommonEvent event) {
        onMain(() -> {
            CommonCallback callback = externalCommonCallback;
            if (lifecycle.state() != RoomLifecycle.State.DESTROYED && callback != null) event.dispatch(callback);
        });
    }

    private boolean requireReady(@Nullable FastResult<?> result) {
        requireMainThread();
        if (isReady()) return true;
        if (result != null) result.onError(FastException.createRoom(FastException.ROOM_NOT_READY, "Room is not ready"));
        return false;
    }

    private OnRoomReadyCallback onRoomReadyCallback;

    private RoomControllerGroup roomControllerGroup;

    private final CommonCallback commonCallback = new CommonCallback() {
        @Override
        public void throwError(Object args) {
            FastLogger.error("SDK reported an error");
            notifyCommon(callback -> callback.throwError(args));
        }

        @Override
        public void onMessage(JSONObject object) {
            notifyCommon(callback -> callback.onMessage(object));
        }

        @Override
        public void sdkSetupFail(SDKError error) {
            onMain(() -> {
                if (requiresRecreation()) return;
                lifecycle.fail();
                room = null;
                cancelPendingResults();
                cancelSdkResults();
                completeLeave(null, error);
                // A cancellation callback may have destroyed this FastRoom.
                if (lifecycle.state() == RoomLifecycle.State.DESTROYED) return;
                FastLogger.error("sdk setup fail ", error);
                fastRoomContext.notifyFastError(FastException.createSdk(SDK_SETUP_ERROR, error.getMessage()));
                notifyCommon(callback -> callback.sdkSetupFail(error));
            });
        }

        @Override
        public void onLogger(JSONObject object) {
            // Raw SDK arguments can contain room tokens. Logging is opt-in at the consumer.
            notifyCommon(callback -> callback.onLogger(object));
        }
        @Override public String urlInterrupter(String url) {
            CommonCallback callback = externalCommonCallback;
            return callback == null ? url : callback.urlInterrupter(url);
        }
        @Override public void onPPTMediaPlay() {
            notifyCommon(CommonCallback::onPPTMediaPlay);
        }
        @Override public void onPPTMediaPause() {
            notifyCommon(CommonCallback::onPPTMediaPause);
        }
        @Override public void onBackgroundImageLoad(BackgroundImageLoadEvent event) {
            notifyCommon(callback -> callback.onBackgroundImageLoad(event));
        }
        @Override public void onApplianceInitLoadingChange(ApplianceInitLoadingChangeEvent event) {
            notifyCommon(callback -> callback.onApplianceInitLoadingChange(event));
        }
        @Override public void onLocalLogStateChange(JSONObject state) {
            notifyCommon(callback -> callback.onLocalLogStateChange(state));
        }
    };

    private final RoomListener roomListener = new RoomListener() {
        private long canUndoSteps;

        private long canRedoSteps;

        @Override
        public void onPhaseChanged(RoomPhase phase) {
            boolean leaving = lifecycle.state() == RoomLifecycle.State.CANCELLING || lifecycle.state() == RoomLifecycle.State.LEAVING;
            if (lifecycle.state() != RoomLifecycle.State.JOINING && !isReady()
                && !(leaving && (phase == RoomPhase.disconnecting || phase == RoomPhase.disconnected))) return;
            fastRoomContext.notifyRoomPhaseChanged(phase);
        }

        @Override
        public void onDisconnectWithError(Exception e) {
            disconnect();
            FastLogger.warn("receive disconnect error from js " + e.getMessage());
            fastRoomContext.notifyFastError(FastException.createRoom(ROOM_DISCONNECT_ERROR, e.getMessage(), e));
        }

        @Override
        public void onKickedWithReason(String reason) {
            disconnect();
            fastRoomContext.notifyFastError(FastException.createRoom(ROOM_KICKED, reason));
        }

        @Override
        public void onRoomStateChanged(RoomState modifyState) {
            if (!isReady()) return;
            fastRoomContext.notifyRoomStateChanged(modifyState);
        }

        @Override
        public void onCanUndoStepsUpdate(long canUndoSteps) {
            if (!isReady() && lifecycle.state() != RoomLifecycle.State.JOINING) return;
            this.canUndoSteps = canUndoSteps;
            fastRoomContext.notifyRedoUndoChanged(new FastRedoUndo(canRedoSteps, canUndoSteps));
        }

        @Override
        public void onCanRedoStepsUpdate(long canRedoSteps) {
            if (!isReady() && lifecycle.state() != RoomLifecycle.State.JOINING) return;
            this.canRedoSteps = canRedoSteps;
            fastRoomContext.notifyRedoUndoChanged(new FastRedoUndo(canRedoSteps, canUndoSteps));
        }

        @Override
        public void onCatchErrorWhenAppendFrame(long userId, Exception error) {
            FastLogger.warn("receive frame error js userId:" + userId + " error:" + error.getMessage());
        }
    };

    private final Promise<Room> joinRoomPromise = new Promise<Room>() {
        @Override
        public void then(Room room) {
            if (lifecycle.state() == RoomLifecycle.State.DESTROYED) return;
            if (!lifecycle.joined()) {
                finishDisconnect(room);
                return;
            }
            FastLogger.info("join room success");
            FastRoom.this.room = room;
            refreshErrorState();
            updateRoomState(room.getRoomState());
            if (!isReady() || FastRoom.this.room != room) return;
            updateWritable();
            updateIfTextAppliance();
            ensureValidStrokeColor();
            if (!isReady() || FastRoom.this.room != room) return;
            notifyRoomReady();
        }

        @Override
        public void catchEx(SDKError t) {
            boolean cancelled = lifecycle.state() != RoomLifecycle.State.JOINING;
            lifecycle.joinFailed();
            refreshErrorState();
            if (cancelled) completeLeave(null, null);
            else fastRoomContext.notifyFastError(FastException.createRoom(ROOM_JOIN_ERROR, t.getMessage(), t));
        }
    };

    private void updateRoomState(RoomState roomState) {
        if (roomState.getBroadcastState() != null) {
            roomControllerGroup.updateBroadcastState(roomState.getBroadcastState());
        }

        if (roomState.getMemberState() != null) {
            roomControllerGroup.updateMemberState(roomState.getMemberState());
        }

        if (roomState.getSceneState() != null) {
            roomControllerGroup.updateSceneState(roomState.getSceneState());
        }

        if (roomState.getPageState() != null) {
            roomControllerGroup.updatePageState(roomState.getPageState());
        }

        if (roomState.getWindowBoxState() != null) {
            roomControllerGroup.updateWindowBoxState(roomState.getWindowBoxState());
        }
    }

    /**
     * workaround for text appliance when init state
     */
    private void updateIfTextAppliance() {
        MemberState memberState = getRoom().getRoomState().getMemberState();
        if (memberState == null) {
            FastLogger.warn("Member state is unavailable");
            return;
        }
        if (FastAppliance.TEXT.appliance.equals(memberState.getCurrentApplianceName())) {
            fastboardView.whiteboardView.requestFocus();
        }
    }

    /**
     * Ensure that the stroke color is valid.
     */
    private void ensureValidStrokeColor() {
        if (!room.getWritable()) {
            FastLogger.info("Room is not writable, skipping stroke color validation.");
            return;
        }

        MemberState memberState = getRoom().getRoomState().getMemberState();
        if (memberState == null) {
            FastLogger.warn("Member state is unavailable");
            return;
        }

        int color = Util.rgbToColor(memberState.getStrokeColor());
        List<Integer> toolsColors = FastUiSettings.getToolsColors();
        boolean isValidColor = toolsColors != null && toolsColors.contains(color);

        if (!isValidColor) {
            FastLogger.info("Stroke color " + Util.toHexColorString(color) + " not in tools colors, using default color");
            if (toolsColors != null && !toolsColors.isEmpty()) {
                int randomIndex = Util.randomInt(toolsColors.size());
                setStrokeColor(toolsColors.get(randomIndex));
            }
        }
    }

    private final FastRoomListener interFastRoomListener = new FastRoomListener() {
        @Override
        public void onRoomReadyChanged(FastRoom fastRoom) {
            if (fastRoom.isReady()) {
                roomControllerGroup.setFastRoom(fastRoom);
                roomControllerGroup.updateFastStyle(getFastStyle());
                fastboardView.updateFastStyle(getFastStyle());
                updateWindowStyle();
            }
        }

        @Override
        public void onRoomStateChanged(RoomState roomState) {
            updateRoomState(roomState);
        }

        @Override
        public void onRedoUndoChanged(FastRedoUndo count) {
            roomControllerGroup.updateRedoUndo(count);
        }

        @Override
        public void onOverlayChanged(int key) {
            roomControllerGroup.updateOverlayChanged(key);
        }

        @Override
        public void onFastStyleChanged(FastStyle style) {
            roomControllerGroup.updateFastStyle(style);
            fastboardView.updateFastStyle(getFastStyle());
            updateWindowStyle();
        }
    };

    public FastRoom(FastboardView fastboardView, FastRoomOptions options) {
        this.fastboardView = fastboardView;
        this.fastRoomOptions = options;
        this.fastRoomContext = new FastRoomContext(fastboardView);

        setupView();
    }

    private void setupView() {
        View root = fastboardView;

        ViewGroup container = root.findViewById(R.id.fast_room_controller);
        LoadingLayout loadingLayout = root.findViewById(R.id.fast_loading_layout);
        ErrorHandleLayout errorHandleLayout = root.findViewById(R.id.fast_error_handle_layout);
        OverlayLayout overlayLayout = root.findViewById(R.id.fast_overlay_handle_view);

        roomControllerGroup = new FastRoomController(container);
        roomControllerGroup.addController(loadingLayout);
        roomControllerGroup.addController(errorHandleLayout);
        roomControllerGroup.addController(overlayLayout);

        fastRoomContext.setRoomPhaseHandler(new FastRoomPhaseHandler(loadingLayout));
        fastRoomContext.setErrorHandler(new FastErrorHandler(errorHandleLayout));
        fastRoomContext.setOverlayManager(
            new FastOverlayManager(overlayLayout, new FastOverlayHandler(fastRoomContext)));

        // TODO reconnect has no fastRoom instance
        roomControllerGroup.setFastRoom(this);
        roomControllerGroup.updateFastStyle(getFastStyle());

        // update default ratio
        Fastboard fastboard = fastboardView.fastboard;
        fastboard.setWhiteboardRatio(fastRoomOptions);

        addListener(interFastRoomListener);
    }

    public void join() {
        join(this.onRoomReadyCallback);
    }

    public void join(@Nullable OnRoomReadyCallback onRoomReadyCallback) {
        requireMainThread();
        if (!lifecycle.beginJoin()) {
            fastRoomContext.notifyFastError(FastException.createRoom(ROOM_JOIN_ERROR, "Room is busy or destroyed"));
            return;
        }
        this.onRoomReadyCallback = onRoomReadyCallback;
        initSdkIfNeed(fastRoomOptions.getSdkConfiguration());
        if (lifecycle.state() != RoomLifecycle.State.JOINING) return;
        // workaround, white sdk do not notify RoomPhase.connecting
        fastRoomContext.notifyRoomPhaseChanged(RoomPhase.connecting);
        if (lifecycle.state() != RoomLifecycle.State.JOINING) {
            lifecycle.joinFailed();
            refreshErrorState();
            completeLeave(null, null);
            return;
        }
        whiteSdk.joinRoom(fastRoomOptions.getRoomParams(), roomListener, joinRoomPromise);
    }

    private void initSdkIfNeed(WhiteSdkConfiguration config) {
        if (whiteSdk == null) {
            WhiteboardView whiteboardView = fastboardView.whiteboardView;
            whiteSdk = new WhiteSdk(whiteboardView, fastboardView.getContext(), config, commonCallback);
            whiteSdk.setSlideListener(slideListener);
            whiteSdk.setUnifiedPageStateListener(unifiedPageStateListener);
        }
    }

    private void refreshErrorState() {
        if (lifecycle.state() == RoomLifecycle.State.DESTROYED) return;
        ErrorHandleLayout layout = fastboardView.findViewById(R.id.fast_error_handle_layout);
        layout.refreshRetryState();
    }

    public boolean isReady() {
        return room != null && lifecycle.state() == RoomLifecycle.State.JOINED;
    }

    /** A failed SDK or unconfirmed disconnect requires a new FastboardView/FastRoom. */
    public boolean requiresRecreation() {
        requireMainThread();
        return lifecycle.state() == RoomLifecycle.State.FAILED || lifecycle.state() == RoomLifecycle.State.DESTROYED;
    }

    /** Whether the built-in retry action can safely join on this instance. */
    public boolean canRetryJoin() {
        requireMainThread();
        return lifecycle.state() == RoomLifecycle.State.IDLE;
    }

    /**
     * Gets the original whiteboard room. Note that mostly there is no need to care about the room
     *
     * @return internal room of whiteboard.
     */
    public Room getRoom() {
        return room;
    }

    public WhiteSdk getWhiteSdk() {
        return whiteSdk;
    }

    public OverlayManager getOverlayManager() {
        return fastRoomContext.getOverlayManger();
    }

    private void updateWritable() {
        if (room.getWritable()) {
            room.disableSerialization(false);
        }
    }

    private void notifyRoomReady() {
        fastRoomContext.notifyRoomReadyChanged(this);
        if (!isReady()) return;
        if (onRoomReadyCallback != null) {
            onRoomReadyCallback.onRoomReady(this);
        }
    }

    public void redo() {
        if (isReady()) {
            getRoom().redo();
        }
    }

    public void undo() {
        if (isReady()) {
            getRoom().undo();
        }
    }

    /**
     * set appliance
     *
     * @param fastAppliance
     */
    public void setAppliance(FastAppliance fastAppliance) {
        if (!isReady()) {
            FastLogger.warn("call fast room before join..");
            return;
        }
        if (fastAppliance == FastAppliance.OTHER_CLEAR) {
            cleanScene();
            return;
        }
        MemberState memberState = new MemberState();
        memberState.setCurrentApplianceName(fastAppliance.appliance, fastAppliance.shapeType);
        getRoom().setMemberState(memberState);
    }

    /**
     * set appliance stoke width
     *
     * @param width
     */
    public void setStrokeWidth(int width) {
        if (!isReady()) {
            FastLogger.warn("call fast room before join..");
            return;
        }

        MemberState memberState = new MemberState();
        memberState.setStrokeWidth(width);
        getRoom().setMemberState(memberState);
    }

    /**
     * set appliance color
     *
     * @param color color int as 0xAARRGGBB
     */
    public void setStrokeColor(@ColorInt int color) {
        if (!isReady()) {
            FastLogger.warn("call fast room before join..");
            return;
        }

        MemberState memberState = new MemberState();
        memberState.setStrokeColor(new int[]{
            color >> 16 & 0xff,
            color >> 8 & 0xff,
            color & 0xff,
        });
        getRoom().setMemberState(memberState);
    }

    public void setWindowBoxState(FastWindowBoxState state) {
        if (isReady()) room.setWindowBoxState(state.value());
    }

    private void updateWindowStyle() {
        if (isReady()) room.setPrefersColorScheme(getFastStyle().isDarkMode()
            ? WindowPrefersColorScheme.Dark : WindowPrefersColorScheme.Light);
    }

    public void cleanScene() {
        if (!isReady()) {
            FastLogger.warn("call fast room before join..");
            return;
        }

        getRoom().cleanScene(true);
    }

    /**
     * change room writable
     *
     * @param writable
     */
    public void setWritable(boolean writable) {
        setWritable(writable, null);
    }

    public void setWritable(boolean writable, FastResult<Boolean> result) {
        if (!requireReady(result)) return;
        SessionResult<Boolean> operation = new SessionResult<>(result);
        operation.owner.setWritable(writable, new Promise<Boolean>() {
            @Override
            public void then(Boolean success) {
                onMain(() -> {
                    if (operation.current() && Boolean.TRUE.equals(success)) operation.owner.disableSerialization(false);
                    operation.onSuccess(success);
                });
            }

            @Override
            public void catchEx(SDKError t) {
                operation.onError(t);
            }
        });
    }

    public boolean isWritable() {
        return isReady() && room.getWritable();
    }

    /**
     * Insert Image
     *
     * @param url    image remote url
     * @param width  image display width
     * @param height image display width
     */
    public void insertImage(String url, int width, int height) {
        if (!isReady()) {
            FastLogger.warn("call fast room before join..");
            return;
        }

        String uuid = UUID.randomUUID().toString();

        ImageInformation imageInfo = new ImageInformation();
        imageInfo.setUuid(uuid);
        imageInfo.setWidth(width);
        imageInfo.setHeight(height);
        imageInfo.setCenterX(getRoom().getRoomState().getCameraState().getCenterX());
        imageInfo.setCenterY(getRoom().getRoomState().getCameraState().getCenterY());

        getRoom().insertImage(imageInfo);
        getRoom().completeImageUpload(uuid, url);
    }

    /**
     * Insert Video
     *
     * @param url   video remote url
     * @param title video app title
     */
    public void insertVideo(String url, String title) {
        insertVideo(url, title, null);
    }

    public void insertVideo(String url, String title, @Nullable FastResult<String> result) {
        if (!requireReady(result)) return;
        addApp(WindowAppParam.createMediaPlayerApp(url, title), result);
    }

    /** Returns the committed App ID, not an App-render-ready signal. */
    public void addApp(WindowAppParam param, @Nullable FastResult<String> result) {
        if (!requireReady(result)) return;
        room.addApp(param, new PromiseResultAdapter<>(new SessionResult<>(result)));
    }

    /** True means focus was committed; setup/render failure is a separate App event. */
    public void focusApp(String appId, FastResult<Boolean> result) {
        if (!requireReady(result)) return;
        room.focusApp(appId, new PromiseResultAdapter<>(new SessionResult<>(result)));
    }

    /**
     * Insert Older Convertor Converted PPTX.
     * <p>
     * Note: this method is only for older convertor, and will be deprecated in the future.
     *
     * @param pages
     * @param title
     * @param result
     */
    public void insertPptx(DocPage[] pages, String title, FastResult<String> result) {
        if (!requireReady(result)) return;

        Scene[] scenes = FastConvertor.convertScenes(pages);
        WindowAppParam param = WindowAppParam.createSlideApp(
            "/" + UUID.randomUUID().toString(),
            scenes,
            title
        );
        addApp(param, result);
    }

    /**
     * Insert Projector Converted PPTX. which is converted by https://api.netless.link/v5/projector/tasks.
     *
     * @param taskUuid
     * @param prefixUrl
     * @param result
     */
    public void insertPptx(String taskUuid, String prefixUrl, String title, FastResult<String> result) {
        if (!requireReady(result)) return;

        WindowAppParam param = WindowAppParam.createSlideApp(
            taskUuid,
            prefixUrl,
            title
        );
        addApp(param, result);
    }

    /**
     * insert static doc insert window
     *
     * @param pages
     */
    public void insertStaticDoc(DocPage[] pages, String title, FastResult<String> result) {
        if (!requireReady(result)) return;

        Scene[] scenes = FastConvertor.convertScenes(pages);
        WindowAppParam param = WindowAppParam.createDocsViewerApp(
            "/" + UUID.randomUUID().toString(),
            scenes,
            title
        );
        addApp(param, result);
    }

    /** Opens static converted image pages with Presentation; insertStaticDoc remains DocsViewer. */
    public void insertPresentation(DocPage[] pages, String title, @Nullable FastResult<String> result) {
        insertPresentation(pages, title, null, result);
    }

    /**
     * Opens the built-in Presentation app. Pass converted image pages, not raw PDF/PPT URLs
     * or dynamic ppt:// pages. originSize is optional and uses the native SDK model.
     * Results preserve the SDK app ID/error, with the same session cancellation as other apps.
     */
    public void insertPresentation(DocPage[] pages, String title, @Nullable WindowOriginSize originSize,
                                   @Nullable FastResult<String> result) {
        if (!requireReady(result)) return;
        boolean valid = pages != null && pages.length > 0;
        if (valid) {
            for (DocPage page : pages) {
                if (page == null || page.getSrc() == null || page.getSrc().trim().isEmpty()
                    || page.getSrc().trim().toLowerCase(java.util.Locale.ROOT).startsWith("ppt")
                    || !validPresentationDimension(page.getWidth()) || !validPresentationDimension(page.getHeight())) {
                    valid = false;
                    break;
                }
            }
        }
        if (originSize != null) valid &= validPresentationDimension(originSize.getWidth())
            && validPresentationDimension(originSize.getHeight());
        if (!valid) {
            if (result != null) result.onError(new IllegalArgumentException("Presentation requires nonempty static image pages and positive finite dimensions"));
            return;
        }
        WindowAppParam param = WindowAppParam.createPresentationApp(
            "/" + UUID.randomUUID(), FastConvertor.convertScenes(pages), title);
        param.setOriginSize(originSize);
        addApp(param, result);
    }

    private static boolean validPresentationDimension(@Nullable Double value) {
        return value != null && !value.isNaN() && !value.isInfinite() && value > 0;
    }

    /**
     * insert static or dynamic doc insert window
     *
     * @param params
     * @param result
     * @deprecated Use {@link #insertStaticDoc(DocPage[], String, FastResult)} for static doc. Use {@link #insertPptx}
     * for dynamic doc.
     */
    public void insertDocs(FastInsertDocParams params, @Nullable FastResult<String> result) {
        if (!requireReady(result)) return;
        SessionResult<String> operation = new SessionResult<>(result);
        if (params.getConverterType() == ConverterType.WhiteboardConverter) {
            insertDocsWhiteboard(params, operation);
        } else {
            insertDocsProjector(params, operation);
        }
    }

    private void insertDocsWhiteboard(FastInsertDocParams params, SessionResult<String> result) {
        Region region = FastConvertor.convertRegion(params.getRegion());

        ConverterV5 convert = new ConverterV5.Builder()
            .setResource("")
            .setType(params.isDynamicDoc() ? ConvertType.Dynamic : ConvertType.Static)
            .setTaskUuid(params.getTaskUUID())
            .setTaskToken(params.getTaskToken())
            .setRegion(region)
            .setPoolInterval(3000)
            .setTimeout(30_000L)
            .setCallback(new ConverterCallbacks() {
                @Override
                public void onProgress(Double progress, ConversionInfo convertInfo) {
                }

                @Override
                public void onFinish(ConvertedFiles converted, ConversionInfo convertInfo) {
                    WindowAppParam param = params.isDynamicDoc()
                        ? WindowAppParam.createSlideApp(generateUniqueDir(params.getTaskUUID()), converted.getScenes(), params.getTitle())
                        : WindowAppParam.createDocsViewerApp(generateUniqueDir(params.getTaskUUID()), converted.getScenes(), params.getTitle());
                    addConvertedApp(param, result);
                }

                private String generateUniqueDir(String taskUUID) {
                    String uuid = UUID.randomUUID().toString();
                    return String.format("/%s/%s", taskUUID, uuid);
                }

                @Override
                public void onFailure(ConvertException e) {
                    if (result != null) {
                        result.onError(e);
                    }
                }
            }).build();
        convert.startConvertTask();
    }

    private void insertDocsProjector(FastInsertDocParams params, SessionResult<String> result) {
        Region region = FastConvertor.convertRegion(params.getRegion());

        ProjectorQuery projectorQuery = new ProjectorQuery.Builder()
            .setTaskUuid(params.getTaskUUID())
            .setTaskToken(params.getTaskToken())
            .setRegion(region)
            .setPoolInterval(3000)
            .setTimeout(30_000L)
            .setCallback(new ProjectorQuery.Callback() {
                @Override
                public void onProgress(double progress, ProjectorQuery.QueryResponse convertInfo) {

                }

                @Override
                public void onFinish(ProjectorQuery.QueryResponse response) {
                    WindowAppParam param = WindowAppParam.createSlideApp(
                        response.getUuid(),
                        response.getPrefix(),
                        params.getTitle()
                    );
                    addConvertedApp(param, result);
                }

                @Override
                public void onFailure(ConvertException e) {
                    if (result != null) {
                        result.onError(e);
                    }
                }
            })
            .build();
        projectorQuery.startQuery();
    }

    private void addConvertedApp(WindowAppParam param, SessionResult<String> result) {
        onMain(() -> {
            if (result.current()) result.owner.addApp(param, new PromiseResultAdapter<>(result));
            else result.cancel();
        });
    }

    /** Composes SDK callbacks without replacing Fastboard's internal error handling. */
    public void setCommonCallback(@Nullable CommonCallback callback) {
        requireMainThread();
        externalCommonCallback = callback;
    }

    public void setSlideListener(@Nullable SlideListener listener) {
        requireMainThread();
        slideListener = listener;
        if (whiteSdk != null) whiteSdk.setSlideListener(listener);
    }

    public void setUnifiedPageStateListener(@Nullable UnifiedPageStateListener listener) {
        requireMainThread();
        unifiedPageStateListener = listener;
        if (whiteSdk != null) whiteSdk.setUnifiedPageStateListener(listener);
    }

    /** Returns the SDK's accepted/reason/message result, not a Boolean. */
    public void dispatchDocsEvent(WindowDocsEvent event, FastResult<DispatchDocsEventResult> result) {
        if (!requireReady(result)) return;
        room.dispatchDocsEvent(event, new PromiseResultAdapter<>(new SessionResult<>(result)));
    }

    /**
     * Unified page observation; does not change the toolbar's main-page semantics.
     * During page transitions the SDK may report an unconfirmed state error.
     * Wait for a unified page success event or retry with a deadline; accepted is not render-ready.
     */
    public void getPageState(@Nullable WindowPageStateOptions options, FastResult<UnifiedPageState> result) {
        if (!requireReady(result)) return;
        room.getPageState(options, new PromiseResultAdapter<>(new SessionResult<>(result)));
    }

    public void querySlidePageState(@Nullable String appId, FastResult<SlidePageState> result) {
        if (!requireReady(result)) return;
        room.querySlidePageState(appId, new PromiseResultAdapter<>(new SessionResult<>(result)));
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        fastRoomContext.setErrorHandler(errorHandler);
    }

    public void setRoomPhaseHandler(RoomPhaseHandler roomPhaseHandler) {
        fastRoomContext.setRoomPhaseHandler(roomPhaseHandler);
    }

    public void setOverlayHandler(OverlayHandler overlayHandler) {
        fastRoomContext.setOverlayHandler(overlayHandler);
    }

    public OverlayManager getOverlayManger() {
        return fastRoomContext.getOverlayManger();
    }

    public void addListener(FastRoomListener listener) {
        fastRoomContext.addListener(listener);
    }

    public void removeListener(FastRoomListener listener) {
        fastRoomContext.removeListener(listener);
    }

    /**
     * register app
     *
     * @param params
     * @param result
     */
    public void registerApp(FastRegisterAppParams params, FastResult<Boolean> result) {
        requireMainThread();
        if (requiresRecreation()) {
            if (result != null) result.onError(cancelledError());
            return;
        }
        initSdkIfNeed(fastRoomOptions.getSdkConfiguration());
        if (requiresRecreation()) {
            if (result != null) result.onError(cancelledError());
            return;
        }
        SdkResult<Boolean> completion = new SdkResult<>(result);
        whiteSdk.registerApp(FastConvertor.convertRegisterAppParams(params), new Promise<Boolean>() {
            @Override
            public void then(Boolean aBoolean) {
                onMain(() -> completion.finish(aBoolean, null));
            }

            @Override
            public void catchEx(SDKError t) {
                onMain(() -> completion.finish(null, t));
            }
        });
    }

    /**
     * @return return a copy of current fastStyle
     */
    public FastStyle getFastStyle() {
        return fastRoomContext.getFastStyle().copy();
    }

    /**
     * update room board new style
     *
     * @param style
     */
    public void setFastStyle(FastStyle style) {
        fastRoomContext.setFastStyle(style);
    }

    public void setResource(FastResource fastResource) {
        fastRoomContext.setResource(fastResource);
        updateUiFastStyle(getFastStyle());
    }

    private void updateUiFastStyle(FastStyle fastStyle) {
        roomControllerGroup.updateFastStyle(fastStyle);
        fastboardView.updateFastStyle(fastStyle);
    }

    public void disconnect() {
        disconnect(null);
    }

    public void disconnect(@Nullable FastResult<Object> result) {
        requireMainThread();
        if (lifecycle.state() == RoomLifecycle.State.DESTROYED || lifecycle.state() == RoomLifecycle.State.FAILED) {
            if (result != null) result.onError(cancelledError());
            return;
        }
        if (lifecycle.state() == RoomLifecycle.State.IDLE) {
            if (result != null) result.onSuccess(null);
            return;
        }
        if (result != null) leaveResults.add(result);
        if (lifecycle.state() == RoomLifecycle.State.CANCELLING || lifecycle.state() == RoomLifecycle.State.LEAVING) {
            return; // Coalesce reentrant disconnects; never republish not-ready recursively.
        }
        lifecycle.beginLeave();
        Room oldRoom = room;
        room = null;
        cancelPendingResults();
        fastRoomContext.notifyRoomReadyChanged(this);
        if (oldRoom != null && lifecycle.state() != RoomLifecycle.State.DESTROYED) finishDisconnect(oldRoom);
    }

    private void finishDisconnect(Room oldRoom) {
        oldRoom.disconnect(new Promise<Object>() {
            @Override
            public void then(Object o) {
                lifecycle.left(true);
                refreshErrorState();
                completeLeave(o, null);
            }

            @Override
            public void catchEx(SDKError t) {
                // Do not reuse a bridge whose old room may still be connected.
                lifecycle.left(false);
                refreshErrorState();
                completeLeave(null, t);
            }
        });
    }

    private void completeLeave(@Nullable Object value, @Nullable Exception error) {
        List<FastResult<Object>> results = new ArrayList<>(leaveResults);
        leaveResults.clear();
        for (FastResult<Object> result : results) {
            if (error == null) result.onSuccess(value);
            else result.onError(error);
        }
    }

    public void destroy() {
        requireMainThread();
        if (lifecycle.state() == RoomLifecycle.State.DESTROYED) return;
        lifecycle.destroy();
        room = null;
        cancelPendingResults();
        cancelSdkResults();
        completeLeave(null, cancelledError());
        onRoomReadyCallback = null;
        externalCommonCallback = null;
        slideListener = null;
        unifiedPageStateListener = null;
        if (whiteSdk != null) {
            whiteSdk.setSlideListener(null);
            whiteSdk.setUnifiedPageStateListener(null);
            whiteSdk.releaseRoom();
        }
        try {
            fastRoomContext.notifyRoomReadyChanged(this);
        } finally {
            fastRoomContext.close();
            WhiteboardView whiteboardView = fastboardView.whiteboardView;
            fastboardView.removeView(whiteboardView);
            WhiteboardViewManager.get().release(whiteboardView);
        }
    }

    public RoomControllerGroup getRootRoomController() {
        return roomControllerGroup;
    }

    public void setRootRoomController(RoomControllerGroup roomControllerGroup) {
        RoomControllerGroup oldGroup = this.roomControllerGroup;
        oldGroup.hide();
        oldGroup.removeAll();

        this.roomControllerGroup = roomControllerGroup;
        roomControllerGroup.setFastRoom(this);
        roomControllerGroup.updateFastStyle(fastRoomContext.getFastStyle());
    }

    public FastboardView getFastboardView() {
        return fastboardView;
    }
}
