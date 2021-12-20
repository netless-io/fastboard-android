package io.agora.board.fast;

import com.herewhite.sdk.Room;
import com.herewhite.sdk.RoomListener;
import com.herewhite.sdk.RoomParams;
import com.herewhite.sdk.domain.MemberState;
import com.herewhite.sdk.domain.Promise;
import com.herewhite.sdk.domain.RoomPhase;
import com.herewhite.sdk.domain.RoomState;
import com.herewhite.sdk.domain.SDKError;

import io.agora.board.fast.internal.FastErrorHandler;
import io.agora.board.fast.model.FastRoomOptions;

public class FastRoom {
    private final FastSdk fastSdk;
    private final FastContext fastContext;
    private final RoomParams params;

    private Room room;

    public FastRoom(FastSdk fastSdk, FastRoomOptions options) {
        this.fastSdk = fastSdk;
        this.fastContext = fastSdk.fastContext;
        this.params = new RoomParams(options.getUuid(), options.getToken(), options.getUid());
    }

    public void join() {
        fastSdk.whiteSdk.joinRoom(params, roomListener, joinRoomPromise);
    }

    public Room getRoom() {
        return room;
    }

    private Promise<Room> joinRoomPromise = new Promise<Room>() {
        @Override
        public void then(Room room) {
            FastLogger.info("join room success" + room.toString());
            FastRoom.this.room = room;
            notifyRoomState(room.getRoomState());
        }

        @Override
        public void catchEx(SDKError t) {
            FastLogger.error("join room error", t);

            FastErrorHandler errorHandler = fastContext.errorHandler;
            errorHandler.onJoinRoomError(new FastException(t.getMessage(), t));
        }
    };

    private RoomListener roomListener = new RoomListener() {
        private long canUndoSteps;
        private long canRedoSteps;

        @Override
        public void onPhaseChanged(RoomPhase phase) {
            fastContext.notifyRoomPhaseChanged(phase);
        }

        @Override
        public void onDisconnectWithError(Exception e) {

        }

        @Override
        public void onKickedWithReason(String reason) {

        }

        @Override
        public void onRoomStateChanged(RoomState modifyState) {
            notifyRoomState(modifyState);
        }

        @Override
        public void onCanUndoStepsUpdate(long canUndoSteps) {
            canUndoSteps = canUndoSteps;
        }

        @Override
        public void onCanRedoStepsUpdate(long canRedoSteps) {
            canRedoSteps = canRedoSteps;
        }

        @Override
        public void onCatchErrorWhenAppendFrame(long userId, Exception error) {

        }
    };

    private void notifyRoomState(RoomState roomState) {
        if (roomState.getBroadcastState() != null) {
            fastContext.notifyBroadcastStateChanged(roomState.getBroadcastState());
        }

        if (roomState.getMemberState() != null) {
            fastContext.notifyMemberStateChanged(roomState.getMemberState());
        }
    }

    public void setColor(Integer color) {
        MemberState memberState = new MemberState();
        memberState.setStrokeColor(new int[]{
                color >> 16 & 0xff,
                color >> 8 & 0xff,
                color & 0xff,
        });
        getRoom().setMemberState(memberState);
    }

    public void setAppliance(String appliance) {
        MemberState memberState = new MemberState();
        memberState.setCurrentApplianceName(appliance);
        getRoom().setMemberState(memberState);
    }

    public void setStokeWidth(int width) {
        MemberState memberState = new MemberState();
        memberState.setStrokeWidth(width);
        getRoom().setMemberState(memberState);
    }

    public void cleanScene() {
        getRoom().cleanScene(true);
    }
}
