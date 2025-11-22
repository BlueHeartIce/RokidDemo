package com.blue.glassesapp.core.utils.hid

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.blue.glassesapp.common.model.bindmodel.HidLinkModel
import com.blue.glassesapp.feature.home.model.REMOTE_INPUT_NONE

object BluetoothHidManager {
    private const val TAG = "BluetoothHidManager"

    /** ------------------------------
     * HID 链路状态模型
     * ------------------------------ */


    /** 对外暴露的 HID 状态 */
    val hidState = HidLinkModel()

    var bluetoothHidDevice: BluetoothHidDevice? = null
        private set
    var bluetoothDevice: BluetoothDevice? = null
        private set
    var isHidDeviceConnected = false
        private set

    private var bluetoothAdapter: BluetoothAdapter? = null

    /**
     * 初始化 HID
     */
    fun initLink(context: Context, macAddress: String) {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        bluetoothDevice = bluetoothAdapter?.getRemoteDevice(macAddress)

        if (hidState.appRegistered) {
            hidState.error = null
            bluetoothHidDevice?.connect(bluetoothDevice)
            return
        } else if (hidState.serviceConnected) {
            bluetoothHidDevice?.registerApp(
                HidConstants.SDP_RECORD, null, null, Runnable::run, callback
            )
        } else {
            bluetoothAdapter?.getProfileProxy(context, profileListener, BluetoothProfile.HID_DEVICE)
        }


    }

    /** ------------------------------
     * ServiceListener
     * ------------------------------ */
    private val profileListener = object : BluetoothProfile.ServiceListener {
        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile?) {
            if (profile == BluetoothProfile.HID_DEVICE) {

                bluetoothHidDevice = proxy as BluetoothHidDevice
                Log.d(TAG, "HID service connected")

                hidState.serviceConnected = true
                hidState.error = null

                bluetoothDevice?.let { device ->
                    val connected = bluetoothHidDevice?.connect(device) ?: false
                    Log.d(TAG, "Attempt connect to ${device.name}: $connected")
                }

                bluetoothHidDevice?.registerApp(
                    HidConstants.SDP_RECORD, null, null, Runnable::run, callback
                )
            }
        }

        override fun onServiceDisconnected(profile: Int) {
            if (profile == BluetoothProfile.HID_DEVICE) {
                Log.d(TAG, "HID service disconnected")

                bluetoothHidDevice = null
                isHidDeviceConnected = false

                hidState.serviceConnected = false
                hidState.deviceConnected = false
                hidState.appRegistered = false
                hidState.error = "HID service disconnected"
            }
        }
    }

    /** ------------------------------
     * HID 回调
     * ------------------------------ */
    val callback: BluetoothHidDevice.Callback = object : BluetoothHidDevice.Callback() {

        override fun onAppStatusChanged(device: BluetoothDevice?, registered: Boolean) {
            super.onAppStatusChanged(device, registered)

            Log.d(TAG, "onAppStatusChanged registered=$registered")

            hidState.appRegistered = registered
            hidState.error = if (registered) null else "registerApp failed"

            if (registered) {
                // 尝试重连
                bluetoothDevice?.let { dev ->
                    bluetoothHidDevice?.connect(dev)
                }
            }
        }

        override fun onConnectionStateChanged(device: BluetoothDevice, state: Int) {
            when (state) {
                BluetoothHidDevice.STATE_CONNECTED -> {
                    isHidDeviceConnected = true
                    hidState.deviceConnected = true
                    hidState.error = null
                }

                BluetoothHidDevice.STATE_DISCONNECTED -> {
                    isHidDeviceConnected = false
                    hidState.deviceConnected = false
                    hidState.error = "HID disconnected"
                }
            }
            Log.d(TAG, "HID ${device.address} state=$state")
        }
    }

    /**
     * 发送按键按下
     */
    @SuppressLint("MissingPermission")
    fun sendKeyDown(id: Int, data: ByteArray): Boolean {
        if (bluetoothHidDevice != null && isHidDeviceConnected) {
            LogUtils.d("sendKeyDown: ${data.toHexString()}")
            return bluetoothHidDevice!!.sendReport(
                bluetoothDevice, id, data
            )
        }
        return false
    }

    /**
     * 发送按键抬起
     */
    @SuppressLint("MissingPermission")
    fun sendKeyUp(): Boolean {
        if (bluetoothHidDevice != null && isHidDeviceConnected) {
            return bluetoothHidDevice!!.sendReport(
                bluetoothDevice, HidConstants.ID_REMOTE_CONTROL, REMOTE_INPUT_NONE
            )
        }
        return false
    }

    /** ByteArray 转 Hex 字符串 */
    private fun ByteArray.toHexString(): String = joinToString(" ") { "%02X".format(it) }

    /** ----------------------------------------
     * release() 释放资源
     * ---------------------------------------- */
    fun release() {
        Log.d(TAG, "Releasing HID profile...")

        bluetoothHidDevice?.unregisterApp()
        bluetoothAdapter?.closeProfileProxy(BluetoothProfile.HID_DEVICE, bluetoothHidDevice)

        bluetoothHidDevice = null
        bluetoothDevice = null
        isHidDeviceConnected = false

        // 清空状态
        hidState.serviceConnected = false
        hidState.appRegistered = false
        hidState.deviceConnected = false
        hidState.error = null

        Log.d(TAG, "HID released")
    }
}
