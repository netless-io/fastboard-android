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
    implementation "com.github.netless-io:fastboard-android:1.1.0"
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
### Style

```java
// code placeholder
```

### RoomPhaseHandler
```java
// code placeholder
```

### ErrorHandler
```java
// code placeholder
```

### Customize UI

```java
// code placeholder
```
