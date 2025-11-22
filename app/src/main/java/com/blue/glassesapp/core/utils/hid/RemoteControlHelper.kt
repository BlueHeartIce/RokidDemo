package com.blue.glassesapp.core.utils.hid

import android.annotation.SuppressLint
import com.blue.glassesapp.feature.home.model.REMOTE_INPUT_NONE

object RemoteControlHelper {
    /** 内部统一发送 Report */
    @SuppressLint("MissingPermission")
    private fun send(data: ByteArray): Boolean {
        return BluetoothHidManager.sendKeyDown(HidConstants.ID_REMOTE_CONTROL, data)
    }

    /** 按键按下 */
    @SuppressLint("MissingPermission")
    fun sendKeyDown(data: ByteArray): Boolean {
        return send(data)
    }

    /**
     *
     *  按键抬起（全 0 report）
     *  */
    @SuppressLint("MissingPermission")
    fun sendKeyUp(): Boolean {
        return send(REMOTE_INPUT_NONE)
    }
}
