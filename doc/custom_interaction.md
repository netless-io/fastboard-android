# 自定义交互机制（JavaScript ↔ Android Native）

本文档介绍如何在 Fastboard 中注册自定义 JavaScript Bridge，实现 Android 原生代码与白板内 JavaScript
代码的双向通信。

## 概述

通过 `WhiteboardView.addJavascriptObject()` 方法，你可以将 Android 对象注册到白板的 JavaScript 环境中，使
JavaScript 代码能够调用 Android 原生方法。

## 创建 JS Bridge 类

创建一个 Kotlin/Java 类，使用 `@JavascriptInterface` 注解标记需要暴露给 JavaScript 的方法：

```kotlin
class HiOneJSBridge {

    @JavascriptInterface
    fun getToken(args: Any, handler: CompletionHandler<String>) {
        try {
            val jsonArgs = when (args) {
                is JSONObject -> args
                else -> JSONObject(args.toString())
            }
            val uid = jsonArgs.optString("uid")

            val token = fetchTokenFromServer(uid)

            handler.complete(JSONObject().apply {
                put("token", token)
            }.toString())
        } catch (e: Exception) {
            handler.complete(JSONObject().apply {
                put("error", e.message ?: "Unknown error")
            }.toString())
        }
    }
}
```

### 关键要点

- **@JavascriptInterface**：必须添加，否则方法无法从 JavaScript 调用
- **CompletionHandler**：标记为异步方法，适用于需要返回值的调用
- **参数类型约定**：`args` 类型由 JS 和 Native 约定
  - 可以是基础类型（String、Number、Boolean）
  - 可以是可序列化对象（如 JSONObject）
  - 示例中使用 JSONObject
- **返回类型约定**：返回类型也由 JS 和 Native 约定
  - 建议返回 JSON 字符串，便于 JavaScript 解析
  - 也可以返回基础类型

## 注册 JS Bridge

在 FastRoom 创建完成后，通过 `WhiteboardView` 注册 Bridge：

```kotlin
// 在 HiOneActiviyt 中
private fun setupFastboard() {
    val fastboardView = findViewById<FastboardView>(R.id.fastboard_view)
    val fastboard = fastboardView.getFastboard()
  
    // 注册自定义 JS Bridge, "hione" 为命名空间，与 JS 端调用时使用的名称对应
    val whiteboardView = fastRoom!!.fastboardView.whiteboardView
    whiteboardView.addJavascriptObject(HiOneJSBridge(), "hione")
}
```

## JavaScript 调用方式

### 异步方法调用

对于带 `CompletionHandler` 参数的异步方法，使用 `window.bridge.asyncCall()`：

```javascript
async function getToken() {
  try {
    const response = await window.bridge.asyncCall(
      "hione.getToken",      // 格式：命名空间.方法名
      { uid: "user123" }     // 参数: 对象
    );

    const result = typeof response === "string" ? JSON.parse(response) : response;

    if (!result.token) {
      throw new Error(result.error || "token 为空");
    }

    console.log("获取到 token:", result.token);
    return result.token;
  } catch (err) {
    console.error("获取 token 失败:", err);
    throw err;
  }
}

getToken();
```

### 事件通知（单向调用，JS → Native）

对于事件通知类调用（如用户行为上报、埋点、状态同步等），
应使用单向 fire-and-forget 调用模型。

- Native 不返回结果
- JavaScript 不等待响应
- 不使用 CompletionHandler
- 不使用 asyncCall

此类方法用于表达“发生了一件事”，而不是“请求一个结果”。

```kotlin
@JavascriptInterface
fun trackUserEvent(args: Any) {
    try {
        val jsonArgs = when (args) {
            is JSONObject -> args
            else -> JSONObject(args.toString())
        }
        val name = jsonArgs.optString("name")
        val params = jsonArgs.optJSONObject("params")

        Log.d("JSBridge", "Event: $name, params: $params")
    } catch (e: Exception) {
        Log.e("JSBridge", "trackUserEvent error", e)
    }
}
```

```javascript
function trackUserEvent(name, params) {
  window.bridge.call("hione.trackUserEvent", { name, params });
}

trackUserEvent("button_click", { buttonId: "save" });
```

## 完整示例

### Android 端

完整代码请参考：
- Bridge 实现：[HiOneJSBridge.kt](../app/src/main/java/io/agora/board/fast/sample/cases/hione/HiOneJSBridge.kt)
- 注册示例：[HiOneActivity.kt](../app/src/main/java/io/agora/board/fast/sample/cases/hione/HiOneActivity.kt)

### JavaScript 端

```javascript
// 异步调用：获取 Token
async function getToken() {
  try {
    const response = await window.bridge.asyncCall("hione.getToken", {
      uid: "user123",
    });

    const result =
      typeof response === "string" ? JSON.parse(response) : response;

    if (result.token) {
      console.log("Token:", result.token);
      return result.token;
    } else {
      throw new Error(result.error);
    }
  } catch (err) {
    console.error("获取 token 失败:", err);
    throw err;
  }
}

// 单向通知：上报事件
function trackUserEvent(name, params) {
  window.bridge.call("hione.trackUserEvent", { name, params });
}

// 使用示例
getToken();
trackUserEvent("button_click", { buttonId: "save" });
```

## 注意事项

### 1. 参数和返回类型约定

**参数类型（args）**
- 类型由 JS 和 Native 双方约定
- 可以是基础类型：String、Number、Boolean
- 可以是可序列化对象：JSONObject、JSONArray
- 示例中使用 JSONObject 传递参数

**返回类型**
- 返回类型也由双方约定
- 推荐返回 JSON 字符串，便于解析
- 也可以返回基础类型

```kotlin
// JSONObject 示例
@JavascriptInterface
fun method(args: Any, handler: CompletionHandler<String>) {
    val jsonArgs = when (args) {
        is JSONObject -> args
        else -> JSONObject(args.toString())
    }
    val value = jsonArgs.optString("key")
    
    handler.complete(JSONObject().apply {
        put("result", value)
    }.toString())
}

// 基础类型示例
@JavascriptInterface
fun getCount(args: Any, handler: CompletionHandler<String>) {
    handler.complete("42")
}
```

### 2. 错误处理

始终添加 try-catch，返回友好的错误信息：

```kotlin
@JavascriptInterface
fun method(args: Any, handler: CompletionHandler<String>) {
    try {
        // 业务逻辑
    } catch (e: Exception) {
        handler.complete(JSONObject().apply {
            put("error", e.message ?: "Unknown error")
        }.toString())
    }
}
```

### 3. 命名空间

使用有意义的命名空间，避免冲突：

```kotlin
// ✅ 推荐
whiteboardView.addJavascriptObject(MyBridge(), "myapp")

// ❌ 不推荐
whiteboardView.addJavascriptObject(MyBridge(), "bridge")
```

### 4. 调用方式

- **异步调用**（需要返回值）：使用 `CompletionHandler` + `window.bridge.asyncCall()`
- **单向通知**（不需要返回值）：不使用 `CompletionHandler` + `window.bridge.call()`