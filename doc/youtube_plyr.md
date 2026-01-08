# 修复 YouTube 视频播放问题

## 问题描述

在 Fastboard 中使用 Plyr 插件播放 YouTube 视频时,可能会遇到 **YouTube Error 153** 错误,导致视频无法正常播放。

### 错误现象

- 播放器显示 YouTube Error 153
- 视频无法加载和播放
- 控制台可能显示跨域或协议相关的错误信息

## 问题原因

YouTube IFrame API 无法在 `file://` 协议下正常工作。默认情况下,Fastboard 使用 `file://` 协议加载 WhiteboardView 的内置资源,这是最稳定的运行模式。但是,YouTube IFrame Player API 要求通过 HTTP/HTTPS 协议加载,否则会因为安全限制而失败。

### 技术背景

- **默认模式**: WhiteboardView 使用 `file://` 协议加载白板资源
- **YouTube 要求**: YouTube IFrame API 需要 HTTPS 协议
- **冲突**: 协议不匹配导致 YouTube 视频播放失败

## 解决方案

Fastboard 提供了 `enableAssetsHttps` 配置选项,可以将资源加载协议从 `file://` 切换到基于 `WebViewAssetLoader` 的 HTTPS 方案,从而解决 YouTube 视频播放问题。

### 配置方法

在初始化 Fastboard 时,通过 `FastboardConfig` 启用 HTTPS 资源加载:

```kotlin
val config = FastboardConfig.Builder(this)
    .enableAssetsHttps(true)
    .build()
Fastboard.setConfig(config)
```

### 完整示例

以下是一个完整的配置示例,展示如何在 Application 或 Activity 中设置:

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // 配置 Fastboard,启用 HTTPS 资源加载
        val config = FastboardConfig.Builder(this)
            .enableAssetsHttps(true)
            .build()
        Fastboard.setConfig(config)
    }
}
```

或者在 Activity 中:

```kotlin
class WhiteboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 确保在使用 Fastboard 之前进行配置
        val config = FastboardConfig.Builder(this)
            .enableAssetsHttps(true)
            .build()
        Fastboard.setConfig(config)
        
        // 后续的 Fastboard 初始化代码...
    }
}
```

## 配置说明

### enableAssetsHttps 参数

- **类型**: `boolean`
- **默认值**: `false`
- **作用**: 启用基于 WebViewAssetLoader 的 HTTPS 资源加载方案

#### 何时使用

- 需要使用 Plyr 插件播放 YouTube 视频
- 需要突破 `file://` 协议限制的特殊场景
- 需要与第三方服务集成,且该服务要求 HTTPS 协议

### 注意事项

1. **配置时机**: 必须在创建 FastboardView 或 Fastboard 实例之前完成配置
2. **全局生效**: 此配置对所有 WhiteboardView 实例全局生效
3. **兼容性**: 此功能需要 Android WebView 支持 WebViewAssetLoader (Android 5.0+)

## 验证配置是否生效

配置完成后,可以通过以下方式验证:

1. 在 Fastboard 中插入 Plyr 插件
2. 尝试播放 YouTube 视频
3. 如果视频能够正常加载和播放,说明配置已生效

## 技术支持

如果在配置过程中遇到问题,请检查:

- 是否在正确的时机调用了配置代码
- 是否使用了最低要求版本的 Fastboard SDK (1.8.0)

如有其他问题,请联系技术支持团队。