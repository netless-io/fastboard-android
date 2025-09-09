## 🛠️ 实现沉浸式模式并固定根布局高度

在 Android 应用中，沉浸式模式可以隐藏状态栏和导航栏，为用户提供沉浸式全屏体验，适合视频播放、游戏、图像浏览和电子书阅读等场景。同时，合理处理窗口
Insets（系统栏、键盘等区域占用）可以避免根布局高度被意外改变，确保界面布局稳定。

### 1. 设置沉浸式模式（隐藏状态栏和导航栏）

沉浸式模式有两种主要实现方案，分别适用于不同 Android 版本和需求：

#### 方案 A：使用 `WindowInsetsControllerCompat`（推荐，Android 21+）

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupFullScreen()
    }

    private fun setupFullScreen() {
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        // 隐藏导航栏和状态栏
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
        // 设置系统栏行为，用户滑动时临时显示系统栏
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}
```

> **兼容性说明：**  
> `WindowInsetsControllerCompat` 是 androidx 提供的兼容方案，适用于 Android 10 (API 29) 及以上版本，且在
> AndroidX 库中可向下兼容到较低版本，但实际隐藏行为在低版本可能不完全一致。

#### 方案 B：使用 `View.setSystemUiVisibility()`（适用于 Android 5.0~9.0）

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupFullScreenLegacy()
    }

    private fun setupFullScreenLegacy() {
        if (Build.VERSION.SDK_INT in 21..28) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }
}
```

> **兼容性说明：**  
> 该方案适用于 Android 5.0 (API 21) 至 Android 9.0 (API 28)，使用旧版系统 UI
> 标志隐藏系统栏。部分设备和定制系统可能表现略有差异。

### 2. 配置 `windowSoftInputMode` 以避免键盘弹出时布局异常

在 `AndroidManifest.xml` 中对应 Activity 添加如下属性：

```xml

<activity android:name=".MainActivity" android:windowSoftInputMode="stateHidden|adjustNothing" />
```

- `stateHidden`：启动时隐藏软键盘
- `adjustNothing`：避免系统自动调整布局大小，防止键盘弹出时根布局高度变化

### 3. 防止根布局高度被键盘或导航栏影响

当启用沉浸式模式时，系统窗口的 Insets 可能会导致根布局高度变化，尤其是键盘弹出时

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 禁用系统自动调整布局大小，内容可绘制到系统栏区域
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 这里不修改 padding，保持根布局高度不变
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, insets ->
            insets
        }

        // 可选：如果需要动态调整内容区域，可以在这里处理 Insets (不建议)
        // ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, insets ->
        //     val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
        //     val navInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
        //     view.setPadding(0, 0, 0, imeInsets.bottom.coerceAtLeast(navInsets.bottom))
        //     insets
        // }
    }
}
```

### 4. 关于平板（PAD）与手机的差异

- **导航栏位置不同**：手机通常位于屏幕底部，平板可能在屏幕边缘（左/右侧），导致 Insets 计算不同。
- **系统栏尺寸和行为差异**：平板的状态栏和导航栏尺寸可能更大，且部分平板支持多窗口或分屏时 Insets
  变化更复杂。
- **兼容性处理**：建议使用 `WindowInsetsCompat` 统一处理系统栏和键盘 Insets，避免设备差异带来的布局问题。

---

## 📚 官方文档及参考链接

- [WindowInsets | Android Developers](https://developer.android.com/reference/android/view/WindowInsets)  
  详细介绍窗口Insets的API和用法。

- [Hide system bars for immersive mode | Android Developers](https://developer.android.com/develop/ui/views/layout/immersive)  
  官方沉浸式模式实现指南。

- [About window insets | Jetpack Compose](https://developer.android.com/develop/ui/compose/system/insets)  
  介绍Jetpack Compose中窗口Insets的处理方式。

- [WindowInsetsControllerCompat | AndroidX](https://developer.android.com/reference/androidx/core/view/WindowInsetsControllerCompat)  
  AndroidX中兼容窗口Insets控制的类。