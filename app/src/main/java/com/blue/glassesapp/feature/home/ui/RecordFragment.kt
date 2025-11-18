package com.blue.glassesapp.feature.home.ui

import android.graphics.Typeface
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.Observable
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ActivityUtils
import com.blue.armobile.BR
import com.blue.armobile.R
import com.blue.armobile.databinding.FragmentRecordBinding
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIViewHelper
import com.blue.glassesapp.core.base.BaseQMUIFragment
import com.blue.glassesapp.core.db.entity.GlassesRecordModel
import com.blue.glassesapp.feature.home.vm.HomeVm
import com.blue.glassesapp.feature.scanblueroorh.BluetoothScanActivity


/**
 * <pre>
 * 眼镜交互记录
 * </pre>
 *
 * <p>创建人: zxh</p>
 * <p>日期: 2025/11/6</p>
 */
class RecordFragment : BaseQMUIFragment<FragmentRecordBinding>(R.layout.fragment_record) {
    private val viewModel: HomeVm by activityViewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreate() {
        binding.initView()
        binding.model = viewModel
        viewModel.recordViewCallBack = object : HomeVm.RecordViewCallBack {
            override fun onRecordViewCallBack(recordModel: GlassesRecordModel?) {
                binding.recyclerView.scrollToPosition(0)


            }

        }
    }

    fun FragmentRecordBinding.initView() {
        initTopBar()
        val layoutManager = LinearLayoutManager(context)
        layoutManager.stackFromEnd = true // ✅ 最新消息显示在底部
        layoutManager.reverseLayout = true
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = this@RecordFragment.viewModel.recordAdapter
    }


    var leftButton: Button? = null
    fun FragmentRecordBinding.initTopBar() {
        leftButton = topBar.addLeftTextButton(
            viewModel.glassesInfo.value?.name, QMUIViewHelper.generateViewId()
        ).apply {
            setTextColor(resources.getColor(R.color.black))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
            // 设置字体加粗
            setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD)
            setTextColor(resources.getColorStateList(R.color.text_primary, null))
        }
        viewModel.glassesInfo.value?.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(
                sender: Observable?,
                propertyId: Int,
            ) {
                if (propertyId == BR.name) {
                    leftButton?.post {
                        leftButton?.text = viewModel.glassesInfo.value?.name
                    }
                }
            }
        })

        topBar.addRightTextButton("添加", QMUIViewHelper.generateViewId()).apply {
            setTextColor(resources.getColor(R.color.text_primary))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        }.setOnClickListener {
            ActivityUtils.startActivity(BluetoothScanActivity::class.java)
        }

    }


    override fun onNetworkStatusChanged(isConnected: Boolean) {

    }

}