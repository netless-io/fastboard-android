package io.agora.board.fast;

import static io.agora.board.fast.FastException.ROOM_DISCONNECT_ERROR;
import static io.agora.board.fast.FastException.ROOM_JOIN_ERROR;

import android.view.View;
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
import com.herewhite.sdk.domain.SDKError;
import com.herewhite.sdk.domain.Scene;
import com.herewhite.sdk.domain.WindowAppParam;

import org.json.JSONObject;

import java.util.UUID;

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
import io.agora.board.fast.model.ConverterType;
import io.agora.board.fast.model.DocPage;
import io.agora.board.fast.model.FastAppliance;
import io.agora.board.fast.model.FastInsertDocParams;
import io.agora.board.fast.model.FastRedoUndo;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastStyle;
import io.agora.board.fast.ui.ErrorHandleLayout;
import io.agora.board.fast.ui.FastRoomController;
import io.agora.board.fast.ui.LoadingLayout;
import io.agora.board.fast.ui.OverlayLayout;
import io.agora.board.fast.ui.RoomControllerGroup;

public class FastRoom {

    private final FastboardView fastboardView;

    private final FastRoomOptions fastRoomOptions;

    private final FastRoomContext fastRoomContext;

    private WhiteSdk whiteSdk;

    private Room room;

    private OnRoomReadyCallback onRoomReadyCallback;

    private RoomControllerGroup roomControllerGroup;

    private final CommonCallback commonCallback = new CommonCallback() {
        @Override
        public void throwError(Object args) {

        }

        @Override
        public void onMessage(JSONObject object) {

        }

        @Override
        public void sdkSetupFail(SDKError error) {
            FastLogger.error("sdk setup fail ", error);
            fastRoomContext.notifyFastError(FastException.createSdk(error.getMessage()));
        }

        @Override
        public void onLogger(JSONObject object) {
            FastLogger.info(object.toString());
        }
    };

