package com.blue.glassesapp.feature.home.ui

import android.graphics.Typeface
import android.util.TypedValue
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blue.armobile.R
import com.blue.armobile.databinding.FragmentHomeBinding
import com.blue.glassesapp.common.model.GlassesLinkState
import com.blue.glassesapp.core.base.BaseQMUIFragment
import com.blue.glassesapp.core.utils.CommonModel
import com.blue.glassesapp.core.utils.CxrUtil
import com.blue.glassesapp.feature.home.vm.HomeVm
import com.blue.glassesapp.feature.scanblueroorh.BluetoothScanActivity
import com.qmuiteam.qmui.util.QMUIViewHelper


class HomeFragment : BaseQMUIFragment<FragmentHomeBinding>(R.layout.fragment_home),
    NetworkUtils.OnNetworkStatusChangedListener {
    private val viewModel: HomeVm by activityViewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreate() {
        binding.model = viewModel
        LogUtils.d(TAG, viewModel.toString())
        binding.initView()
    }

    override fun onNetworkStatusChanged(isConnected: Boolean) {
    }

    private fun FragmentHomeBinding.initView() {
        initTopBar()

        seekBarVolume.max = 15
        seekBarVolume.min = 0
        seekBarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                LogUtils.d(TAG, "onProgressChanged")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                LogUtils.d(TAG, "onStartTrackingTouch")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                LogUtils.d(TAG, "onStopTrackingTouch")
                seekBar?.let {
                    CommonModel.glassesInfo.volume = it.progress
                    CxrUtil.setVolume(it.progress)
                }
            }
        })


        seekBarBrightness.max = 15
        seekBarBrightness.min = 0
        seekBarBrightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    CommonModel.glassesInfo.brightness = it.progress
                    CxrUtil.setBrightness(it.progress)
                }
            }
        })
    }

    fun FragmentHomeBinding.initTopBar() {
        topBar.addLeftTextButton("主页", QMUIViewHelper.generateViewId()).apply {
            setTextColor(resources.getColor(R.color.black))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
            // 设置字体加粗
            setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD)
            setTextColor(resources.getColorStateList(R.color.text_primary, null))
        }


        topBar.addRightTextButton("添加", QMUIViewHelper.generateViewId()).apply {
            setTextColor(resources.getColor(R.color.text_primary))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        }.setOnClickListener {
            ActivityUtils.startActivity(BluetoothScanActivity::class.java)
        }

        btnToConnect.setOnClickListener {
            CxrUtil.initGlassesLink(requireActivity().application, {
                LogUtils.d(TAG, "linkGlasses: $it")
                val linkState = viewModel.glassesInfo.value?.glassesLinkState
                if (linkState == GlassesLinkState.UNPAIRED || CommonModel.deviceDeviceName.isNullOrEmpty()) {
                    ActivityUtils.startActivity(BluetoothScanActivity::class.java)
                } else if (linkState == GlassesLinkState.UNCONNECTED || linkState == GlassesLinkState.CONNECT_FAILED) {
                    viewModel.linkGlasses()
                }
            })
        }
    }
}