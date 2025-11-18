package com.blue.glassesapp.core.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.NetworkUtils.OnNetworkStatusChangedListener
import com.qmuiteam.qmui.arch.QMUIFragment


/**
 * @Description TODO
 *
 *
 * @Author liux
 * @Date 2022/11/11 15:42
 * @Version 1.0
 */
open abstract class BaseQMUIFragment<T : ViewDataBinding>(private val contentLayoutId: Int) :
    QMUIFragment(), OnNetworkStatusChangedListener {
    val TAG = this.javaClass.simpleName
    lateinit var binding: T
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkUtils.registerNetworkStatusChangedListener(this)
    }


    override fun onCreateView(): View {
        binding =
            DataBindingUtil.inflate(LayoutInflater.from(activity), contentLayoutId, null, false)
        onCreate()
        return binding.root
    }


    abstract fun onCreate()

    override fun onDestroy() {
        super.onDestroy()
        NetworkUtils.unregisterNetworkStatusChangedListener(this)
    }

    override fun onDisconnected() {
        onNetworkStatusChanged(false)
    }

    override fun onConnected(p0: NetworkUtils.NetworkType?) {
        onNetworkStatusChanged(true)
    }


    abstract fun onNetworkStatusChanged(isConnected: Boolean)
}