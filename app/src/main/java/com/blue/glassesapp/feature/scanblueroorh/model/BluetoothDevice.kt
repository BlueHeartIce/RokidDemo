package com.blue.glassesapp.feature.scanblueroorh.model

import android.bluetooth.BluetoothDevice

data class BluetoothDevice(
    val device: BluetoothDevice,
    val name: String?,
    val address: String,
    val rssi: Int
)