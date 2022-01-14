package io.agora.board.fast.internal;

import com.herewhite.sdk.RoomParams;
import com.herewhite.sdk.WhiteSdkConfiguration;
import com.herewhite.sdk.domain.PlayerConfiguration;
import com.herewhite.sdk.domain.Region;
import com.herewhite.sdk.domain.WindowParams;
import com.herewhite.sdk.domain.WindowPrefersColorScheme;

import java.util.HashMap;

import io.agora.board.fast.model.FastPlayerOptions;
import io.agora.board.fast.model.FastRegion;
import io.agora.board.fast.model.FastRoomOptions;
import io.agora.board.fast.model.FastSdkOptions;

public class FastConvertor {
    public static WhiteSdkConfiguration convertSdkOptions(FastSdkOptions options) {
        WhiteSdkConfiguration result;
        if (options.getConfiguration() != null) {
            result = options.getConfiguration();
        } else {
            result = new WhiteSdkConfiguration(options.getAppId());
            result.setUseMultiViews(true);
        }
        return result;
    }

    public static RoomParams convertRoomOptions(FastRoomOptions options) {
        RoomParams result;
        if (options.getRoomParams() != null) {
            result = options.getRoomParams();
        } else {
            result = new RoomParams(options.getUuid(), options.getToken(), options.getUid());
            result.setWritable(options.isWritable());
            result.setRegion(convertRegion(options.getFastRegion()));
        }
        result.setDisableNewPencil(false);
        if (result.getWindowParams() == null) {
            HashMap<String, String> styleMap = new HashMap<>();
            styleMap.put("bottom", "30px");
            styleMap.put("right", "44px");
            styleMap.put("position", "fixed");

            WindowParams windowParams = new WindowParams();
            windowParams.setChessboard(false);
            windowParams.setDebug(true);
            windowParams.setCollectorStyles(styleMap);
            windowParams.setPrefersColorScheme(WindowPrefersColorScheme.Auto);
        }
        return result;
    }

    public static PlayerConfiguration convertPlayerOptions(FastPlayerOptions options) {
        PlayerConfiguration result;
        if (options.getPlayerConfiguration() != null) {
            result = options.getPlayerConfiguration();
        } else {
            result = new PlayerConfiguration(options.getUuid(), options.getToken());
            result.setRegion(convertRegion(options.getFastRegion()));
        }
        return result;
    }


    private static Region convertRegion(FastRegion fastRegion) {
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
}
