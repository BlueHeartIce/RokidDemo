package com.blue.glassesapp.common.binding

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.blue.armobile.R
import com.blue.glassesapp.common.model.GlassesLinkState


/**
 * <pre>
 *
 * </pre>
 *
 * <p>创建人: zxh</p>
 * <p>日期: 2025/11/5</p>
 */
object AppBindingAdapter {
    @JvmStatic
    @BindingAdapter("batteryLevelText")
    fun setBatteryText(view: TextView, level: Int) {
        view.text = view.context.getString(R.string.battery_level, level)
    }

    /**
     * 设备连接中
     */
    @JvmStatic
    @BindingAdapter("deviceConnecting")
    fun setDeviceConnecting(view: View, linkState: GlassesLinkState) {
        view.isVisible =
            linkState == GlassesLinkState.CONNECTING || linkState == GlassesLinkState.CONNECTION_INFO
    }

    /**
     * 设备未配对或者连接失败
     */
    @JvmStatic
    @BindingAdapter("deviceNoConnect")
    fun setDeviceUnpair(view: TextView, linkState: GlassesLinkState) {
        view.isVisible =
            linkState == GlassesLinkState.UNPAIRED || linkState == GlassesLinkState.CONNECT_FAILED || linkState == GlassesLinkState.UNCONNECTED
        view.text = when (linkState) {
            GlassesLinkState.UNPAIRED -> view.context.getString(R.string.go_pair_up)
            GlassesLinkState.UNCONNECTED, GlassesLinkState.CONNECT_FAILED -> view.context.getString(
                R.string.go_connect
            )

            else -> ""
        }
    }

    /**
     * 设备已连接
     */
    @JvmStatic
    @BindingAdapter("deviceConnected")
    fun setDeviceConnected(view: View, linkState: GlassesLinkState) {
        view.isVisible = linkState == GlassesLinkState.CONNECTED
    }

    /**
     * 电池电量图片
     */
    @JvmStatic
    @BindingAdapter("charging", "batteryLevelImage")
    fun setBatteryImage(view: TextView, charging: Boolean, level: Int) {
        if (charging) {
            view.text = "正在充电"
        } else {
            view.text = "电量:$level %"
        }
    }
}