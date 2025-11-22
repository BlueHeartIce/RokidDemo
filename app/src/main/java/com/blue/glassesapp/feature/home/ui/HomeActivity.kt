package com.blue.glassesapp.feature.home.ui

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ServiceUtils
import com.blue.armobile.R
import com.blue.armobile.databinding.ActivityHomeBinding
import com.blue.glassesapp.core.base.BaseActivity
import com.blue.glassesapp.core.service.DeviceMonitorService
import com.blue.glassesapp.core.utils.CommonModel
import com.blue.glassesapp.feature.home.vm.HomeVm
import com.google.gson.Gson
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.arch.QMUIFragmentPagerAdapter
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.tab.QMUITab
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator
import com.qmuiteam.qmui.widget.tab.QMUITabSegment
import com.rokid.cxr.Caps
import com.rokid.cxr.client.extend.CxrApi
import com.rokid.cxr.client.extend.callbacks.PhotoPathCallback
import com.rokid.cxr.client.extend.callbacks.PhotoResultCallback
import com.rokid.cxr.client.extend.callbacks.SendStatusCallback
import com.rokid.cxr.client.utils.ValueUtil
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {
    val TAG: String = this::class.java.simpleName
    private val viewModel: HomeVm by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    override fun onCreate() {
        // 初始化 DataBinding
        viewModel.init()
        binding.initView()
        binding.initPagers()
        LogUtils.d(TAG, viewModel.toString())
        if (!ServiceUtils.isServiceRunning(DeviceMonitorService::class.java)) {
            ServiceUtils.startService(DeviceMonitorService::class.java)
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.linkGlasses()
    }

    override fun onPause() {
        super.onPause()
    }



    private fun ActivityHomeBinding.initPagers() {
        val pagerAdapter: QMUIFragmentPagerAdapter = object : QMUIFragmentPagerAdapter(
            supportFragmentManager
        ) {
            override fun createFragment(position: Int): QMUIFragment {
                return when (position) {
                    0 -> HomeFragment()
                    1 -> RecordFragment()
                    2 -> RemoteControlFragment()
                    else -> HomeFragment()
                }
            }

            override fun getCount(): Int {
                return 3
            }
        }
        pager.adapter = pagerAdapter

        val indicatorHeight = QMUIDisplayHelper.dp2px(mContext, 2)
        tabs.reset()
        tabs.setIndicator(
            QMUITabIndicator(
                indicatorHeight, false, true
            )
        )

        val tabBuilder: QMUITabBuilder = tabs.tabBuilder().setGravity(Gravity.CENTER)
        tabBuilder.setDynamicChangeIconColor(true)
        val mainTab: QMUITab = tabBuilder.setText("主页").setColorAttr(
            com.qmuiteam.qmuilibrary.R.attr.qmui_config_color_blue,
            com.qmuiteam.qmuilibrary.R.attr.qmui_config_color_red
        ).build(getContext())

        val historyTab: QMUITab = tabBuilder.setText("记录").build(getContext())
        val control: QMUITab = tabBuilder.setText("遥控器").build(getContext())
        tabs.addTab(mainTab)
        tabs.addTab(historyTab)
        tabs.addTab(control)

        tabs.setupWithViewPager(pager, false)
        tabs.mode = QMUITabSegment.MODE_FIXED
    }


    /**
     * 注册蓝牙广播接收
     */
    fun initRegisterReceiver() {
        IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        }
    }

    private fun ActivityHomeBinding.initView() {

    }


    // photo path callback
    private val photoPathResult = object : PhotoPathCallback {
        /**
         * photo path callback
         *
         * @param status photo path status
         * @see ValueUtil.CxrStatus
         * @see ValueUtil.CxrStatus.RESPONSE_SUCCEED response succeed
         * @see ValueUtil.CxrStatus.RESPONSE_INVALID response invalid
         * @see ValueUtil.CxrStatus.RESPONSE_TIMEOUT response timeout
         * @param path photo path
         */
        override fun onPhotoPath(
            status: ValueUtil.CxrStatus?,
            path: String?,
        ) {
            Log.i(
                "SelfDesignViewActivity", "takePhotoPath onPhotoPath: status=$status, path=$path"
            )
        }

    }

    // photo result callback
    private val result = object : PhotoResultCallback {
        /**
         * photo result callback
         *
         * @param status photo take status
         * @see ValueUtil.CxrStatus
         * @see ValueUtil.CxrStatus.RESPONSE_SUCCEED response succeed
         * @see ValueUtil.CxrStatus.RESPONSE_INVALID response invalid
         * @see ValueUtil.CxrStatus.RESPONSE_TIMEOUT response timeout
         * @param photo WebP photo data byte array
         */

        override fun onPhotoResult(
            status: ValueUtil.CxrStatus?,
            photo: ByteArray?,
        ) {
            LogUtils.i(
                "SelfDesignViewActivity",
                "takePhotoResult onPhotoResult: status=$status, photo size=${photo?.size}"
            )


            MainScope().launch {
                ImageUtils.bytes2Bitmap(photo).let {
                    LogUtils.d(
                        "SelfDesignViewActivity",
                        "takePhotoResult bitmap size=${it.width}, ${it.height}"
                    )
                    findViewById<ImageView>(R.id.img_glass).setImageBitmap(it)

                    if (CommonModel.useCustomView) {
                        showCustomView("模拟返回数据")
                    } else {
                        sendTtsFeedback("模拟返回数据")
                    }
                }
            }
        }


        /**
         * 发送TTS反馈到眼镜端（由眼镜播放语音）
         */
        private fun sendTtsFeedback(feedbackText: String) {
            LogUtils.d("发送TTS反馈：$feedbackText")
            val status =
                CxrApi.getInstance().sendGlobalMsgContent(0, feedbackText, CommonModel.useTTS)
            LogUtils.e("TTS反馈发送失败，状态：$status")
        }
    }


    /**
     * open ai camera
     *
     * @param width photo width
     * @param height photo height
     * @param quality photo quality range [0-100]
     *
     * @return open camera result
     * @see ValueUtil.CxrStatus
     * @see ValueUtil.CxrStatus.REQUEST_SUCCEED request succeed
     * @see ValueUtil.CxrStatus.REQUEST_WAITING request waiting, do not request again
     * @see ValueUtil.CxrStatus.REQUEST_FAILED request failed
     */
    fun aiOpenCamera(width: Int, height: Int, quality: Int): ValueUtil.CxrStatus? {
        return CxrApi.getInstance().openGlassCamera(width, height, quality)
    }

    /**
     * take photo
     *
     * @param width photo width
     * @param height photo height
     * @param quality photo quality range[0-100]
     *
     * @return take photo result
     * @see ValueUtil.CxrStatus
     * @see ValueUtil.CxrStatus.REQUEST_SUCCEED request succeed
     * @see ValueUtil.CxrStatus.REQUEST_WAITING request waiting, do not request again
     * @see ValueUtil.CxrStatus.REQUEST_FAILED request failed
     */
    fun takePhotoResult(width: Int, height: Int, quality: Int): ValueUtil.CxrStatus? {
        return CxrApi.getInstance().takeGlassPhoto(width, height, quality, result)
    }


    /**
     * take photo path
     *
     * @param width photo width
     * @param height photo height
     * @param quality photo quality
     *
     * @return take photo path result
     * @see ValueUtil.CxrStatus
     * @see ValueUtil.CxrStatus.REQUEST_SUCCEED request succeed
     * @see ValueUtil.CxrStatus.REQUEST_WAITING request waiting, do not request again
     * @see ValueUtil.CxrStatus.REQUEST_FAILED request failed
     */
    fun takePhotoPath(width: Int, height: Int, quality: Int): ValueUtil.CxrStatus? {
        return CxrApi.getInstance().takeGlassPhoto(width, height, quality, photoPathResult)
    }
//    fun takePhotoByte(width: Int, height: Int, quality: Int): ValueUtil.CxrStatus? {
////        return CxrApi.getInstance().takeGlassPhoto(width, height, quality, photoResultCallback)
//    }


    fun sendExampleMessage() {
//         val cXRServiceBridge = CXRServiceBridge()
        // 1. 创建 Caps 对象并填充数据
        val args = Caps()
        args.write("测试数据")  // 写入字符串消息
        args.writeUInt32(5)        // 写入一个无符号32位整数参数（示例值）

        // 2. 准备要发送的二进制数据（示例：空数据）
        val data = byteArrayOf()  // 实际场景中替换为需要发送的数据
        data.size      // 数据大小

        // 3. 调用 sendMessage 发送数据
//        val result = cXRServiceBridge.sendMessage(
//            "your_topic",  // 消息通道名称（根据实际协议定义）
//            args,               // 参数对象
//            data,               // 二进制数据
//            offset,           // 数据起始偏移量
//            size                // 数据长度
//        )
//        CxrController.getInstance()
//            .request(CxrApi.getInstance().t, "your_topic", args, object : RequestCallback {
//                override fun onResponse(p0: Int, p1: ValueUtil.CxrStatus?, p2: String?, p3: Caps?) {
//                    Log.i(
//                        "SelfDesignViewActivity",
//                        "request onResponse: p0=$p0, p1=$p1, p2=$p2, p3=$p3"
//                    )
//                }
//            })

//        // 4. 处理发送结果
//        if (result == 0) {
//            Log.d("send_message", "Send message success")
//        } else {
//            Log.d("send_message", "Send message Error: $result")
//        }
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    val streamCallback = object : SendStatusCallback {
        /**
         * send succeed
         */
        override fun onSendSucceed() {
            Log.i("SelfDesignViewActivity", "onSendSucceed")

        }

        /**
         * send failed
         * @param errorCode
         * @see ValueUtil.CxrSendErrorCode
         */
        override fun onSendFailed(errorCode: ValueUtil.CxrSendErrorCode?) {
            Log.i("SelfDesignViewActivity", "onSendFailed: $errorCode")
        }

    }

    /**
     * send stream
     * @param type
     * @see ValueUtil.CxrStreamType
     * @see ValueUtil.CxrStreamType.WORD_TIPS teleprompter words
     * @param stream
     * @param fileName
     * @return send status
     * @see ValueUtil.CxrStatus
     * @see ValueUtil.CxrStatus.REQUEST_SUCCEED request succeed
     * @see ValueUtil.CxrStatus.REQUEST_WAITING request waiting, do not request again
     * @see ValueUtil.CxrStatus.REQUEST_FAILED request failed
     */
    fun sendStream(
        type: ValueUtil.CxrStreamType,
        stream: ByteArray,
        fileName: String,
    ): ValueUtil.CxrStatus? {
        CxrApi.getInstance()
        return CxrApi.getInstance().sendStream(type, stream, fileName, streamCallback)
    }


    /**
     * 显示自定义view
     */
    fun showCustomView(text: String) {
        val customViewMap = mapOf(
            "type" to "LinearLayout", "props" to mapOf(
                "id" to "main",
                "layout_width" to "match_parent",
                "layout_height" to "match_parent",
                "orientation" to "vertical",
                "gravity" to "center_vertical",
                "paddingStart" to "12dp",
                "paddingEnd" to "12dp",
                "paddingTop" to "160dp",
                "paddingBottom" to "80dp",
                "backgroundColor" to "#FF000000"
            ), "children" to listOf(
                mapOf(
                    "type" to "TextView", "props" to mapOf(
                        "text" to text,
                        "textSize" to "16sp",
                        "textStyle" to "bold",
                        "textColor" to "#FFFFFFFF",
                        "marginEnd" to "8dp"
                    )
                )
            )
        )
        CxrApi.getInstance().openCustomView(Gson().toJson(customViewMap))
        // 五秒后关闭自定义界面
        MainScope().launch {
            delay(5000)
            CxrApi.getInstance().closeCustomView()
        }
    }

}