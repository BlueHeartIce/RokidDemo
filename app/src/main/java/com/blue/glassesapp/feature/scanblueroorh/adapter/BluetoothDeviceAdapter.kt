package com.blue.glassesapp.feature.scanblueroorh.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blue.armobile.R
import com.blue.glassesapp.feature.scanblueroorh.model.BluetoothDevice

class BluetoothDeviceAdapter(
    private val deviceList: MutableList<BluetoothDevice>,
    private val itemClickListener: ((BluetoothDevice) -> Unit)? = null
) :
    RecyclerView.Adapter<BluetoothDeviceAdapter.DeviceViewHolder>() {

    class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.device_name)
        val addressTextView: TextView = itemView.findViewById(R.id.device_address)
        val rssiTextView: TextView = itemView.findViewById(R.id.device_rssi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bluetooth_device, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = deviceList[position]
        holder.nameTextView.text = device.name ?: "Unknown Device"
        holder.addressTextView.text = device.address
        holder.rssiTextView.text = "${device.rssi} dBm"

        // 设置点击监听器
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(device)
        }
    }

    override fun getItemCount(): Int = deviceList.size

    fun addDevice(device: BluetoothDevice) {
        // 检查设备是否已经存在
        if (!deviceList.any { it.address == device.address }) {
            deviceList.add(device)
            notifyItemInserted(deviceList.size - 1)
        }
    }

    fun clearDevices() {
        deviceList.clear()
        notifyDataSetChanged()
    }
}