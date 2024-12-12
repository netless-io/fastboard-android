# 窗口应用管理

Fastboard 支持在多窗口中运行多个应用程序，每个应用程序均为独立窗口，可运行不同的功能模块。对于外部应用程序，Fastboard
提供了灵活的注册与添加方式。以下为详细指南。

## 注册应用程序支持

在注册外部应用程序前，请注意以下关键事项：
1.版本一致性：确保各端的应用版本一致或兼容，否则可能导致运行异常。
2.注册时机：务必在加入房间之前完成注册，否则应用程序可能无法正常加载。

### 示例：注册 Plyr 播放器

以下以 Plyr 播放器为例，展示注册过程。

#### 方法一：通过本地文件注册

若使用本地文件注册，需指定挂载的全局变量名。此变量名在 JavaScript 文件中暴露，可参考插件文档获取。Netless
应用插件的变量命名通常为 NetlessApp${PluginName}.default。

```kotlin
FastRegisterAppParams(
    /* javascriptString = */ getAppJsFromAsserts("app/appPlyr-v0.1.3.iife.js"),
    /* kind = */ "Plyr",
    /* variable = */ "NetlessAppPlyr.default",
    /* appOptions = */ emptyMap()
)

fastRoom.registerApp(params, object : FastResult<Boolean> {
    override fun onSuccess(value: Boolean?) {
        // register success
    }

    override fun onError(exception: Exception?) {
        // register fail
    }
})
```

#### 方法二：通过网络文件注册

若使用网络文件注册，请确保文件地址可访问。建议将文件托管至可控的 CDN 以保障稳定性。

```kotlin
val params = FastRegisterAppParams(
    "https://cdn.jsdelivr.net/npm/@netless/app-plyr@0.1.3/dist/main.iife.js",
    "Plyr",
    emptyMap(),
)

fastRoom.registerApp(params, object : FastResult<Boolean> {
    override fun onSuccess(value: Boolean?) {
        // register success
    }

    override fun onError(exception: Exception?) {
        // register fail
    }
})
```

## 添加应用程序

对于已注册的应用程序，可通过以下方式将其添加到房间中。添加时需配置 Options 和 Attributes
参数，其具体值请参考三方应用程序文档。

### 添加 YouTube 播放器

以下展示如何通过 Plyr 播放器加载 YouTube 视频：

```kotlin
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
    val KIND_PLYR = "Plyr"
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
```

## 参考文档

[plyr README](https://github.com/netless-io/netless-app/tree/master/packages/app-plyr)