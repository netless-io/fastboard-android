package io.agora.board.fast.sample.cases.base

import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

open class BaseActivity : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    /**
     * It's a test include all operation, reorganize in production environment
     */
    protected fun setupFullScreen() {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.navigationBars())
        controller.hide(WindowInsetsCompat.Type.statusBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    protected fun setupWindowInsets() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(this.window.decorView) { view, insets ->
            insets
        }

        // ViewCompat.setOnApplyWindowInsetsListener(this.window.decorView) { view, insets ->
        //     val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
        //     val navInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
        //     view.setPadding(0, 0, 0, imeInsets.bottom)
        //     insets
        // }
    }
}
