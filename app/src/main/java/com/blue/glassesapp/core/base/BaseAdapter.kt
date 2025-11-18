package com.blue.glassesapp.core.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

/**
 * @Description TODO
 *
 *
 * @Author liux
 * @Date 2023/6/13 18:44
 * @Version 1.0
 */
abstract class BaseAdapter<T : RecyclerView.ViewHolder,M>(val mdatas: ArrayList<M>, val layoutId:Int): Adapter<T>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        val view = LayoutInflater.from(parent.context).inflate(layoutId,parent,false)
        return getViewH(view)
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        loadData(holder,position)
        holder?.itemView?.setOnClickListener { lis?.click(mdatas[position]) }
    }

    var lis :OnItemClickLis<M>?=null

    fun setLis2(click: OnItemClickLis<M>){
        this.lis = click
    }

    interface OnItemClickLis<K>{
        fun click(m:K)
    }

    override fun getItemCount() = mdatas.size

    abstract fun getViewH(view:View):T

    abstract fun loadData(holder: T,position: Int)
}