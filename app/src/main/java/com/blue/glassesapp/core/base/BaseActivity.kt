package com.blue.glassesapp.core.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.NetworkUtils
import com.bumptech.glide.Glide
import kotlin.apply
import kotlin.let

/**
 * @Description TODO
 *
 *
 * @Author liux
 * @Date 2022/11/11 15:27
 * @Version 1.0
 */
open abstract class BaseActivity<T : ViewBinding>(val contentLayoutId: Int) :
    AppCompatActivity(contentLayoutId) {
    lateinit var binding: T
    var isFirstOpen = true
    lateinit var mContext: Context
    open var needFullScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (needFullScreen) {
            setFullScreen()
        }
        mContext = getContext()
        binding = DataBindingUtil.setContentView(this, contentLayoutId)
        onCreate()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (needFullScreen) {
            setFullScreen()
        }
    }

    protected fun setFullScreen() {
        window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        //占满全屏，activity绘制将状态栏也加入绘制范围。
        //如此即使使用BEHAVIOR_SHOW_BARS_BY_SWIPE或BEHAVIOR_SHOW_BARS_BY_TOUCH
        //也不会因为状态栏的显示而导致activity的绘制区域出现变形
        //使用刘海屏也需要这一句进行全屏处理
        WindowCompat.setDecorFitsSystemWindows(window, false)
        //隐藏状态栏和导航栏 以及交互
        WindowInsetsControllerCompat(window, window.decorView).let {
            //隐藏状态栏和导航栏
            //用于WindowInsetsCompat.Type.systemBars()隐藏两个系统栏
            //用于WindowInsetsCompat.Type.statusBars()仅隐藏状态栏
            //用于WindowInsetsCompat.Type.navigationBars()仅隐藏导航栏
            it.hide(WindowInsetsCompat.Type.systemBars())
            //交互效果
            //BEHAVIOR_SHOW_BARS_BY_SWIPE 下拉状态栏操作可能会导致activity画面变形
            //BEHAVIOR_SHOW_BARS_BY_TOUCH 未测试到与BEHAVIOR_SHOW_BARS_BY_SWIPE的明显差异
            //BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE 下拉或上拉的屏幕交互操作会显示状态栏和导航栏
            it.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        // Configure the behavior of the hidden system bars.
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        var lastInsets: WindowInsetsCompat? = null
        // 设置监听器以在系统窗口插入更改时调整视图的大小。
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, windowInsets ->
            // You can hide the caption bar even when the other system bars are visible.
            // To account for this, explicitly check the visibility of navigationBars()
            // and statusBars() rather than checking the visibility of systemBars().
            if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars()) || windowInsets.isVisible(
                    WindowInsetsCompat.Type.statusBars()
                )
            ) {
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            } else {
                // Show both the status bar and the navigation bar.
                windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
            }
            ViewCompat.onApplyWindowInsets(view, windowInsets)
        }
    }

    protected abstract fun onCreate()

    fun getContext(): Context {
        return this
    }

    override fun onBackPressed() {
        if (canBack()) {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        Glide.with(this).resumeRequests()
        super.onResume()
    }

    override fun onStart() {
        Glide.with(this).resumeRequests()
        super.onStart()

    }

    override fun onPause() {
        super.onPause()
        Glide.with(this).pauseRequests()
    }

    override fun onDestroy() {
        super.onDestroy()
        Glide.get(this).clearMemory()
    }


    /**
     * 是否可以返回
     * 用于处理退出的一些判断提醒
     */
    open fun canBack(): Boolean {
        return true
    }

    /**
     * 打开页面
     */
    open fun openPage(cls: Class<*>, flag: Int? = null) {
        startActivity(
            Intent(
                this, cls
            ).apply {
                flag?.let {
                    flags = flag
                }
            })
    }

}