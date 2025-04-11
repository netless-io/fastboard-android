# 定制 Whiteboard

Fastboard 基于 Whiteboard 进行二次开发，提供了更丰富的功能和更优的性能。由于底层仍依赖
Whiteboard，因此你可以通过 Whiteboard 的 API 来定制 Fastboard 的白板功能。

## RoomParams 参数配置

Fastboard 提供了简洁的参数配置接口，适用于大多数场景。对于需要更高定制性的用户，可以通过 RoomParams
进行高级配置。

以下示例展示如何通过 RoomParams.windowParams 自定义窗口样式：

```kotlin
// step config FastRoomOptions
fun createRoomOptions(intent: Intent): FastRoomOptions {
    val roomOptions = FastRoomOptions(
        Constants.SAMPLE_APP_ID,
        intent.getStringExtra(Constants.KEY_ROOM_UUID),
        intent.getStringExtra(Constants.KEY_ROOM_TOKEN),
        repository.userId,
        FastRegion.CN_HZ
    )

    val roomParams = roomOptions.roomParams.apply {
        windowParams.collectorStyles = hashMapOf(
            // "left" to "12px",
            // "top" to "120px",
            "right" to "12px",
            "bottom" to "120px",
            "position" to "fixed"
        )
    }
    // NOTE: roomParams 需要重新赋值给 roomOptions
    roomOptions.roomParams = roomParams
    return roomOptions
}

```

## Room API

Fastboard 提供 Room 接口，可用于实现白板交互行为的定制，例如控制文档翻页等操作。

以下示例展示如何结合手势滑动事件进行文档翻页：

```kotlin 
// 示例来至文件 io.agora.board.fast.sample.cases.hione.HiOneLayout
gestureView = root.findViewById(R.id.gesture_view)
gestureView.setGestureListener(object : HiOneGestureView.GestureListener {
    override fun onLeftSwipe() {
        // Right to Left Swipe
        fastRoom?.room?.dispatchDocsEvent(WindowDocsEvent.NextPage, null)
    }

    override fun onRightSwipe() {
        // Left to Right Swipe
        fastRoom?.room?.dispatchDocsEvent(WindowDocsEvent.PrevPage, null)
    }
})
```

## WhiteboardView

WhiteboardView 提供了对 WebView 行为的更底层控制，例如跨域访问等能力。

以下示例展示如何允许本地文件访问网络资源（启用 CORS）：

```kotlin
// step config WhiteboardView
fastboardView.whiteboardView.settings.allowUniversalAccessFromFileURLs = true
```