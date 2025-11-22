package com.blue.glassesapp.feature.home.ui

import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.blue.armobile.R
import com.blue.armobile.databinding.FragmentRemoteControlBinding
import com.blue.glassesapp.core.base.BaseQMUIFragment
import com.blue.glassesapp.core.utils.CommonModel
import com.blue.glassesapp.core.utils.CxrUtil
import com.blue.glassesapp.core.utils.hid.BluetoothHidManager
import com.blue.glassesapp.core.utils.hid.RemoteControlHelper
import com.blue.glassesapp.core.utils.hid.RemoteInput
import com.blue.glassesapp.feature.home.vm.HomeVm
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import com.rokid.cxr.client.extend.CxrApi

/**
 * <pre>
 * 遥控器页面
 * </pre>
 *
 * <p>创建人: zxh</p>
 * <p>日期: 2024/9/18</p>
 */
class RemoteControlFragment :
    BaseQMUIFragment<FragmentRemoteControlBinding>(R.layout.fragment_remote_control),
    View.OnClickListener {
    private val viewModel: HomeVm by activityViewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreate() {
        binding.initView()
        binding.model = viewModel
    }

    override fun onNetworkStatusChanged(isConnected: Boolean) {
    }

    fun FragmentRemoteControlBinding.initView() {

        btnToConnect.setOnClickListener {
            if (CommonModel.deviceMacAddress.isNullOrEmpty()) {
                ToastUtils.showShort("请先连接设备")
            } else {
                BluetoothHidManager.initLink(requireActivity(), CommonModel.deviceMacAddress)
            }
        }

        mIbKeyboardHome.setOnClickListener(this@RemoteControlFragment)
//        mIbKeyboardBack.setOnClickListener(this@RemoteControlFragment)
//        mIbKeyboardPower.setOnClickListener(this@RemoteControlFragment)
//        mIbKeyboardVolumeUp.setOnClickListener(this@RemoteControlFragment)
//        mIbKeyboardVolumeDown.setOnClickListener(this@RemoteControlFragment)

        mIbKeyboardVolumeUp.setOnTouchListener(touchListener)
        mIbKeyboardVolumeDown.setOnTouchListener(touchListener)

        btnUp.setOnTouchListener(
            touchListener
        )
        btnDown.setOnTouchListener(
            touchListener
        )
        btnLeft.setOnTouchListener(
            touchListener
        )
        btnRight.setOnTouchListener(
            touchListener
        )
        btnCenter.setOnTouchListener(
            touchListener
        )
        mIbKeyboardBack.setOnTouchListener(
            touchListener
        )


    }


    val touchListener = object : View.OnTouchListener {
        override fun onTouch(
            view: View?,
            event: MotionEvent?,
        ): Boolean {
            if (event?.action == MotionEvent.ACTION_UP) {
                RemoteControlHelper.sendKeyUp()
                return true
            } else if (event?.action == MotionEvent.ACTION_DOWN) {
                when (view?.id) {
                    R.id.btn_up -> {
                        RemoteControlHelper.sendKeyDown(
                            RemoteInput.REMOTE_INPUT_MENU_UP
                        )
                    }

                    R.id.btn_down -> {
                        RemoteControlHelper.sendKeyDown(
                            RemoteInput.REMOTE_INPUT_MENU_DOWN
                        )
                    }

                    R.id.btn_left -> {
                        RemoteControlHelper.sendKeyDown(
                            RemoteInput.REMOTE_INPUT_MENU_LEFT
                        )
                    }

                    R.id.btn_right -> {
                        RemoteControlHelper.sendKeyDown(
                            RemoteInput.REMOTE_INPUT_MENU_RIGHT
                        )
                    }

                    R.id.btn_center -> {
                        RemoteControlHelper.sendKeyDown(
                            RemoteInput.REMOTE_INPUT_MENU_PICK
                        )
                    }

                    R.id.mIbKeyboardBack -> {
                        RemoteControlHelper.sendKeyDown(
                            RemoteInput.REMOTE_INPUT_BACK
                        )
                    }

                    R.id.mIbKeyboardVolumeUp -> {
                        RemoteControlHelper.sendKeyDown(
                            RemoteInput.REMOTE_INPUT_VOLUME_INC
                        )
                    }

                    R.id.mIbKeyboardVolumeDown -> {
                        RemoteControlHelper.sendKeyDown(
                            RemoteInput.REMOTE_INPUT_VOLUME_DEC
                        )
                    }
                }
            }
            return true
        }
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.mIbKeyboardHome -> {
//                viewmodel.onHomeClick()
            }

            R.id.mIbKeyboardPower -> {
                QMUIDialog.MessageDialogBuilder(requireActivity()).setTitle("提示")
                    .setMessage("确定要关机吗？").addAction(
                        QMUIDialogAction(
                            "取消"
                        ) { dialog, p1 -> dialog?.dismiss() })
                    .addAction(QMUIDialogAction("取消") { dialog, p1 ->
                        CxrApi.getInstance().notifyGlassShutdown()
                        dialog?.dismiss()
                    }).show()
            }

            R.id.mIbKeyboardVolumeUp -> {
                viewModel.glassesInfo.value?.let {
                    if (it.volume >= 15) {
                        return
                    }
                    it.volume += 1
                    CxrUtil.setVolume(it.volume)
                }
            }

            R.id.mIbKeyboardVolumeDown -> {
                viewModel.glassesInfo.value?.let {
                    if (it.volume <= 0) {
                        return
                    }
                    it.volume -= 1
                    CxrUtil.setVolume(it.volume)
                }
            }
        }
    }
}
