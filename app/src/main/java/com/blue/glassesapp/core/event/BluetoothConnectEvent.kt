package com.blue.glassesapp.core.event

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.BOND_NONE
import android.bluetooth.BluetoothDevice.DEVICE_TYPE_UNKNOWN


/**
 * <pre>
 *     蓝牙连接事件
 * </pre>
 *
 * <p>创建人: zxh</p>
 * <p>日期: 2025/11/5</p>
 */
class BluetoothConnectEvent {
    var name = ""
    var address = ""
    var bondState = BOND_NONE
    var type = DEVICE_TYPE_UNKNOWN
    var blueAction = ""


}