package io.agora.board.fast.sample.misc;

public class TestCase {
    public String theme;
    public String describe;

    public RoomInfo roomInfo;
    public ReplayInfo replayInfo;

    public TestCase(String theme, String describe, RoomInfo roomInfo) {
        this.theme = theme;
        this.describe = describe;
        this.roomInfo = roomInfo;
    }

    public TestCase(String theme, String describe, ReplayInfo replayInfo) {
        this.theme = theme;
        this.describe = describe;
        this.replayInfo = replayInfo;
    }

    public boolean isLive() {
        return roomInfo != null;
    }

    public static class RoomInfo {
        public String roomUUID;
        public String roomToken;

        public boolean writable;

        public RoomInfo(String roomUUID, String roomToken, boolean writable) {
            this.roomUUID = roomUUID;
            this.roomToken = roomToken;
            this.writable = writable;
        }
    }

    public static class ReplayInfo {
        public String roomUUID;
        public String roomToken;

        public ReplayInfo(String roomUUID, String roomToken) {
            this.roomUUID = roomUUID;
            this.roomToken = roomToken;
        }
    }
}


