package io.agora.board.fast.sample.cases.helper

import android.app.AlertDialog
import android.widget.EditText
import android.widget.LinearLayout
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

/**
 * Room operations
 */
class RoomOperationsKT(val fastRoom: FastRoom) {
    private val context = fastRoom.fastboardView.context

    /**
     * Register apps, all netless apps can be found in {@link https://github.com/netless-io/netless-app/}
     *
     * for cn region, use https://netless-app.oss-cn-hangzhou.aliyuncs.com/ for other region, use
     * https://cdn.jsdelivr.net/ or https://unpkg.com/
     */
    fun registerApps() {
        val paramsList = listOf(
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
                /* javascriptString = */ getAppJsFromAsserts("app/appPlyr-v0.2.6.iife.js"),
                /* kind = */ "Plyr",
                /* variable = */ "NetlessAppPlyr.default",
                /* appOptions = */ emptyMap()
            ),
            // EmbeddedPage app
            FastRegisterAppParams(
                getAppJsFromAsserts("app/embedPage.iife.js"),
                "EmbeddedPage",
                "NetlessAppEmbeddedPage.default",
                emptyMap(),
            ),
            // Monaco app
            FastRegisterAppParams(
                "https://netless-app.oss-cn-hangzhou.aliyuncs.com/@netless/app-monaco/0.1.14-beta.1/dist/main.iife.js",
                "Monaco",
                emptyMap(),
            ),
            // geogebra app
            FastRegisterAppParams(
                getAppJsFromAsserts("app/geogebra.iife.js"),
                "GeoGebra",
                "NetlessAppGeoGebra.default",
                emptyMap(),
            ),
            // quill app
            FastRegisterAppParams(
                getAppJsFromAsserts("app/quill.iife.js"),
                "Quill",
                "NetlessAppQuill.default",
                emptyMap(),
            ),
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
        val src: String,
        val provider: String? = PLYR_PROVIDER_NORMAL,
        val type: String? = null,
        val poster: String? = null,
    ) : WindowAppParam.Attributes() {

        companion object {
            @JvmField
            val PLAY_TYPE_VIDEO = "video/mp4"

            @JvmField
            val PLAY_TYPE_AUDIO = "audio/mp3"

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
        addYoutubeApp("https://www.youtube.com/embed/bTqVqk7FSmY")
    }

    fun addYoutubeApp(url: String) {
        val options = Options("Youtube")
        val attributes = PlyrAttributes(
            url,
            provider = PlyrAttributes.PLYR_PROVIDER_YOUTUBE,
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

    fun addPlyrVideo() {
        addPlyrVideo("https://whiteboard-cros-test.oss-cn-hangzhou.aliyuncs.com/BigBuckBunny_tiny.mp4")
    }

    fun addPlyrVideo(url: String, poster: String? = null) {
        val options = Options("Video")
        val attributes = PlyrAttributes(
            src = url,
            poster = poster,
            type = PlyrAttributes.PLAY_TYPE_VIDEO,
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

    fun showVideoInputDialog() {
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 24)
        }

        val urlInput = EditText(context).apply {
            hint = "输入视频地址 (MP4)"
            setText("https://whiteboard-cros-test.oss-cn-hangzhou.aliyuncs.com/BigBuckBunny_tiny.mp4")
        }
        container.addView(urlInput)

        AlertDialog.Builder(context)
            .setTitle("添加视频")
            .setView(container)
            .setPositiveButton("确定") { _, _ ->
                val url = urlInput.text.toString().trim()
                if (url.isNotEmpty()) {
                    addPlyrVideo(url)
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }

    fun showYoutubeInputDialog() {
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 24)
        }

        val urlInput = EditText(context).apply {
            hint = "输入 YouTube 地址"
            setText("https://www.youtube.com/embed/bTqVqk7FSmY")
        }
        container.addView(urlInput)

        AlertDialog.Builder(context)
            .setTitle("添加 YouTube 视频")
            .setView(container)
            .setPositiveButton("确定") { _, _ ->
                val url = urlInput.text.toString().trim()
                if (url.isNotEmpty()) {
                    addYoutubeApp(url)
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }
}