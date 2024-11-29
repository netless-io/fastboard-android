# 更新日志
## [Version 1.6.3] - 2024-11-29
- 修复: 修复 `FastUiSettings.setToolboxEdgeMargin` 在未指定 Toolbox Gravity 时无效的问题。
  `
## [Version 1.6.2] - 2024-06-18
- 新增: 新增 `Fastboard.preloadWhiteboardView` and `FastboardConfig.autoPreload` 用于手动控制白板预加载

## [Version 1.6.1] - 2024-04-29
- 修复: 修复 `FastRoom.updateIfTextAppliance` 偶现空指针异常

## [Version 1.6.0] - 2023-12-14
- 新增: 新增 `FastboardConfig` 用于 Fastboard 辅助配置。 
  - 支持预加载提升白板加载速度，配置项为 `FastboardConfig.enablePreload` 和 `FastboardConfig.preloadCount`。 
  - 使用 `Fastboard.setConfig(FastboardConfig config)` 配置。

## [Version 1.5.0] - 2023-11-07
- 更新: 更新 whiteboard-android 版本要求为 2.16.73。
- 更新：禁用房间加入成功后全量回调 `roomState`, 客户端需要通过 `Room.getRoomState()` 来获取完整的 `RoomState`。

## [Version 1.4.1] - 2023-08-30
- 新增: `FastAppliance.HAND` 用于移动视图。

## [1.4.0] - 2023-08-21
- 更新: 更改最低 whiteboard-android 版本要求为 2.16.47。
- 更新: `FastRoom.insertImage`, 在当前视图中心插入图片。
- 新增: `FastRoom.insertStaticDoc`, 插入静态文档。
- 新增: `FastRoom.insertPptx`, 插入动态 PPT 文档。
- 新增: `FastLogger.setLogger`, 支持设置自定义日志记录器。
- 新增: `FastAppliance.PENCIL_ERASER`, 用于部分擦除笔画。

## [1.3.4] - 2023-02-03
- **重大变更**: 移除 `FastResource.getBackground`, 替换为 `FastResource.getBackgroundColor`。
- 新增: `FastResource.getBoardBackgroundColor` 用于配置白板背景。默认值为 `FastResource.getBackgroundColor`

## [1.3.3] - 2022-11-21
- 修复: `FastUiSettings.hideRoomController` 在启动阶段不生效的问题。
- 修复: TextAppliance 初始化时无焦点的问题。
- 添加: `FastRoomOptions.userPayload` 用于启用用户光标并设置光标显示。

## [1.3.1] - 2022-11-16
- 更新: 调整用户界面、图标和默认边距。
- 新增: `FastUiSettings.setToolboxEdgeMargin` 更新工具箱边距。
- 添加: `FastRoom.setWritable()`,带回调。
- 添加: `FastRoom.isWritable()`。
- 新增: `FastRoomOptions.containerSizeRatio` 配置默认上下文视图比例。

## [1.2.3] - 2022-08-03
- 修复: `MemberState` 某些字段无法本地保存的问题。

## [1.2.2] - 2022-07-28
- 修复: 示例中 core:core-ktx 导致的最小编译解码器问题。

## [1.2.1] - 2022-06-13
- 修复: `FastInsertDocParams(String taskUUID, String taskToken, String fileType)` 参数顺序错误。
- 新增: `FastInsertDocParams.dynamicDoc` 和 `FastInsertDocParams.converterType` 支持 Projector 文档转换。
- **重大变更**: 将 `FastRoom.setStokeWidth` 更名为 `FastRoom.setStrokeWidth`。

## [1.2.0] - 2022-05-07
- 修复: 修复自定义的 `RoomControllerGroup` 获取资源时的 NullPointerException。
- 修复: 修复`FastRoom.setFastStyle` 只更新内部`RoomController`视图的问题。
- 新增: 添加 `FastUiSettings.setToolsXXX` 配置颜色和设备。**重要提示:** 这些配置应在加入房间前调用。
- 更新: 无需额外调用 `Fastboard.setWhiteboardRatio`,即可初始化窗口比例。
- **重大变更**: 将 `ResourceImpl` 替换为 `FastResource`。

## [1.1.0] - 2022-04-11
- 修复: 修复覆盖隐藏时的 `ToolBox` 选定状态。
- 新增: 添加 `FastRoomListener`.`onRoomPhaseChanged`。
- 新增: 增加 `FastUiSettings.showRoomController`。
- **重大变更**: 更新 `FastUiSettings.hideRoomController`,用 `ControllerId` 替换 resId。

## [1.0.0] - 2022-03-20
- 更新: 将 `FastRoom` 视为主应用程序类。

## [1.0.0-beta.5] - 2022-03-14
- 更新: 更新 `FastRoomOptions.fastRegion` 为必填项。
- 更新: 移除 `FastInsertDocParams.resource`。

## [1.0.0-beta.4] - 2022-03-11
- 更新: 更新示例。
- 更新: 使用 ErrorHandleLayout 更新默认 ErrorHandle。
- 更新: 用 Fastboard 取代 FastSdk。
- 更新: 将所有播放器重新定义为重播。
- 更新: 合并 FastListener RoomState apis。

## [1.0.0-beta.3] - 2022-02-16
- 新增: 添加 FastboardView.setWhiteboardRatio。
- 新增: 添加配置资源模板(ResourceImpl)。

## [1.0.0-beta.2] - 2022-01-25
- 新增: 添加 Listener.onWindowBoxStateChanged。
- 新增: 添加 UiSettings.setRedoUndoOrientation。
- 更新: 更新示例。
- 更新: 移除 RoomControllerGroup.setupView。
- 更新: 移除 FastSdk.setOverlayManager。

## [1.0.0-beta.1] - 2022-01-14
- 新增: Fastboard 的基本概念。
- 新增: 一些示例。