    private final RoomListener roomListener = new RoomListener() {
        private long canUndoSteps;

        private long canRedoSteps;

        @Override
        public void onPhaseChanged(RoomPhase phase) {
            fastRoomContext.notifyRoomPhaseChanged(phase);
        }

        @Override
        public void onDisconnectWithError(Exception e) {
            FastLogger.warn("receive disconnect error from js " + e.getMessage());
            fastRoomContext.notifyFastError(FastException.createRoom(ROOM_DISCONNECT_ERROR, e.getMessage(), e));
        }

        @Override
        public void onKickedWithReason(String reason) {
            FastLogger.warn("receive kicked from js with reason " + reason);
        }

        @Override
        public void onRoomStateChanged(RoomState modifyState) {
            fastRoomContext.notifyRoomStateChanged(modifyState);
        }

        @Override
        public void onCanUndoStepsUpdate(long canUndoSteps) {
            this.canUndoSteps = canUndoSteps;
            fastRoomContext.notifyRedoUndoChanged(new FastRedoUndo(canRedoSteps, canUndoSteps));
        }

        @Override
        public void onCanRedoStepsUpdate(long canRedoSteps) {
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
            FastLogger.info("join room success" + room.toString());
            FastRoom.this.room = room;
            updateRoomState(room.getRoomState());
            updateWritable();
            updateIfTextAppliance();
            notifyRoomReady();
        }

        @Override
        public void catchEx(SDKError t) {
            FastLogger.error("join room error", t);
            fastRoomContext.notifyFastError(FastException.createRoom(ROOM_JOIN_ERROR, t.getMessage(), t));
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
        String applianceName = getRoom().getRoomState().getMemberState().getCurrentApplianceName();
        if (FastAppliance.TEXT.appliance.equals(applianceName)) {
            fastboardView.whiteboardView.requestFocus();
        }
    }

    private final FastRoomListener interFastRoomListener = new FastRoomListener() {
        @Override
        public void onRoomReadyChanged(FastRoom fastRoom) {
            if (fastRoom.isReady()) {
                roomControllerGroup.setFastRoom(fastRoom);
                roomControllerGroup.updateFastStyle(getFastStyle());
                fastboardView.updateFastStyle(getFastStyle());
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
        this.onRoomReadyCallback = onRoomReadyCallback;
        initSdkIfNeed(fastRoomOptions.getSdkConfiguration());
        whiteSdk.joinRoom(fastRoomOptions.getRoomParams(), roomListener, joinRoomPromise);
        // workaround, white sdk do not notify RoomPhase.connecting
        fastRoomContext.notifyRoomPhaseChanged(RoomPhase.connecting);
    }

    private void initSdkIfNeed(WhiteSdkConfiguration config) {
        if (whiteSdk == null) {
            WhiteboardView whiteboardView = fastboardView.whiteboardView;
            whiteSdk = new WhiteSdk(whiteboardView, fastboardView.getContext(), config, commonCallback);
        }
    }

    public boolean isReady() {
        return room != null;
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
        if (onRoomReadyCallback != null) {
            onRoomReadyCallback.onRoomReady(this);
        }
        fastRoomContext.notifyRoomReadyChanged(this);
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
        if (!isReady()) {
            FastLogger.warn("call fast room before join..");
            return;
        }

        room.setWritable(writable, new Promise<Boolean>() {
            @Override
            public void then(Boolean success) {
                FastLogger.info("set writable result " + success);
                if (success) {
                    room.disableSerialization(false);
                }
                if (result != null) {
                    result.onSuccess(success);
                }
            }

            @Override
            public void catchEx(SDKError t) {
                FastLogger.error("set writable error", t);
                if (result != null) {
                    result.onError(t);
                }
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
        if (!isReady()) {
            FastLogger.warn("call fast room before join..");
            return;
        }

        WindowAppParam param = WindowAppParam.createMediaPlayerApp(url, title);
        getRoom().addApp(param, null);
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
        if (!isReady()) {
            FastLogger.warn("call fast room before join..");
            return;
        }

        Scene[] scenes = FastConvertor.convertScenes(pages);
        WindowAppParam param = WindowAppParam.createSlideApp(
            "/" + UUID.randomUUID().toString(),
            scenes,
            title
        );
        getRoom().addApp(param, new PromiseResultAdapter<>(result));
    }

    /**
     * Insert Projector Converted PPTX. which is converted by https://api.netless.link/v5/projector/tasks.
     *
     * @param taskUuid
     * @param prefixUrl
     * @param result
     */
    public void insertPptx(String taskUuid, String prefixUrl, String title, FastResult<String> result) {
        if (!isReady()) {
            FastLogger.warn("call fast room before join..");
            return;
        }

        WindowAppParam param = WindowAppParam.createSlideApp(
            taskUuid,
            prefixUrl,
            title
        );
        getRoom().addApp(param, new PromiseResultAdapter<>(result));
    }

    /**
     * insert static doc insert window
     *
     * @param pages
     */
    public void insertStaticDoc(DocPage[] pages, String title, FastResult<String> result) {
        if (!isReady()) {
            FastLogger.warn("call fast room before join..");
            return;
        }

        Scene[] scenes = FastConvertor.convertScenes(pages);
        WindowAppParam param = WindowAppParam.createDocsViewerApp(
            "/" + UUID.randomUUID().toString(),
            scenes,
            title
        );
        getRoom().addApp(param, new PromiseResultAdapter<>(result));
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
        if (!isReady()) {
            FastLogger.warn("call fast room before join..");
            return;
        }

        if (params.getConverterType() == ConverterType.WhiteboardConverter) {
            insertDocsWhiteboard(params, result);
        } else {
            insertDocsProjector(params, result);
        }
    }

    private void insertDocsWhiteboard(FastInsertDocParams params, @Nullable FastResult<String> result) {
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
                    WindowAppParam param = WindowAppParam.createSlideApp(generateUniqueDir(params.getTaskUUID()),
                        converted.getScenes(), params.getTitle());
                    getRoom().addApp(param, new PromiseResultAdapter<>(result));
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

    private void insertDocsProjector(FastInsertDocParams params, FastResult<String> result) {
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
                    getRoom().addApp(param, new PromiseResultAdapter<>(result));
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
    }

    public void destroy() {
        WhiteboardView whiteboardView = fastboardView.whiteboardView;
        whiteboardView.removeAllViews();
        whiteboardView.destroy();
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
