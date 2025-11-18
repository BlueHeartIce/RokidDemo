package com.blue.glassesapp.feature.scanblueroorh

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.pm.PackageManager
import android.os.Build
import android.os.ParcelUuid
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.blue.armobile.R
import com.blue.armobile.databinding.ActivityBluetoothScanBinding
import com.blue.glassesapp.common.model.BluetoothLinkState
import com.blue.glassesapp.common.model.GlassesLinkState
import com.blue.glassesapp.core.base.BaseActivity
import com.blue.glassesapp.core.utils.CommonModel
import com.blue.glassesapp.core.utils.CxrUtil
import com.blue.glassesapp.feature.scanblueroorh.adapter.BluetoothDeviceAdapter
import com.blue.glassesapp.feature.scanblueroorh.model.BluetoothDevice
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID


class BluetoothScanActivity :
    BaseActivity<ActivityBluetoothScanBinding>(R.layout.activity_bluetooth_scan) {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private lateinit var deviceAdapter: BluetoothDeviceAdapter
    private var isScanning = false

    private val PERMISSION_REQUEST_CODE = 1

    private val TAG = "BluetoothScan"

    private val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    } else {
        arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onCreate() {
        CommonModel.isInBluetoothPairingPage = true
        initBluetooth()
        checkPermissions()
        binding.initViews()
    }


    /**
     * Initialize UI components
     */
    private fun ActivityBluetoothScanBinding.initViews() {
        deviceAdapter = BluetoothDeviceAdapter(mutableListOf()) { device ->
            // Handle item click events
            handleDeviceItemClick(device)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@BluetoothScanActivity)
            adapter = deviceAdapter
        }

        btnScan.setOnClickListener {
            if (isScanning) {
                stopScan()
            } else {
                startScan()
            }
        }
    }

    /**
     * Initialize Bluetooth components
     */
    private fun initBluetooth() {
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
    }

    /**
     * Check and request necessary permissions
     */
    private fun checkPermissions() {
        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
        }
    }

    /**
     * Check if all required permissions are granted
     * @return true if all permissions are granted, false otherwise
     */
    private fun hasPermissions(): Boolean {
        return permissions.all {
            ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    var isStart = false

    override fun onStart() {
        super.onStart()
        isStart = true
    }

    override fun onStop() {
        isStart = false
        super.onStop()
    }

    /**
     * Handle permission request results
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    ToastUtils.showShort("权限已授权")
                } else {

                    ToastUtils.showShort("请先授权")
                }
            }
        }
    }

    /**
     * Start Bluetooth LE scanning
     */
    private fun startScan() {
        val adapter = bluetoothAdapter
        val scanner = bluetoothLeScanner

        // Check if Bluetooth is supported
        if (adapter == null || scanner == null) {
            ToastUtils.showShort("请先打开蓝牙")
            return
        }

        // Check if Bluetooth is enabled
        if (!adapter.isEnabled) {
            ToastUtils.showShort("请先打开蓝牙")
            return
        }

        // Check permissions
        if (!hasPermissions()) {
            ToastUtils.showShort("请先授权")
            return
        }

        // Create scan filter for specific service UUID
        val filter = ScanFilter.Builder()
            .setServiceUuid(ParcelUuid(UUID.fromString("00009100-0000-1000-8000-00805f9b34fb")))
            .build()
        // Configure scan settings
        val settings = ScanSettings.Builder().build()
        try {
            deviceAdapter.clearDevices()
            scanner.startScan(listOf(filter), settings, scanCallback)
            isScanning = true
            binding.btnScan.text = "停止扫描"
            ToastUtils.showShort("已开始扫描...")
        } catch (e: SecurityException) {
            ToastUtils.showShort("请先授权蓝牙扫描")
        }
    }

    /**
     * Stop Bluetooth LE scanning
     */
    private fun stopScan() {
        val scanner = bluetoothLeScanner
        if (scanner != null) {
            try {
                scanner.stopScan(scanCallback)
            } catch (e: SecurityException) {
                // Ignore permission exceptions
            }
        }
        isScanning = false
        binding.btnScan.text = "开启蓝牙扫描"
        ToastUtils.showShort("已停止扫描")
    }

    /**
     * Bluetooth LE scan callback
     */
    private val scanCallback = object : ScanCallback() {
        /**
         * Called when a BLE device is found
         */
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            result?.let {
                val device = it.device
                val rssi = it.rssi
                val deviceName = try {
                    device.name
                } catch (e: SecurityException) {
                    null
                }
                if (ActivityCompat.checkSelfPermission(
                        this@BluetoothScanActivity, Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
//                if (device.bondState == android.bluetooth.BluetoothDevice.BOND_NONE) {
                // Create BluetoothDevice object and add to adapter
                val bluetoothDevice = BluetoothDevice(
                    device = device, name = deviceName, address = device.address, rssi = rssi
                )
                runOnUiThread {
                    deviceAdapter.addDevice(bluetoothDevice)
                }
//                }
            }
        }

        /**
         * Called when scan fails
         */
        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            runOnUiThread {
                ToastUtils.showShort("扫描失败 error code: $errorCode")
                isScanning = false
                binding.btnScan.text = "开启蓝牙扫描"
            }
        }
    }

    /**
     * Handle RecyclerView item click events for Bluetooth devices
     * @param device The clicked Bluetooth device
     */
    private fun handleDeviceItemClick(device: BluetoothDevice) {
        // Log device information
        LogUtils.d(TAG, "Clicked Bluetooth device information:")
        LogUtils.d(TAG, "  Name: ${device.name ?: "Unknown"}")
        LogUtils.d(TAG, "  Address: ${device.address}")
        LogUtils.d(TAG, "  Signal strength: ${device.rssi} dBm")

        ToastUtils.showShort(
            "蓝牙设备 \n" + "Name: ${device.name ?: "Unknown"}\n" + "Address: ${device.address}\n" + "Signal strength: ${device.rssi} d"
        )

        CxrUtil.initConnectDevice(
            this@BluetoothScanActivity,
            device.device,
            object : CxrUtil.BluetoothDeviceConnectListener {
                override fun onBluetoothConnect(state: BluetoothLinkState, msg: String) {
                    when (state) {
                        BluetoothLinkState.CONNECTING -> {
                            LogUtils.d(TAG, "Bluetooth connecting")
                            ToastUtils.showShort("蓝牙连接中")
                        }

                        BluetoothLinkState.CONNECTED -> {
                            LogUtils.d(TAG, "Bluetooth connected")
                            ToastUtils.showShort("蓝牙已连接")
                        }


                        BluetoothLinkState.DISCONNECTED -> {
                            LogUtils.d(TAG, "Bluetooth disconnected")
                            ToastUtils.showShort("蓝牙已断开")
                        }

                        BluetoothLinkState.FAILED -> {
                            LogUtils.d(TAG, "Bluetooth error")
                            ToastUtils.showShort("蓝牙连接错误：$msg")
                        }

                        BluetoothLinkState.ON_CONNECTION_INFO -> {
                            LogUtils.d(TAG, "Bluetooth connection info")
                            ToastUtils.showShort("获取到蓝牙连接信息")
                        }

                        else -> {
                            LogUtils.d(TAG, "Bluetooth error")
                            ToastUtils.showShort("蓝牙错误")
                        }
                    }
                }

                override fun onDeviceConnected(state: GlassesLinkState, msg: String) {
                    when (state) {
                        GlassesLinkState.CONNECTING -> {
                            LogUtils.d(TAG, "Glasses connecting")
                            ToastUtils.showShort("眼镜连接中")
                        }

                        GlassesLinkState.CONNECTED -> {
                            LogUtils.d(TAG, "Glasses connected")
                            ToastUtils.showShort("眼镜已连接,返回主界面")
                            MainScope().launch {
                                delay(1000)
                                finish()
                            }
                        }

                        GlassesLinkState.CONNECT_FAILED -> {
                            LogUtils.d(TAG, "Glasses disconnected")
                            ToastUtils.showShort("眼镜连接失败：$msg")
                        }

                        else -> {
                            LogUtils.d(TAG, "Glasses error")
                            ToastUtils.showShort("眼镜错误")
                        }
                    }
                }
            })
    }


    /**
     * Called when the activity is destroyed
     */
    override fun onDestroy() {
        super.onDestroy()
        CommonModel.isInBluetoothPairingPage = false
        if (isScanning) {
            stopScan()
        }
    }


}