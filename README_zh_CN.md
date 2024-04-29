# Fastboard Android

Fastboard 是一个快速接入 agora-whiteboard 包装库。</br>
历史原因，agora-whiteboard 承载了比较多的遗留包袱，api 数量繁多，相关概念有理解成本。</br>
Fastboard 旨在降低 api 数量，降低接入成本。其被寄予以下特点：</br>

* 轻接口的
* 场景化的
* 可配置的

## 环境配置
### 最低支持版本
- Android SDK Version >= 21
- Android Tools Build >= 4.1.0


### build.gradle 配置

```groovy
// project build
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}

// app build
android {
    // ...
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation "com.github.netless-io:fastboard-android:1.6.1"
}
```
## 快速接入

Fastboard 默认存在 UI，支持默认的异常，事件处理。理想情况下，新用户只需做有限的配置即可以顺利接入白板。
具体实例可参看 [GetStartActivity](app/src/main/java/io/agora/board/fast/sample/cases/GetStartActivity.java)

### 设置布局
```xml
<OuterLayout>
    <!-- ... -->
    <io.agora.board.fast.FastboardView
        android:id="@+id/fastboard_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</OuterLayout>
```
### 加入房间
<details>
    <summary>Java</summary>

```java
private void setupFastboard() {
    FastboardView fastboardView = findViewById(R.id.fastboard_view);
    Fastboard fastboard = fastboardView.getFastboard();

    FastRoomOptions roomOptions = new FastRoomOptions(
        USER_APP_ID,
        roomUUID,
        roomToken,
        uid,
        FastRegion.CN_HZ
    );
    
    FastRoom fastRoom = fastboard.createFastRoom(roomOptions);
    fastRoom.join();
}
```

</details>

<details open>
    <summary>Kotlin</summary>

```kotlin
private fun setupFastboard() {
    val fastboardView = findViewById<FastboardView>(R.id.fastboard_view)
    val fastboard = fastboardView.fastboard
    val roomOptions = FastRoomOptions(
        USER_APP_ID,
        roomUUID,
        roomToken,
        uid,
        FastRegion.CN_HZ
    )

    val fastRoom = fastboard.createFastRoom(roomOptions)
    fastRoom.join()
}
```

</details>

## 可配置
### 暗色模式设置

<details>
    <summary>Java</summary>

```java
private void updateDarkMode(boolean darkMode) {
    FastStyle fastStyle = fastRoom.getFastStyle();
    fastStyle.setDarkMode(darkMode);
    fastRoom.setFastStyle(fastStyle);
}
```

</details>

<details>
    <summary>Kotlin</summary>

```kotlin
private fun updateDarkMode(darkMode: Boolean) {
    val fastStyle: FastStyle = fastRoom.fastStyle.apply {
        isDarkMode = darkMode
    }
    fastRoom.fastStyle = fastStyle
}
```

</details>

### 主题色设置

<details>
    <summary>Java</summary>

```java
private void updateMainColor(@ColorInt int color) {
    FastStyle fastStyle = fastRoom.getFastStyle();
    fastStyle.setMainColor(color);
    fastRoom.setFastStyle(fastStyle);
}
```

</details>

<details>
    <summary>Kotlin</summary>

```kotlin
private fun updateMainColor(@ColorInt color: Int) {
    val fastRoom = FastRoom();
    val fastStyle: FastStyle = fastRoom.fastStyle.apply {
        mainColor = color
    }
    fastRoom.fastStyle = fastStyle
}
```

</details>

### Toolbox 设置

<details>
    <summary>Java</summary>

