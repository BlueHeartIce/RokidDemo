package com.blue.glassesapp.common.model.bindmodel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.blue.armobile.BR

/**
 * @Description 眼睛信息
 * @Author zxh
 * @Date 2025/11/05
 * @Version 1.0
 */
class HidLinkModel : BaseObservable() {
    // HID profile 是否已连接
    var serviceConnected: Boolean = false
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.serviceConnected)
        }

    // registerApp 是否成功
    var appRegistered: Boolean = false
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.appRegistered)
        }

    // 设备 HID link 是否连上
    var deviceConnected: Boolean = false
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.deviceConnected)
        }
    var error: String? = null
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.error)
        }
}