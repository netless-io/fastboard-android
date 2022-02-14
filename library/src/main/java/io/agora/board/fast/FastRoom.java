package io.agora.board.fast;

import static io.agora.board.fast.FastException.ROOM_DISCONNECT_ERROR;
import static io.agora.board.fast.FastException.ROOM_JOIN_ERROR;

import com.herewhite.sdk.Room;
import com.herewhite.sdk.RoomListener;
import com.herewhite.sdk.RoomParams;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.Promise;
import com.herewhite.sdk.domain.RoomPhase;
import com.herewhite.sdk.domain.RoomState;
import com.herewhite.sdk.domain.SDKError;
import com.herewhite.sdk.internal.Logger;

import io.agora.board.fast.extension.OverlayManager;
import io.agora.board.fast.internal.FastConvertor;
import io.agora.board.fast.model.FastAppliance;
import io.agora.board.fast.model.FastRedoUndo;
import io.agora.board.fast.model.FastRoomOptions;

public class FastRoom {
    private final FastSdk fastSdk;
    private final FastContext fastContext;
    private final RoomParams params;
    private final RoomListener roomListener = new RoomListener() {
        private long canUndoSteps;
        private long canRedoSteps;

        @Override
        public void onPhaseChanged(RoomPhase phase) {
            fastContext.notifyRoomPhaseChanged(phase);
        }

        @Override
        public void onDisconnectWithError(Exception e) {
            FastLogger.warn("receive disconnect error from js " + e.getMessage());
            fastContext.notifyFastError(FastException.createRoom(ROOM_DISCONNECT_ERROR, e.getMessage(), e));
        }

        @Override
        public void onKickedWithReason(String reason) {
            FastLogger.warn("receive kicked from js with reason " + reason);
        }

        @Override
        public void onRoomStateChanged(RoomState modifyState) {
            notifyRoomState(modifyState);
        }

        @Override
        public void onCanUndoStepsUpdate(long canUndoSteps) {
            this.canUndoSteps = canUndoSteps;
            fastContext.notifyRedoUndoChanged(new FastRedoUndo(canRedoSteps, canUndoSteps));
        }

        @Override
        public void onCanRedoStepsUpdate(long canRedoSteps) {
            this.canRedoSteps = canRedoSteps;
            fastContext.notifyRedoUndoChanged(new FastRedoUndo(canRedoSteps, canUndoSteps));
        }

        @Override
        public void onCatchErrorWhenAppendFrame(long userId, Exception error) {
            FastLogger.warn("receive frame error js userId:" + userId + " error:" + error.getMessage());
        }
    };
    
    private Room room;

    private final Promise<Room> joinRoomPromise = new Promise<Room>() {
        @Override
        public void then(Room room) {
            FastLogger.info("join room success" + room.toString());
            FastRoom.this.room = room;
            notifyRoomState(room.getRoomState());
            updateWritable();
        }

        @Override
        public void catchEx(SDKError t) {
            FastLogger.error("join room error", t);
            fastContext.notifyFastError(FastException.createRoom(ROOM_JOIN_ERROR, t.getMessage(), t));
        }
    };

    public FastRoom(FastSdk fastSdk, FastRoomOptions options) {
        this.fastSdk = fastSdk;
        this.fastContext = fastSdk.fastContext;
        this.params = FastConvertor.convertRoomOptions(options);
    }

    public void join() {
        fastSdk.whiteSdk.joinRoom(params, roomListener, joinRoomPromise);
        // workaround, white sdk do not notify RoomPhase.connecting
        fastContext.notifyRoomPhaseChanged(RoomPhase.connecting);
    }

    public Room getRoom() {
        return room;
    }

    public FastContext getFastContext() {
        return fastContext;
    }

    public OverlayManager getOverlayManager() {
        return fastContext.getOverlayManger();
    }

    private void updateWritable() {
        if (room.getWritable()) {
            room.disableSerialization(false);
        }
    }

    private void notifyRoomState(RoomState roomState) {
        if (roomState.getBroadcastState() != null) {
            fastContext.notifyBroadcastStateChanged(roomState.getBroadcastState());
        }

        if (roomState.getMemberState() != null) {
            fastContext.notifyMemberStateChanged(roomState.getMemberState());
        }

        if (roomState.getSceneState() != null) {
            fastContext.notifySceneStateChanged(roomState.getSceneState());
        }

        if (roomState.getWindowBoxState() != null) {
            fastContext.notifyWindowBoxStateChanged(roomState.getWindowBoxState());
        }
    }

    public void setColor(Integer color) {
        if (getRoom() == null) {
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

    public void setAppliance(FastAppliance fastAppliance) {
        if (getRoom() == null) {
            FastLogger.warn("call fast room before join..");
            return;
        }

        MemberState memberState = new MemberState();
        memberState.setCurrentApplianceName(fastAppliance.appliance, fastAppliance.shapeType);
        getRoom().setMemberState(memberState);
    }

    public void setStokeWidth(int width) {
        if (getRoom() == null) {
            FastLogger.warn("call fast room before join..");
            return;
        }

        MemberState memberState = new MemberState();
        memberState.setStrokeWidth(width);
        getRoom().setMemberState(memberState);
    }

    public void cleanScene() {
        if (getRoom() == null) {
            FastLogger.warn("call fast room before join..");
            return;
        }

        getRoom().cleanScene(true);
    }

    public void setWritable(boolean writable) {
        if (getRoom() == null) {
            FastLogger.warn("call fast room before join..");
            return;
        }

        room.setWritable(writable, new Promise<Boolean>() {
            @Override
            public void then(Boolean result) {
                Logger.info("set writable result " + result);
                if (result) {
                    room.disableSerialization(false);
                }
            }

            @Override
            public void catchEx(SDKError t) {
                Logger.error("set writable error", t);
            }
        });
    }
}