```java
public static void configToolBox() {
    // config ToolsExpandAppliances
    ArrayList<List<FastAppliance>> expandAppliances = new ArrayList<>();
    expandAppliances.add(Arrays.asList(FastAppliance.CLICKER));
    expandAppliances.add(Arrays.asList(FastAppliance.SELECTOR));
    expandAppliances.add(Arrays.asList(FastAppliance.PENCIL));
    // hide text appliance
    expandAppliances.add(Arrays.asList(FastAppliance.TEXT));
    expandAppliances.add(Arrays.asList(FastAppliance.ERASER));
    expandAppliances.add(Arrays.asList(
            FastAppliance.STRAIGHT,
            FastAppliance.ARROW,
            FastAppliance.RECTANGLE,
            FastAppliance.ELLIPSE,
            FastAppliance.PENTAGRAM,
            FastAppliance.RHOMBUS,
            FastAppliance.BUBBLE,
            FastAppliance.TRIANGLE
    ));
    // hide clear appliance
    // expandAppliances.add(Arrays.asList(FastAppliance.OTHER_CLEAR));
    FastUiSettings.setToolsExpandAppliances(expandAppliances);

    // config ToolsCollapseAppliances
    ArrayList<FastAppliance> collapseAppliances = new ArrayList<>();
    collapseAppliances.add(FastAppliance.PENCIL);
    collapseAppliances.add(FastAppliance.ERASER);
    collapseAppliances.add(FastAppliance.ARROW);
    collapseAppliances.add(FastAppliance.SELECTOR);
    collapseAppliances.add(FastAppliance.TEXT);
    collapseAppliances.add(FastAppliance.OTHER_CLEAR);
    FastUiSettings.setToolsCollapseAppliances(collapseAppliances);

    // config ToolsColors
    ArrayList<Integer> colors = new ArrayList<>();
    colors.add(Color.parseColor("#EC3455"));
    colors.add(Color.parseColor("#F5AD46"));
    colors.add(Color.parseColor("#68AB5D"));
    colors.add(Color.parseColor("#32C5FF"));
    colors.add(Color.parseColor("#005BF6"));
    colors.add(Color.parseColor("#6236FF"));
    colors.add(Color.parseColor("#9E51B6"));
    colors.add(Color.parseColor("#6D7278"));
    FastUiSettings.setToolsColors(colors);
}
```

</details>

<details>
    <summary>Kotlin</summary>

```kotlin
fun configToolBox(): Unit {
    // config ToolsExpandAppliances
    val expandAppliances = listOf(
        listOf(FastAppliance.CLICKER),
        listOf(FastAppliance.SELECTOR),
        listOf(FastAppliance.PENCIL),
        // hide text appliance
        // listOf(FastAppliance.TEXT),
        listOf(FastAppliance.ERASER),
        listOf(
            FastAppliance.STRAIGHT,
            FastAppliance.ARROW,
            FastAppliance.RECTANGLE,
            FastAppliance.ELLIPSE,
            FastAppliance.PENTAGRAM,
            FastAppliance.RHOMBUS,
            FastAppliance.BUBBLE,
            FastAppliance.TRIANGLE
        ),
        // hide clear appliance
        // listOf(FastAppliance.OTHER_CLEAR),
    )
    FastUiSettings.setToolsExpandAppliances(expandAppliances)

    // config ToolsCollapseAppliances
    val collapseAppliances = listOf(
        FastAppliance.PENCIL,
        FastAppliance.ERASER,
        FastAppliance.ARROW,
        FastAppliance.SELECTOR,
    )
    FastUiSettings.setToolsCollapseAppliances(collapseAppliances)

    // config ToolsColors
    val colors = listOf(
        Color.parseColor("#EC3455"),
        Color.parseColor("#F5AD46"),
        Color.parseColor("#68AB5D"),
        Color.parseColor("#32C5FF"),
        Color.parseColor("#005BF6"),
        Color.parseColor("#6236FF"),
        Color.parseColor("#9E51B6"),
        Color.parseColor("#6D7278"),
    )
    FastUiSettings.setToolsColors(colors)
}
```

</details>

## 场景化

### 你画我猜型

此场景的特点为：

* 多人协同绘制
* 低接口使用率
* 竖屏需求

```java
// code placeholder
```

### 公开课场景

此场景的特点为：

* 长时间单用户可写
* 众多只读用户
* 预览回放需求

```java
// code placeholder
```

### 通用应用型
此场景的特点为：

* 用户界面全支持，依赖适当可定制。
* 结合实时视频场景。
* 依赖一定程度状态同步。

```java
// code placeholder
```

## 术语

### 可写权限

白板`只读用户`，`可写用户`通过 WebSocket 连接到不同类型的服务代理节点。</br>
读写权限切换会更改连接节点，依据网络环境不同，可能存在不等的百毫秒级别的耗时。</br>
在低数量用户交互场景，建议使用 `disableInput**` 系列方法临时性禁止用户输入。

### 视窗大小

白板设计选择，赋予白板无限画布的属性，无限画布条件下，对白板的单X/Y缩放是不可行的。</br>
受限于等比缩放，不同设备，不同的显示比例的情况下，不同用户看到的视图必然是不同的。</br>
在特定的强窗口一致的场景下，需要保证以下两个条件：

1. 白板比例一致。
2. 白板缩放到合适显示区域。

对于条件1，Android 平台下实现通过更改 WhiteboardView 的 LayoutParams 限定比例。

对于条件2，各端通过 `moveCameraToContainer` 设置相同的 `RectangleConfig` 满足

此两条件会以特定的接口封装在 `FastRoom` 下，如用户有高度自定义需求情况下可参考。