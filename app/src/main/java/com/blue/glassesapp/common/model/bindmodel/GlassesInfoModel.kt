package com.blue.glassesapp.common.model.bindmodel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.blue.armobile.BR
import com.blue.glassesapp.common.model.GlassesLinkState

/**
 * @Description 眼睛信息
 * @Author zxh
 * @Date 2025/11/05
 * @Version 1.0
 */
class GlassesInfoModel : BaseObservable() {
    var name = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    var deviceId = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.deviceId)
        }

    var secret = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.secret)
        }

    var battery = 0
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.battery)
        }

    var charging = false
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.charging)
        }

    /**
     * 亮度
     * 0-15
     */
    var brightness = 0
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.brightness)
        }

    /**
     * 音量
     * 0-15
     */
    var volume = 0
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.volume)
        }


    var glassesLinkState: GlassesLinkState = GlassesLinkState.UNPAIRED
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.glassesLinkState)
        }

}