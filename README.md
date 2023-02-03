# Fastboard Android
Fastboard is a quick access to the agora-whiteboard wrapper library </br>
For historical reasons, agora-whiteboard carries a lot of legacy baggage. It's contains a large number of apis, and some concepts are cost time to understand. </br>
Fastboard is aimed to reduce the number of apis and the cost of access. It is pinned on the following features </br>

* Low-cost
* Scenario-based
* Configurable

## Environment
### Requirements
- Android SDK Version >= 21
- Android Tools Build >= 4.1.0

### build.gradle Configuration
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
    implementation "com.github.netless-io:fastboard-android:1.3.4"
}
```

## Quick start
the Fastboard contains default UI and default exceptions, event handler. Ideally, new users will be able to access the whiteboard with limited configuration. 

example can be found in [GetStartActivity](app/src/main/java/io/agora/board/fast/sample/cases/GetStartActivity.java)

### Layout
```xml
<OuterLayout>
    <!-- ... -->
    <io.agora.board.fast.FastboardView
        android:id="@+id/fastboard_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</OuterLayout>
```
### Join Room
<details open>
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

<details>
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

## Configuration
### Dark Mode

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

### Custom Main Color

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

### Custom ToolBox

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
