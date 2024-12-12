package io.agora.board.fast.sample.cases.helper

import com.herewhite.sdk.domain.Promise
import com.herewhite.sdk.domain.SDKError
import com.herewhite.sdk.domain.WindowAppParam
import com.herewhite.sdk.domain.WindowAppParam.Options
import io.agora.board.fast.FastRoom
import io.agora.board.fast.extension.FastResult
import io.agora.board.fast.model.FastRegisterAppParams
import io.agora.board.fast.sample.misc.Utils
import java.io.IOException
import java.util.Arrays

class RoomOperationsKT(val fastRoom: FastRoom) {
    private val context = fastRoom.fastboardView.context

    /**
     * for cn region, use https://netless-app.oss-cn-hangzhou.aliyuncs.com/ for other region, use
     * https://cdn.jsdelivr.net/ or https://unpkg.com/
     */
    fun registerApps() {
        val paramsList = Arrays.asList(
            // Plyr app remote js
            // Youtube, OnlineVideo etc.
            // FastRegisterAppParams(
            //     // https://cdn.jsdelivr.net/npm/@netless/app-plyr@0.1.3/dist/main.iife.js
            //     // https://unpkg.com/@netless/app-plyr@0.1.3/dist/main.iife.js
            //     "https://netless-app.oss-cn-hangzhou.aliyuncs.com/@netless/app-plyr/0.1.3/dist/main.iife.js",
            //     "Plyr",
            //     emptyMap(),
            // ),
            // Plyr app local js
            FastRegisterAppParams(
                getAppJsFromAsserts("app/appPlyr-v0.1.3.iife.js"),
                "Plyr",
                "NetlessAppPlyr.default",
                emptyMap()
            ),
            // EmbeddedPage app
            FastRegisterAppParams(
                "https://netless-app.oss-cn-hangzhou.aliyuncs.com/@netless/app-embedded-page/0.1.1/dist/main.iife.js",
                "EmbeddedPage",
                emptyMap()
            ),
            // Monaco app
            FastRegisterAppParams(
                "https://netless-app.oss-cn-hangzhou.aliyuncs.com/@netless/app-monaco/0.1.14-beta.1/dist/main.iife.js",
                "Monaco",
                emptyMap(),
            )
        )
        for (params in paramsList) {
            fastRoom.registerApp(params, object : FastResult<Boolean> {
                override fun onSuccess(value: Boolean?) {
                    // register success
                }

                override fun onError(exception: Exception?) {
                    // register fail
                }
            })
        }
    }

    class PlyrAttributes @JvmOverloads constructor(
        val src: String, val provider: String? = PLYR_PROVIDER_NORMAL
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
            PlyrAttributes.PLYR_PROVIDER_YOUTUBE,
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

    private fun getAppJsFromAsserts(path: String): String {
        return try {
            Utils.getStringFromAsserts(context, path)
        } catch (ignored: IOException) {
            ""
        }
    }
}