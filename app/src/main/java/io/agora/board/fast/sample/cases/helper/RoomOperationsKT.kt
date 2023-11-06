package io.agora.board.fast.sample.cases.helper

import com.herewhite.sdk.domain.Promise
import com.herewhite.sdk.domain.SDKError
import com.herewhite.sdk.domain.WindowAppParam
import com.herewhite.sdk.domain.WindowAppParam.Options
import com.herewhite.sdk.domain.WindowRegisterAppParams
import io.agora.board.fast.FastRoom
import java.util.Arrays

class RoomOperationsKT(val fastRoom: FastRoom) {
    /**
     * for cn region, use https://netless-app.oss-cn-hangzhou.aliyuncs.com/ for other region, use
     * https://cdn.jsdelivr.net/ or https://unpkg.com/
     */
    fun registerApps() {
        val paramsList = Arrays.asList( // Youtube, OnlineVideo etc.
            WindowRegisterAppParams(
                // https://cdn.jsdelivr.net/npm/@netless/app-plyr@0.1.3/dist/main.iife.js
                // https://unpkg.com/@netless/app-plyr@0.1.3/dist/main.iife.js
                "https://netless-app.oss-cn-hangzhou.aliyuncs.com/@netless/app-plyr/0.1.3/dist/main.iife.js",
                "Plyr", emptyMap()
            ),  // EmbeddedPage app
            WindowRegisterAppParams(
                "https://netless-app.oss-cn-hangzhou.aliyuncs.com/@netless/app-embedded-page/0.1.1/dist/main.iife.js",
                "EmbeddedPage", emptyMap()
            ),
            WindowRegisterAppParams(
                "https://netless-app.oss-cn-hangzhou.aliyuncs.com/@netless/app-monaco/0.1.14-beta.1/dist/main.iife.js",
                "Monaco", emptyMap()
            )
        )
        for (params in paramsList) {
            fastRoom.whiteSdk.registerApp(params, object : Promise<Boolean> {
                override fun then(t: Boolean) {
                    // register success
                }

                override fun catchEx(t: SDKError) {
                    // register fail
                }
            })
        }
    }

    class PlyrAttributes @JvmOverloads constructor(
        val src: String,
        val provider: String? = PLYR_PROVIDER_NORMAL
    ) : WindowAppParam.Attributes() {

        companion object {
            @JvmField
            val PLYR_PROVIDER_YOUTUBE = "youtube"

            @JvmField
            val PLYR_PROVIDER_VIMEO = "vimeo"

            @JvmField
            val PLYR_PROVIDER_NORMAL: String? = null
        }
    }

    companion object {
        const val KIND_PLYR = "Plyr"
    }

    fun addYoutubeApp() {
        // youtube
        val options = Options("Youtube")
        val attributes = PlyrAttributes(
            "https://www.youtube.com/embed/bTqVqk7FSmY",
            PlyrAttributes.PLYR_PROVIDER_YOUTUBE
        )
        val param = WindowAppParam(KIND_PLYR, options, attributes)
        fastRoom.room.addApp(param, object : Promise<String> {
            override fun then(t: String) {
                // success
            }

            override fun catchEx(t: SDKError) {
                // fail
            }
        })
    }
}