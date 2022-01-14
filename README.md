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
    implementation "com.github.netless-io:fastboard-android:1.0.0"
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
```java
private void setupFastboard() {
    // step 1
    FastboardView fastboardView = findViewById(R.id.fastboard_view);
    // step 2: get fastSdk
    FastSdkOptions fastSdkOptions = new FastSdkOptions(USER_APP_ID);
    FastSdk fastSdk = fastboardView.getFastSdk(fastSdkOptions);

    // step 3: join room
    FastRoomOptions roomOptions = new FastRoomOptions(
            roomUUID,
            roomToken,
            uid);
    fastSdk.joinRoom(roomOptions);
}
```

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
