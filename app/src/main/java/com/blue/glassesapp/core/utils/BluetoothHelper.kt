package com.blue.glassesapp.core.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.ParcelUuid
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.ConcurrentHashMap


/**
 * <pre>
 *
 * </pre>
 *
 * <p>创建人: zxh</p>
 * <p>日期: 2025/9/15</p>
 */
/**
 * Bluetooth Helper
 * @author rokid
 * @date 2025/04/27
 * @param context Activity Register Context
 * @param initStatus Init Status
 * @param deviceFound Device Found
 */
class BluetoothHelper(
    val context: AppCompatActivity,
    val initStatus: (INIT_STATUS) -> Unit,
    val deviceFound: () -> Unit
) {
    companion object {
        const val TAG = "Rokid Glasses CXR-M"
        // Request Code
        const val REQUEST_CODE_PERMISSIONS = 100

        // Required Permissions
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
        ).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                add(Manifest.permission.BLUETOOTH_SCAN)
                add(Manifest.permission.BLUETOOTH_CONNECT)
            }
        }.toTypedArray()

        // Init Status
        enum class INIT_STATUS {
            NotStart,
            INITING,
            INIT_END
        }
    }

    // Scan Results
    val scanResultMap: ConcurrentHashMap<String, BluetoothDevice> = ConcurrentHashMap()

    // Bonded Devices
    val bondedDeviceMap: ConcurrentHashMap<String, BluetoothDevice> = ConcurrentHashMap()

    // Scanner
    private val scanner by lazy {
        adapter?.bluetoothLeScanner ?: run {
            Toast.makeText(context, "Bluetooth is not supported", Toast.LENGTH_SHORT).show()
            showRequestPermissionDialog()
            throw Exception("Bluetooth is not supported!!")
        }
    }

    // Bluetooth Enabled
    @SuppressLint("MissingPermission")
    private val bluetoothEnabled: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
        this.observe(context) {
            if (this.value == true) {
                initStatus.invoke(INIT_STATUS.INIT_END)
                startScan()
            } else {
                showRequestBluetoothEnableDialog()
            }
        }
    }

    //  Bluetooth State Listener
    private val requestBluetoothEnable = context.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            adapter = manager?.adapter
        } else {
            showRequestBluetoothEnableDialog()
        }
    }

    // Bluetooth Adapter
    private var adapter: BluetoothAdapter? = null
        set(value) {
            field = value
            value?.let {
                if (!it.isEnabled) {
                    //to Enable it
                    requestBluetoothEnable.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
                } else {
                    bluetoothEnabled.postValue(true)
                }
            }
        }

    // Bluetooth Manager
    private var manager: BluetoothManager? = null
        set(value) {
            field = value
            initStatus.invoke(INIT_STATUS.INITING)
            value?.let {
                adapter = it.adapter
            } ?: run {
                Toast.makeText(context, "Bluetooth is not supported", Toast.LENGTH_SHORT).show()
                showRequestPermissionDialog()
            }
        }

    // Permission Result
    val permissionResult: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
        this.observe(context) {
            if (it == true) {
                manager =
                    context.getSystemService(AppCompatActivity.BLUETOOTH_SERVICE) as BluetoothManager
            } else {
                showRequestPermissionDialog()
            }
        }
    }

    // Scan Listener
    val scanListener = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            result?.let { r ->
                r.device.name?.let {
                    scanResultMap[it] = r.device
                    deviceFound.invoke()
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Toast.makeText(
                context,
                "Scan Failed $errorCode",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    // check permissions
    fun checkPermissions() {
        initStatus.invoke(INIT_STATUS.NotStart)

        val allGranted = REQUIRED_PERMISSIONS.all { perm ->
            ContextCompat.checkSelfPermission(
                context,
                perm
            ) == PackageManager.PERMISSION_GRANTED
        }

        if (allGranted) {
            // 权限已经全部授予，直接初始化 BluetoothManager
            manager = context.getSystemService(AppCompatActivity.BLUETOOTH_SERVICE) as BluetoothManager
            permissionResult.postValue(true)
        } else {
            // 申请权限
            context.requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            context.registerReceiver(
                bluetoothStateListener,
                IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
            )
        }
//        context.requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
//        context.registerReceiver(
//            bluetoothStateListener,
//            IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
//        )

    }

    // Release
    @SuppressLint("MissingPermission")
    fun release() {
        context.unregisterReceiver(bluetoothStateListener)
        stopScan()
        permissionResult.postValue(false)
        bluetoothEnabled.postValue(false)
    }


    // Show Request Permission Dialog
    private fun showRequestPermissionDialog() {
        AlertDialog.Builder(context)
            .setTitle("Permission")
            .setMessage("Please grant the permission")
            .setPositiveButton("OK") { _, _ ->
                context.requestPermissions(
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
            .setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(
                    context,
                    "Permission does not granted, FINISH",
                    Toast.LENGTH_SHORT
                ).show()
                context.finish()
            }
            .show()
    }

    // Show Request Bluetooth Enable Dialog
    private fun showRequestBluetoothEnableDialog() {
        AlertDialog.Builder(context)
            .setTitle("Bluetooth")
            .setMessage("Please enable the bluetooth")
            .setPositiveButton("OK") { _, _ ->
                requestBluetoothEnable.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            }
            .setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(
                    context,
                    "Bluetooth does not enabled, FINISH",
                    Toast.LENGTH_SHORT
                ).show()
                context.finish()
            }
            .show()
    }

    // Start Scan
    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun startScan() {
        scanResultMap.clear()
        val connectedList = getConnectedDevices()
        for (device in connectedList) {
            device.name?.let {
                if (it.contains("Glasses", false)) {
                    bondedDeviceMap[it] = device
                    deviceFound.invoke()
                }
            }
        }

        adapter?.bondedDevices?.forEach { d ->
            d.name?.let {
                if (it.contains("Glasses", false)) {
                    if (bondedDeviceMap[it] == null) {
                        bondedDeviceMap[it] = d
                    }
                }
                deviceFound.invoke()
            }
        }

        try {
            scanner.startScan(
                listOf<ScanFilter>(
                    ScanFilter.Builder()
                        .setServiceUuid(ParcelUuid.fromString("00009100-0000-1000-8000-00805f9b34fb"))//Rokid Glasses Service
                        .build()
                ), ScanSettings.Builder().build(),
                scanListener
            )
        } catch (e: Exception) {
            Toast.makeText(context, "Scan Failed ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Stop Scan
    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun stopScan() {
        scanner.stopScan(scanListener)
    }

    //  Get Connected Devices
    @SuppressLint("MissingPermission")
    private fun getConnectedDevices(): List<BluetoothDevice> {
        return adapter?.bondedDevices?.filter { device ->
            try {
                val isConnected =
                    device::class.java.getMethod("isConnected").invoke(device) as Boolean
                isConnected
            } catch (_: Exception) {
                Toast.makeText(context, "Get Connected Devices Failed", Toast.LENGTH_SHORT).show()
                false
            }
        } ?: emptyList()
    }

    // Bluetooth State Listener
    val bluetoothStateListener = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                when (state) {
                    BluetoothAdapter.STATE_TURNING_ON -> {
                        initStatus.invoke(INIT_STATUS.INITING)
                    }
                    BluetoothAdapter.STATE_ON -> {
                        bluetoothEnabled.postValue(true)
                        permissionResult.postValue(true) // 蓝牙已开启 + 权限可用
                    }
                    BluetoothAdapter.STATE_TURNING_OFF -> {
                        initStatus.invoke(INIT_STATUS.NotStart)
                    }
                    BluetoothAdapter.STATE_OFF -> {
                        initStatus.invoke(INIT_STATUS.NotStart)
                        bluetoothEnabled.postValue(false)
                        permissionResult.postValue(false) // 蓝牙关闭时认为不可用
                    }
                    else -> {
                        initStatus.invoke(INIT_STATUS.NotStart)
                        permissionResult.postValue(false)
                    }
                }
            }
        }
    }

}