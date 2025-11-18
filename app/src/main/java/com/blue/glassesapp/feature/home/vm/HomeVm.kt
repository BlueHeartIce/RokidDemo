package com.blue.glassesapp.feature.home.vm

import android.app.Application
import android.graphics.Bitmap
import android.util.Size
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.blue.glassesapp.common.enums.BusinessType
import com.blue.glassesapp.common.enums.InteractionDirection
import com.blue.glassesapp.common.model.GlassesLinkState
import com.blue.glassesapp.core.db.DBManager
import com.blue.glassesapp.core.db.entity.GlassesRecordModel
import com.blue.glassesapp.core.utils.AppInternalFileUtil
import com.blue.glassesapp.core.utils.AppTimeUtils
import com.blue.glassesapp.core.utils.CommonModel
import com.blue.glassesapp.core.utils.CxrUtil
import com.blue.glassesapp.feature.home.ui.adapter.RecordAdapter
import com.rokid.cxr.client.extend.CxrApi
import com.rokid.cxr.client.extend.callbacks.PhotoResultCallback
import com.rokid.cxr.client.extend.listeners.AiEventListener
import com.rokid.cxr.client.extend.listeners.AudioStreamListener
import com.rokid.cxr.client.utils.ValueUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * <pre>
 *
 * </pre>
 *
 * <p>创建人: zxh</p>
 * <p>日期: 2025/11/5</p>
 */
class HomeVm(val appContext: Application) : AndroidViewModel(appContext) {
    val TAG = this.javaClass.simpleName
    val glassesInfo = MutableLiveData(CommonModel.glassesInfo)
    var recordModelList = ArrayList<GlassesRecordModel>()
    var recordAdapter: RecordAdapter = RecordAdapter(recordModelList)

    // 当前业务类型
    var currentBusinessType: BusinessType = BusinessType.RECOGNIZE_PERSON_IDENTITY
    fun init() {
        if (CommonModel.deviceDeviceName.isNullOrEmpty()) {
            glassesInfo.value?.glassesLinkState = GlassesLinkState.UNPAIRED
        } else {
            glassesInfo.value?.glassesLinkState = GlassesLinkState.UNCONNECTED
        }
        initBusiness()
        queryData(1)
    }

    /**
     * 连接眼镜
     * 检查眼镜是否有已配对设备
     * 检查设备蓝牙是否已经连接
     */
    fun linkGlasses() {
        if (glassesInfo.value?.glassesLinkState == GlassesLinkState.UNCONNECTED || glassesInfo.value?.glassesLinkState == GlassesLinkState.CONNECT_FAILED) {
            CxrUtil.initGlassesLink(appContext.applicationContext) {
                if (it == GlassesLinkState.CONNECTED) {

                }
            }
        }
    }

    /**
     * 初始化业务场景
     */
    fun initBusiness() {
        when (currentBusinessType) {
            BusinessType.RECOGNIZE_PERSON_IDENTITY -> {
                initAiScene()
            }

            else -> {
                initAiScene()
            }
        }
    }

    /**
     * 初始化AI场景
     *
     * 此处模拟发起一次拍照场景
     */
    fun initAiScene() {
        CxrApi.getInstance().setAudioStreamListener(object : AudioStreamListener {
            override fun onStartAudioStream(p0: Int, p1: String?) {
                LogUtils.d("onStartAudioStream", p0, p1)
                takePhotoByte(cameraSize)
            }

            override fun onAudioStream(p0: ByteArray?, p1: Int, p2: Int) {
                LogUtils.d("onAudioStream", p1, p2)
            }
        })
        CxrApi.getInstance().setAiEventListener(object : AiEventListener {
            override fun onAiKeyDown() {
            }

            override fun onAiKeyUp() {
            }

            override fun onAiExit() {
            }
        })
    }


    private val result = PhotoResultCallback { status, photo ->

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
        CxrApi.getInstance().notifyAiStart()
        ImageUtils.bytes2Bitmap(photo).let {
            LogUtils.d(
                "takePhotoResult bitmap size=${it.width}, ${it.height}"
            )
            writeVerifyRecord(it)
            val record = GlassesRecordModel().apply {
                direction = InteractionDirection.TO_DEVICE.value
                businessType = BusinessType.RECOGNIZE_PERSON_IDENTITY.value
                timestamp = System.currentTimeMillis()
                this.businessType = currentBusinessType.value
                this.message = "模拟数据"
            }
            writeUpdateRecord(record)
        }
    }


    /**
     * 发送识别后内容到眼睛端
     */
    private fun sendTtsFeedback(feedbackText: String) {
//            val status = CxrApi.getInstance().send(0, feedbackText, false)
        val status = CxrApi.getInstance().sendGlobalMsgContent(0, feedbackText, false)
        if (status != ValueUtil.CxrStatus.REQUEST_SUCCEED) {
            LogUtils.e("TTS反馈发送失败，状态：$status")
        }
    }

    val cameraSize = Size(1280, 720)

    /**
     * AI拍照返回
     *
     * 因为眼镜端的相机是旋转90度的，返回的宽高是相反的
     * 如果你想要一个720*1280的图片，那么这里size的宽高应该为1280*720
     * quality 拍照质量，范围1-100
     */
    fun takePhotoByte(size: Size) {
        aiOpenCamera(size.width, size.height, 90).let {
            LogUtils.i(
                "SelfDesignViewActivity", "takePhotoByte aiOpenCamera: $it"
            )
        }
        takePhotoResult(size.width, size.height, 90, result).let {
            LogUtils.i(
                TAG, "takePhotoByte takePhotoResult: $it"
            )
        }
    }

    /**
     * 写入核验记录
     * 图片保存到本地
     */
    fun writeVerifyRecord(img: Bitmap) {
        // 保存图片名称
        TimeUtils.getNowString()
        val imagPath =
            AppInternalFileUtil.createRecordFile("record_${AppTimeUtils.getCurrentTime()}.png")
        ImageUtils.save(img, imagPath, Bitmap.CompressFormat.PNG)
        val recordModel = GlassesRecordModel().apply {
            direction = InteractionDirection.FROM_DEVICE.value
            businessType = BusinessType.RECOGNIZE_PERSON_IDENTITY.value
            contentImg = imagPath
            timestamp = System.currentTimeMillis()
        }
        writeUpdateRecord(recordModel)
    }

    /**
     * 保存更新记录，更新记录
     */
    fun writeUpdateRecord(recordModel: GlassesRecordModel) {
        MainScope().launch {
            DBManager.writeGlassesRequestRecord(recordModel)
            recordAdapter.addData(recordModel)
            recordViewCallBack?.onRecordViewCallBack(recordModel)
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
    fun takePhotoResult(
        width: Int,
        height: Int,
        quality: Int,
        result: PhotoResultCallback,
    ): ValueUtil.CxrStatus? {
        return CxrApi.getInstance().takeGlassPhoto(width, height, quality, result)
    }

    val pageSize = 20

    /**
     * 分页查询数据
     */
    fun queryData(pageNum: Int) {
        Single.just(pageNum).map {
            DBManager.queryGlassesRequestRecord(it, pageSize)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe { messageList ->
                recordAdapter.setDatas(messageList)
                recordViewCallBack?.onRecordViewCallBack(null)
            }.apply {

            }
    }

    var recordViewCallBack: RecordViewCallBack? = null

    interface RecordViewCallBack {
        fun onRecordViewCallBack(recordModel: GlassesRecordModel?) {

        }
    }

}