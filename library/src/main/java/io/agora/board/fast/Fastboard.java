package io.agora.board.fast;

import android.widget.FrameLayout;

import com.herewhite.sdk.WhiteboardView;

import io.agora.board.fast.model.FastReplayOptions;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastStyle;

public class Fastboard {
    public static final String VERSION = "1.0.0";

    private final FastboardView fastboardView;
    private FastStyle fastStyle;
    private float whiteboardRatio = 9.0f / 16;

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

    public void setWhiteboardRatio(float ratio) {
        this.whiteboardRatio = ratio;
        updateWhiteboardLayout(fastboardView.getWidth(), fastboardView.getHeight());
    }

    void updateWhiteboardLayout(int w, int h) {
        WhiteboardView whiteboardView = fastboardView.whiteboardView;

        FrameLayout.LayoutParams lps = (FrameLayout.LayoutParams) whiteboardView.getLayoutParams();
        if (h / whiteboardRatio >= w) {
            lps.height = (int) (w * whiteboardRatio);
            lps.width = w;
        } else {
            lps.height = h;
            lps.width = (int) (h / whiteboardRatio);
        }
        whiteboardView.setLayoutParams(lps);
    }

    public String getVersion() {
        return VERSION;
    }
}
