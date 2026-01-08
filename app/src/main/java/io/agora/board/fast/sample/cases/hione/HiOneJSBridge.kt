package io.agora.board.fast.sample.cases.hione

import android.util.Log
import android.webkit.JavascriptInterface
import org.json.JSONObject
import wendu.dsbridge.special.CompletionHandler

/**
 * HiOne JavaScript Bridge 示例
 * 演示如何创建自定义 JS Bridge 与白板进行通信
 */
class HiOneJSBridge {

    companion object {
        private const val TAG = "HiOneJSBridge"
    }

    /**
     * 异步方法示例：获取用户 Token
     *
     * @param args Bridge 自动包装的参数，包含 data 字段
     * @param handler 异步回调，通过 CompletionHandler 标记为异步方法
     */
    @JavascriptInterface
    fun getToken(args: Any, handler: CompletionHandler<String>) {
        try {
            // args 已经是 JSONObject，直接类型判断和使用
            val jsonArgs = when (args) {
                is JSONObject -> args
                else -> JSONObject(args.toString())
            }

            // 从 data 字段中提取实际参数
            val uid = jsonArgs.optString("uid")

            // TODO: 实现实际的业务逻辑
            val token = "mock_token_${uid}_${System.currentTimeMillis()}"

            // 返回结果
            val response = JSONObject().apply {
                put("token", token)
            }
            handler.complete(response.toString())
        } catch (e: Exception) {
            val error = JSONObject().apply {
                put("error", e.message ?: "Unknown error")
            }
            handler.complete(error.toString())
        }
    }

    /**
     * 事件通知示例：接收 JS 发送的用户事件（单向调用，不返回结果）
     *
     * @param args Bridge 自动包装的参数
     */
    @JavascriptInterface
    fun trackUserEvent(args: Any) {
        try {
            val jsonArgs = when (args) {
                is JSONObject -> args
                else -> JSONObject(args.toString())
            }

            val name = jsonArgs.optString("name")
            val params = jsonArgs.optJSONObject("params")

            // TODO: 处理事件（如埋点、日志、业务通知等）
            Log.d(TAG, "trackUserEvent: $name, params: $params")
        } catch (e: Exception) {
            Log.e(TAG, "trackUserEvent error", e)
        }
    }
}