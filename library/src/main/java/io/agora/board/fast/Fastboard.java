package io.agora.board.fast;

import android.widget.FrameLayout;

import com.herewhite.sdk.WhiteSdkConfiguration;
import com.herewhite.sdk.WhiteboardView;
import com.herewhite.sdk.domain.WindowParams;

import io.agora.board.fast.model.FastReplayOptions;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastStyle;

public class Fastboard {
    public static final String VERSION = "1.5.1";

    private final FastboardView fastboardView;
    private FastStyle fastStyle;
    private Float whiteboardRatio;

    public Fastboard(FastboardView fastboardView) {
        this.fastboardView = fastboardView;
    }

    public FastboardView getFastboardView() {
        return fastboardView;
    }

    public FastRoom createFastRoom(FastRoomOptions roomOptions) {
        return new FastRoom(fastboardView, roomOptions);
    }

    public FastReplay createFastReplay(FastReplayOptions replayOptions) {
        return new FastReplay(fastboardView, replayOptions);
    }

    public FastStyle getFastStyle() {
        return fastStyle.copy();
    }

    void setFastStyle(FastStyle fastStyle) {
        this.fastStyle = fastStyle;
    }

    public void setWhiteboardRatio(Float ratio) {
        this.whiteboardRatio = ratio;
        updateWhiteboardLayout(fastboardView.getWidth(), fastboardView.getHeight());
    }

    /**
     * package api for
     *
     * @param roomOptions
     */
    void setWhiteboardRatio(FastRoomOptions roomOptions) {
        WhiteSdkConfiguration sdkConfiguration = roomOptions.getSdkConfiguration();
        WindowParams windowParams = roomOptions.getRoomParams().getWindowParams();
        if (sdkConfiguration.getUseMultiViews() && windowParams != null) {
            setWhiteboardRatio(windowParams.getContainerSizeRatio());
        }
    }

    void updateWhiteboardLayout(int w, int h) {
        WhiteboardView whiteboardView = fastboardView.whiteboardView;

        FrameLayout.LayoutParams lps = (FrameLayout.LayoutParams) whiteboardView.getLayoutParams();
        if (whiteboardRatio == null) {
            lps.height = FrameLayout.LayoutParams.MATCH_PARENT;
            lps.width = FrameLayout.LayoutParams.MATCH_PARENT;
        } else {
            if (h / whiteboardRatio >= w) {
                lps.height = (int) (w * whiteboardRatio);
                lps.width = w;
            } else {
                lps.height = h;
                lps.width = (int) (h / whiteboardRatio);
            }
        }
        whiteboardView.setLayoutParams(lps);
    }

    public String getVersion() {
        return VERSION;
    }
}
