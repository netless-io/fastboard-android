package io.agora.board.fast.sample.cases.helper;

import com.herewhite.sdk.RoomParams;
import com.herewhite.sdk.WhiteSdkConfiguration;
import com.herewhite.sdk.domain.WindowParams;

import io.agora.board.fast.Fastboard;
import io.agora.board.fast.FastboardView;
import io.agora.board.fast.model.FastRoomOptions;

public class RoomOptionsProcessor {
    private final FastboardView fastboardView;
    private final Fastboard fastboard;
    private final FastRoomOptions roomOptions;

    public RoomOptionsProcessor(FastboardView fastboardView, FastRoomOptions roomOptions) {
        this.fastboardView = fastboardView;
        this.fastboard = fastboardView.getFastboard();
        this.roomOptions = roomOptions;
    }

    public void useMultiViews(boolean use) {
        WhiteSdkConfiguration sdkConfiguration = roomOptions.getSdkConfiguration();
        sdkConfiguration.setUseMultiViews(false);
        roomOptions.setSdkConfiguration(sdkConfiguration);
    }

    /**
     * [hwRatio] h:w ratio
     */
    public void updateRatio(Float hwRatio) {
        RoomParams roomParams = roomOptions.getRoomParams();
        WindowParams windowParams = new WindowParams()
                .setContainerSizeRatio(hwRatio)
                .setChessboard(false);
        roomParams.setWindowParams(windowParams);
        roomOptions.setRoomParams(roomParams);
        // before version 1.1.0 needs call this
        // fastboard.setWhiteboardRatio(hwRatio);
    }
}
