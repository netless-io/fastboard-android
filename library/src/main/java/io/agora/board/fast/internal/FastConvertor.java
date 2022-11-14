package io.agora.board.fast.internal;

import androidx.annotation.NonNull;

import com.herewhite.sdk.RoomParams;
import com.herewhite.sdk.WhiteSdk;
import com.herewhite.sdk.WhiteSdkConfiguration;
import com.herewhite.sdk.domain.PlayerConfiguration;
import com.herewhite.sdk.domain.Region;
import com.herewhite.sdk.domain.WindowParams;
import com.herewhite.sdk.domain.WindowPrefersColorScheme;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.agora.board.fast.Fastboard;
import io.agora.board.fast.model.FastRegion;
import io.agora.board.fast.model.FastReplayOptions;
import io.agora.board.fast.model.FastRoomOptions;

public class FastConvertor {
    private static List<String> netlessUA = Arrays.asList(
            "fastboard/" + Fastboard.VERSION,
            "whiteboard/" + WhiteSdk.Version()
    );

    public static WhiteSdkConfiguration convertSdkOptions(FastRoomOptions options) {
        WhiteSdkConfiguration result = new WhiteSdkConfiguration(options.getAppId());
        result.setUseMultiViews(true);
        result.setNetlessUA(netlessUA);
        return result;
    }

    public static WhiteSdkConfiguration convertSdkOptions(FastReplayOptions options) {
        WhiteSdkConfiguration result = new WhiteSdkConfiguration(options.getAppId());
        // fast default config
        result.setUseMultiViews(true);
        result.setNetlessUA(netlessUA);
        return result;
    }

    public static RoomParams convertRoomOptions(FastRoomOptions options) {
        RoomParams result = new RoomParams(options.getUuid(), options.getToken(), options.getUid());
        result.setWritable(options.isWritable());
        result.setRegion(convertRegion(options.getFastRegion()));

        // fast default config
        result.setDisableNewPencil(false);
        result.setWindowParams(createWindowParams(options.getContainerSizeRatio()));

        return result;
    }

    public static PlayerConfiguration convertReplayOptions(FastReplayOptions options) {
        PlayerConfiguration result = new PlayerConfiguration(options.getUuid(), options.getToken());
        result.setRegion(convertRegion(options.getFastRegion()));
        result.setWindowParams(createWindowParams(options.getContainerSizeRatio()));
        return result;
    }

    public static Region convertRegion(FastRegion fastRegion) {
        if (fastRegion == null) {
            return Region.cn;
        }
        switch (fastRegion) {
            case US_SV:
                return Region.us;
            case SG:
                return Region.sg;
            case IN_MUM:
                return Region.in_mum;
            case GB_LON:
                return Region.gb_lon;
            case CN_HZ:
            default:
                return Region.cn;
        }
    }

    @NonNull
    private static WindowParams createWindowParams(Float ratio) {
        HashMap<String, String> styleMap = new HashMap<>();
        styleMap.put("bottom", "30px");
        styleMap.put("right", "44px");
        styleMap.put("position", "fixed");
        WindowParams windowParams = new WindowParams();
        windowParams.setChessboard(false);
        windowParams.setContainerSizeRatio(ratio != null ? ratio : 9f / 16);
        windowParams.setDebug(true);
        windowParams.setCollectorStyles(styleMap);
        windowParams.setPrefersColorScheme(WindowPrefersColorScheme.Auto);
        return windowParams;
    }
}
