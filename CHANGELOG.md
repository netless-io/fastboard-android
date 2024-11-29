# Change Log
## [Version 1.6.3] - 2024-11-29
- Fix: `FastUiSettings.setToolboxEdgeMargin` not working when Gravity is not specified.

## [Version 1.6.2] - 2024-06-18
- Add: `Fastboard.preloadWhiteboardView` and `FastboardConfig.autoPreload` for manual control of whiteboard preloading.

## [Version 1.6.1] - 2024-04-29
- Fix: Occasional NullPointerException in `FastRoom.updateIfTextAppliance`.

[Version 1.6.0] - 2023-12-14
- Add: `FastboardConfig` for Fastboard configuration.
  - Supports preloading to improve whiteboard loading speed. The configuration items are `FastboardConfig.enablePreload`
    and `FastboardConfig.preloadCount`.
  - Use `Fastboard.setConfig(FastboardConfig config)` to configure.

## [Version 1.5.0] - 2023-11-07
- Update: change minimum whiteboard-android to 2.16.73.
- Update: Disable complete roomState callback when room ready. The `Client` should now use room.getRoomState() to fetch the complete RoomState.

## [Version 1.4.1] - 2023-08-30
- Add: `FastAppliance.HAND` to move the view.

## [Version 1.4.0] - 2023-08-21
- Update: change minimum whiteboard-android to 2.16.47.
- Update: `FastRoom.insertImage` inserts image into the center of the current view.
- Add: `FastRoom.insertStaticDoc` to insert static docs.
- Add: `FastRoom.insertPptx` to insert projector converted docs.
- Add: `FastLogger.setLogger` to set custom logger.
- Add: `FastAppliance.PENCIL_ERASER` to partially erase strokes.

## [Version 1.3.4] - 2023-02-03
- **Breaking Change**: remove `FastResource.getBackground`, and replace it with `FastResource.getBackgroundColor`.
- Add: `FastResource.getBoardBackgroundColor` to configure whiteboard background, its default value is `FastResource.getBackgroundColor`.

## [Version 1.3.3] - 2022-11-21
- Fix: `FastUiSettings.hideRoomController` not working on startup stage.
- Fix: TextAppliance initial with no focus.
- Add: `FastRoomOptions.userPayload` to enable user cursor and set cursor display.

## [Version 1.3.1] - 2022-11-16
- Update: adjust UI, icons and default margins.
- Add: `FastUiSettings.setToolboxEdgeMargin` to update toolbox margin.
- Add: `FastRoom.setWritable()` with callback.
- Add: `FastRoom.isWritable()`.
- Add: `FastRoomOptions.containerSizeRatio` to config default context view ratio.

## [Version 1.2.3] - 2022-08-03
- Fix: `MemberState` some fields cannot be saved locally

## [Version 1.2.2] - 2022-07-28
- Fix: the minimum compileSdk problem caused by core:core-ktx in example.

## [Version 1.2.1] - 2022-06-13
- Fix: `FastInsertDocParams(String taskUUID, String taskToken, String fileType)` parameter sequence error.
- New: `FastInsertDocParams.dynamicDoc` and `FastInsertDocParams.converterType` to support Projector-Conversion docs.
- **Breaking Change**: Rename `FastRoom.setStokeWidth` to `FastRoom.setStrokeWidth`.

## [Version 1.2.0] - 2022-05-07
- Fix: fix get resources NullPointerException on customized `RoomControllerGroup`
- Fix: fix `FastRoom.setFastStyle` only update internal `RoomController` views
- New: add `FastUiSettings.setToolsXXX` to config colors and appliances. **Important Note** these
  config methods should be call before join room.
- Update: initialize the window scale without additional calls `Fastboard.setWhiteboardRatio`.
- **Breaking Change**: replace `ResourceImpl` with `FastResource`

## [Version 1.1.0] - 2022-04-11
- Fix: fix `ToolBox` selected state when overlay hide
- New: add `FastRoomListener`.`onRoomPhaseChanged`
- New: add `FastUiSettings.showRoomController`
- **Breaking Change**: update `FastUiSettings.hideRoomController`, replace resId with `ControllerId`

## [Version 1.0.0] - 2022-03-20
- Update: treat `FastRoom` as main apis class

## [Version 1.0.0-beta.5] - 2022-03-14
- Update: update `FastRoomOptions.fastRegion` required
- Update: remove `FastInsertDocParams.resource`

## [Version 1.0.0-beta.4] - 2022-03-11
- Update: update Samples
- Update: update default ErrorHandle with ErrorHandleLayout
- Update: replace FastSdk with Fastboard
- Update: redefine all Player to Replay
- Update: combine FastListener RoomState apis

## [Version 1.0.0-beta.3] - 2022-02-16
- New: add FastboardView.setWhiteboardRatio
- New: add ResourceImpl for configuration

## [Version 1.0.0-beta.2] - 2022-01-25
- New: add Listener.onWindowBoxStateChanged
- New: add UiSettings.setRedoUndoOrientation
- Update: update Samples
- Update: remove RoomControllerGroup.setupView
- Update: remove FastSdk.setOverlayManager

## [Version 1.0.0-beta.1] - 2022-01-14
- New: basic fastboard concepts
- New: some examples