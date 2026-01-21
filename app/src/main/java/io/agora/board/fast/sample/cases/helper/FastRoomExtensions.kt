package io.agora.board.fast.sample.cases.helper

import com.herewhite.sdk.Room
import com.herewhite.sdk.domain.Promise
import com.herewhite.sdk.domain.SDKError
import com.herewhite.sdk.domain.WindowAppSyncAttrs
import io.agora.board.fast.FastException
import io.agora.board.fast.FastRoom
import io.agora.board.fast.extension.FastResult
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Awaits a Promise and returns the result.
 * Throws [FastException] if the promise fails.
 */
suspend fun <T> awaitPromise(block: (Promise<T>) -> Unit): T =
    suspendCancellableCoroutine { cont ->
        block(object : Promise<T> {
            override fun then(t: T) {
                cont.resume(t)
            }

            override fun catchEx(t: SDKError) {
                cont.resumeWithException(t)
            }
        })
    }

suspend fun <T> awaitFastResult(block: (FastResult<T>) -> Unit): T =
    suspendCancellableCoroutine { cont ->
        block(object : FastResult<T> {
            override fun onSuccess(t: T) {
                cont.resume(t)
            }
            override fun onError(exception: Exception) {
                cont.resumeWithException(exception)
            }
        })
    }

/**
 * Closes an app by its appId.
 * @param appId The ID of the app to close.
 * @return true if the app was closed successfully.
 */
suspend fun FastRoom.closeApp(appId: String): Boolean {
    return awaitPromise {
        room?.closeApp(appId, it)
    }
}

/**
 * Queries all currently open apps.
 * @return A map of appId to [WindowAppSyncAttrs].
 */
suspend fun FastRoom.queryAllApps(): Map<String, WindowAppSyncAttrs> {
    return awaitPromise {
        room?.queryAllApps(it)
    }
}

/**
 * Disconnects from the room asynchronously.
 * @return The result of the disconnect operation.
 */
suspend fun FastRoom.awaitDisconnect(): Any? {
    return awaitPromise {
        room?.disconnect(it)
    }
}

/**
 * Closes all currently open apps.
 * @return true if all apps were closed successfully.
 * @throws FastException if any app fails to close.
 */
suspend fun FastRoom.closeAllApps() {
    val apps = queryAllApps()

    for (appId in apps.keys) {
        closeApp(appId)
    }
}
