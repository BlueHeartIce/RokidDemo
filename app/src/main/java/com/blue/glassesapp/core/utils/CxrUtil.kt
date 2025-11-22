package com.blue.glassesapp.core.utils

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Context
import androidx.annotation.RequiresPermission
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blue.armobile.R
import com.blue.glassesapp.common.model.BluetoothLinkState
import com.blue.glassesapp.common.model.GlassesLinkState
import com.rokid.cxr.client.extend.CxrApi
import com.rokid.cxr.client.extend.callbacks.BluetoothStatusCallback
import com.rokid.cxr.client.extend.listeners.CustomViewListener
import com.rokid.cxr.client.utils.ValueUtil
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object CxrUtil {
    const val TAG = "CxrUtil"
    fun initLocalDeviceInfo() {
        CommonModel.deviceDeviceName = SPUtils.getInstance().getString(ConsModel.SP_KEY_DEVICE_NAME)
        CommonModel.deviceMacAddress = SPUtils.getInstance().getString(ConsModel.SP_KEY_MAC_ADDRESS)
        CommonModel.deviceSocketUuid = SPUtils.getInstance().getString(ConsModel.SP_KEY_SOCKET_UUID)
    }


    interface BluetoothDeviceConnectListener {
        fun onBluetoothConnect(state: BluetoothLinkState, msg: String = "")
        fun onDeviceConnected(state: GlassesLinkState, msg: String = "")
    }


    /**
     * Init Bluetooth
     *
     * @param context   Application Context
     * @param device     Bluetooth Device
     */
    fun initConnectDevice(
        context: Context,
        device: BluetoothDevice,
        listener: BluetoothDeviceConnectListener,
    ) {
        CxrApi.getInstance().initBluetooth(context, device, object : BluetoothStatusCallback {
            /**
             * Connection Info
             *
             * @param socketUuid   Socket UUID
             * @param macAddress   Classic Bluetooth MAC Address
             * @param rokidAccount Rokid Account
             * @param glassesType  Device Type, 0-no display, 1-have display
             */
            @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
            override fun onConnectionInfo(
                socketUuid: String?,
                macAddress: String?,
                rokidAccount: String?,
                glassesType: Int,
            ) {
                LogUtils.i(
                    TAG,
                    "onConnectionInfo: socketUuid=$socketUuid, macAddress=$macAddress, rokidAccount=$rokidAccount, glassesType=$glassesType"
                )
                socketUuid?.let { uuid ->
                    macAddress?.let { address ->
                        connectGlasses(context, device.name ?: "Unknown", uuid, address) {
                            if (it == GlassesLinkState.CONNECTED) {
                                CommonModel.deviceSocketUuid = uuid
                                CommonModel.deviceMacAddress = macAddress
                                CommonModel.deviceDeviceName = device.name ?: "Unknown"

                                SPUtils.getInstance()
                                    .put(ConsModel.SP_KEY_SOCKET_UUID, CommonModel.deviceSocketUuid)
                                SPUtils.getInstance()
                                    .put(ConsModel.SP_KEY_MAC_ADDRESS, CommonModel.deviceMacAddress)
                                SPUtils.getInstance()
                                    .put(ConsModel.SP_KEY_DEVICE_NAME, CommonModel.deviceDeviceName)
                            }
                            listener.onDeviceConnected(it, CommonModel.glassesLinkErrorMsg)
                        }
                    } ?: run {
                        LogUtils.e(TAG, "macAddress is null")
                    }
                } ?: run {
                    LogUtils.e(TAG, "socketUuid is null")
                }
                listener.onBluetoothConnect(BluetoothLinkState.ON_CONNECTION_INFO)
            }

            /**
             * Connected
             */
            override fun onConnected() {
                LogUtils.i(TAG, "onConnected")
                listener.onBluetoothConnect(BluetoothLinkState.CONNECTED)
            }

            /**
             * Disconnected
             */
            override fun onDisconnected() {
                LogUtils.i(TAG, "onDisconnected")
                listener.onBluetoothConnect(BluetoothLinkState.DISCONNECTED)
            }

            /**
             * Failed
             *
             * @param errorCode   Error Code:
             * @see ValueUtil.CxrBluetoothErrorCode
             * @see ValueUtil.CxrBluetoothErrorCode.PARAM_INVALID  Parameter Invalid
             * @see ValueUtil.CxrBluetoothErrorCode.BLE_CONNECT_FAILED BLE Connect Failed
             * @see ValueUtil.CxrBluetoothErrorCode.SOCKET_CONNECT_FAILED Socket Connect Failed
             * @see ValueUtil.CxrBluetoothErrorCode.UNKNOWN Unknown
             */
            override fun onFailed(p0: ValueUtil.CxrBluetoothErrorCode?) {
                LogUtils.e(TAG, "onFailed: $p0")
                val errorMsg = when (p0) {
                    ValueUtil.CxrBluetoothErrorCode.PARAM_INVALID -> "参数错误"
                    ValueUtil.CxrBluetoothErrorCode.BLE_CONNECT_FAILED -> "BLE连接失败"
                    ValueUtil.CxrBluetoothErrorCode.SOCKET_CONNECT_FAILED -> "Socket连接失败"
                    ValueUtil.CxrBluetoothErrorCode.UNKNOWN -> "未知错误"
                    else -> "未知错误"
                }
                listener.onBluetoothConnect(BluetoothLinkState.FAILED, errorMsg)
            }

        })
        CxrApi.getInstance().isBluetoothConnected.let {
            LogUtils.i(TAG, "isBluetoothConnected: $it")
        }
    }

    /**
     * open custom view
     *
     * @param content json format view content
     *
     * @return open request status
     * @see ValueUtil.CxrStatus
     * @see ValueUtil.CxrStatus.REQUEST_SUCCEED request succeed
     * @see ValueUtil.CxrStatus.REQUEST_WAITING request waiting, do not request again
     * @see ValueUtil.CxrStatus.REQUEST_FAILED request failed
     */
    fun openCustomView(content: String): ValueUtil.CxrStatus {
        return CxrApi.getInstance().openCustomView(content)
    }

    /**
     * set custom view listener (true: set listener, false: remove listener)
     */
    fun setCustomViewListener(set: Boolean) {
        CxrApi.getInstance().setCustomViewListener(if (set) customViewListener else null)
    }

    // Custom View Listener
    private val customViewListener = object : CustomViewListener {
        /**
         * custom view icons sent
         */
        override fun onIconsSent() {
            LogUtils.i("CustomViewListener", "onIconsSent: ")
        }

        /**
         * custom view opened
         */
        override fun onOpened() {
            LogUtils.i("CustomViewListener", "onOpened: ")
        }

        /**
         * custom view closed
         */
        override fun onOpenFailed(p0: Int) {
            LogUtils.i("CustomViewListener", "onOpenFailed: $p0")
        }

        /**
         * custom view updated
         */
        override fun onUpdated() {
            LogUtils.i("CustomViewListener", "onUpdated: ")
        }

        /**
         * custom view closed
         */
        override fun onClosed() {
            LogUtils.i("CustomViewListener", "onClosed: ")
        }
    }


    /**
     * close custom view
     * @return close request status
     * @see ValueUtil.CxrStatus
     * @see ValueUtil.CxrStatus.REQUEST_SUCCEED request succeed
     * @see ValueUtil.CxrStatus.REQUEST_WAITING request waiting, do not request again
     * @see ValueUtil.CxrStatus.REQUEST_FAILED request failed
     */
    fun closeCustomView(): ValueUtil.CxrStatus {
        return CxrApi.getInstance().closeCustomView()
    }

    /**
     * update custom view
     * @param content: custom view content that need update
     * @return: update request status
     * @see ValueUtil.CxrStatus
     * @see ValueUtil.CxrStatus.REQUEST_SUCCEED request succeed
     * @see ValueUtil.CxrStatus.REQUEST_WAITING request waiting, do not request again
     * @see ValueUtil.CxrStatus.REQUEST_FAILED request failed
     */
    fun updateCustomView(content: String): ValueUtil.CxrStatus {
        return CxrApi.getInstance().updateCustomView(content)
    }

    /**
     *  Connect
     *
     *  @param context   Application Context
     *  @param socketUuid   Socket UUID
     *  @param macAddress   Classic Bluetooth MAC Address  snEncryptContent:" + var5 + ",clientSecret:" + var6
     */
    fun connectGlasses(
        context: Context,
        deviceName: String,
        socketUuid: String,
        macAddress: String,
        action: (GlassesLinkState) -> Unit,
    ) {
        LogUtils.i(TAG, "connectGlasses: socketUuid=$socketUuid, macAddress=$macAddress")
        CommonModel.glassesInfo.glassesLinkState = GlassesLinkState.CONNECTING
        action(GlassesLinkState.CONNECTING)
        CxrApi.getInstance()
            .connectBluetooth(
                context, socketUuid, macAddress, object : BluetoothStatusCallback {
                override fun onConnectionInfo(
                    socketUuid: String?,
                    macAddress: String?,
                    rokidAccount: String?,
                    glassesType: Int,
                ) {
                    LogUtils.i(
                        TAG,
                        "onConnectionInfo: socketUuid=$socketUuid, macAddress=$macAddress, deviceName=$deviceName, glassesType=$glassesType"
                    )
                    CommonModel.glassesInfo.glassesLinkState = GlassesLinkState.CONNECTION_INFO
                    action(GlassesLinkState.CONNECTION_INFO)
                }

                /**
                 * Connected
                 */
                override fun onConnected() {
                    LogUtils.d(TAG, "Connected")
                    CommonModel.glassesInfo.glassesLinkState = GlassesLinkState.CONNECTED
                    initGlassInfo()
                    action(GlassesLinkState.CONNECTED)
                }

                /**
                 * Disconnected
                 */
                override fun onDisconnected() {
                    LogUtils.d(TAG, "Disconnected")
                    CommonModel.glassesInfo.glassesLinkState = GlassesLinkState.UNCONNECTED
                    action(GlassesLinkState.UNCONNECTED)
                }

                /**
                 * Failed
                 *
                 * @param errorCode   Error Code:
                 * @see ValueUtil.CxrBluetoothErrorCode
                 * @see ValueUtil.CxrBluetoothErrorCode.PARAM_INVALID  Parameter Invalid
                 * @see ValueUtil.CxrBluetoothErrorCode.BLE_CONNECT_FAILED BLE Connect Failed
                 * @see ValueUtil.CxrBluetoothErrorCode.SOCKET_CONNECT_FAILED Socket Connect Failed
                 * @see ValueUtil.CxrBluetoothErrorCode.UNKNOWN Unknown
                 */
                override fun onFailed(p0: ValueUtil.CxrBluetoothErrorCode?) {
                    LogUtils.e(TAG, "Failed CxrBluetoothErrorCode->$p0")
                    CommonModel.glassesInfo.glassesLinkState = GlassesLinkState.CONNECT_FAILED
                    CommonModel.glassesLinkErrorMsg = when (p0) {
                        ValueUtil.CxrBluetoothErrorCode.PARAM_INVALID -> "参数错误"
                        ValueUtil.CxrBluetoothErrorCode.BLE_CONNECT_FAILED -> "蓝牙连接失败"
                        ValueUtil.CxrBluetoothErrorCode.SOCKET_CONNECT_FAILED -> "Socket连接失败"
                        ValueUtil.CxrBluetoothErrorCode.UNKNOWN -> "未知错误"
                        else -> "其他未知错误"
                    }
                    action(GlassesLinkState.CONNECT_FAILED)
                }
            }, readRawFile(context), CommonModel.CLIENT_SECRET)
        initGlassesListener()
    }

    fun initGlassesListener() {
        CxrApi.getInstance().setVolumeUpdateListener { volume ->
            CommonModel.glassesInfo.volume = volume
        }
        CxrApi.getInstance().setBrightnessUpdateListener { brightness ->
            CommonModel.glassesInfo.brightness = brightness
        }
        CxrApi.getInstance().setBatteryLevelUpdateListener { batteryLevel, charging ->
            CommonModel.glassesInfo.battery = batteryLevel
            CommonModel.glassesInfo.charging = charging
        }
    }

    /**
     *
     * @param status information Got Status
     * @see ValueUtil.CxrStatus
     * @see ValueUtil.CxrStatus.RESPONSE_SUCCEED response succeed
     * @see ValueUtil.CxrStatus.RESPONSE_INVALID response invalid
     * @see ValueUtil.CxrStatus.RESPONSE_TIMEOUT response timeout
     * @param glassesInfo glasses information
     */
    fun initGlassInfo() {
        CxrApi.getInstance().getGlassInfo { cxrStatus, glassInfo ->
            if (glassInfo != null && cxrStatus == ValueUtil.CxrStatus.RESPONSE_SUCCEED) {
                CommonModel.glassesInfo.apply {
                    name = glassInfo.deviceName
                    deviceId = glassInfo.deviceId
                    secret = glassInfo.deviceSecret
                    battery = glassInfo.batteryLevel
                    brightness = glassInfo.brightness
                    volume = glassInfo.volume
                }
            }
        }
    }


    /**
     * 初始化眼睛连接
     */
    @androidx.annotation.RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
    fun initGlassesLink(appContext: Context, action: (GlassesLinkState) -> Unit) {
        if (CommonModel.deviceMacAddress.isNotEmpty()) {
            connectGlasses(
                appContext,
                CommonModel.deviceDeviceName,
                CommonModel.deviceSocketUuid,
                CommonModel.deviceMacAddress,
                action
            )
            MainScope().launch{
                while (true) {
                    delay(5000)

//                    HidUitls.connect(CommonModel.deviceMacAddress)
                }
            }
        }
    }

    /**
     * 设置音量
     */
    /**
     * Set glasses volume
     *
     * @param volume volume value range[0-15]
     * @return set volume status
     * @see ValueUtil.CxrStatus
     * @see ValueUtil.CxrStatus.REQUEST_SUCCEED request succeed
     * @see ValueUtil.CxrStatus.REQUEST_WAITING request waiting, do not request again
     * @see ValueUtil.CxrStatus.REQUEST_FAILED request failed
     */
    fun setVolume(volume: Int): ValueUtil.CxrStatus? {
        return CxrApi.getInstance().setGlassVolume(volume)
    }


    /**
     * Set glasses brightness
     *
     * @param brightness brightness value range[0-15]
     * @return set brightness status
     * @see ValueUtil.CxrStatus
     * @see ValueUtil.CxrStatus.REQUEST_SUCCEED request succeed
     * @see ValueUtil.CxrStatus.REQUEST_WAITING request waiting, do not request again
     * @see ValueUtil.CxrStatus.REQUEST_FAILED request failed
     */
    fun setBrightness(brightness: Int): ValueUtil.CxrStatus? {
        return CxrApi.getInstance().setGlassBrightness(brightness)
    }

    /**
     * 读取raw目录下的.lc 文件
     */
    fun readRawFile(context: Context): ByteArray {
        val inputStream = context.resources.openRawResource(R.raw.a1e15aabfb1e4a88bbaf97e31121a84b)
        val bytes = inputStream.readBytes()
        return bytes
    }




}