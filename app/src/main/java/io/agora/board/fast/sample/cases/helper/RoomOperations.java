package io.agora.board.fast.sample.cases.helper;

import com.herewhite.sdk.domain.Promise;
import com.herewhite.sdk.domain.SDKError;
import com.herewhite.sdk.domain.WindowAppParam;
import com.herewhite.sdk.domain.WindowAppParam.Options;
import com.herewhite.sdk.domain.WindowRegisterAppParams;
import io.agora.board.fast.FastRoom;
import io.agora.board.fast.sample.cases.helper.RoomOperationsKT.PlyrAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RoomOperations {

    private final FastRoom fastRoom;

    public RoomOperations(FastRoom fastRoom) {
        this.fastRoom = fastRoom;
    }

    /**
     * for cn region, use https://netless-app.oss-cn-hangzhou.aliyuncs.com/ for other region, use
     * https://cdn.jsdelivr.net/ or https://unpkg.com/
     * <p>
     *
     * @param fastRoom
     */
    public void registerApps(FastRoom fastRoom) {
        List<WindowRegisterAppParams> paramsList = Arrays.asList(
            // Youtube, OnlineVideo etc.
            new WindowRegisterAppParams(
                // https://cdn.jsdelivr.net/npm/@netless/app-plyr@0.1.3/dist/main.iife.js
                // https://unpkg.com/@netless/app-plyr@0.1.3/dist/main.iife.js
                "https://netless-app.oss-cn-hangzhou.aliyuncs.com/@netless/app-plyr/0.1.3/dist/main.iife.js",
                "Plyr",
                Collections.emptyMap()
            ),

            // EmbeddedPage app
            new WindowRegisterAppParams(
                "https://netless-app.oss-cn-hangzhou.aliyuncs.com/@netless/app-embedded-page/0.1.1/dist/main.iife.js",
                "EmbeddedPage",
                Collections.emptyMap()
            ),

            new WindowRegisterAppParams(
                "https://netless-app.oss-cn-hangzhou.aliyuncs.com/@netless/app-monaco/0.1.14-beta.1/dist/main.iife.js",
                "Monaco",
                Collections.emptyMap()
            )
        );

        for (WindowRegisterAppParams params : paramsList) {
            fastRoom.getWhiteSdk().registerApp(params, new Promise<Boolean>() {
                @Override
                public void then(Boolean aBoolean) {
                    // register success
                }

                @Override
                public void catchEx(SDKError t) {
                    // register fail
                }
            });
        }
    }

    public void addApps() {
        // youtube
        Options options = new Options("Youtube");
        RoomOperationsKT.PlyrAttributes attributes = new PlyrAttributes(
            "https://www.youtube.com/embed/bTqVqk7FSmY",
            PlyrAttributes.PLYR_PROVIDER_YOUTUBE
        );
        WindowAppParam param = new WindowAppParam(
            RoomOperationsKT.KIND_PLYR,
            options,
            attributes
        );
        fastRoom.getRoom().addApp(param, new Promise<String>() {
            @Override
            public void then(String s) {
                // add success
            }

            @Override
            public void catchEx(SDKError t) {
                // add fail
            }
        });
    }
}
