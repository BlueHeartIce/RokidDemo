package com.blue.glassesapp.feature.home.ui.adapter

import android.graphics.Color
import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.TimeUtils
import com.blue.armobile.R
import com.blue.armobile.databinding.ItemChatMessageBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.blue.glassesapp.common.enums.InteractionDirection
import com.blue.glassesapp.core.base.BaseAdapter
import com.blue.glassesapp.core.db.entity.GlassesRecordModel
import com.blue.glassesapp.core.utils.AppTimeUtils
import kotlin.collections.get


/**
 * <pre>
 *
 * </pre>
 *
 * <p>创建人: zxh</p>
 * <p>日期: 2025/11/7</p>
 */
class RecordAdapter(datas: ArrayList<GlassesRecordModel>) :
    BaseAdapter<RecordAdapter.RecordVH, GlassesRecordModel>(
        datas, R.layout.item_chat_message
    ) {


    class RecordVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = DataBindingUtil.bind<ItemChatMessageBinding>(itemView)
    }

    override fun getViewH(view: View) = RecordVH(view)

    override fun loadData(holder: RecordVH, position: Int) {
        val record = mdatas[position]
        // 方向：SEND / RECEIVE
        val isSend = record.direction == InteractionDirection.FROM_DEVICE.value
        holder.binding?.apply {
            tvTime.text = TimeUtils.millis2String(
                record.timestamp, AppTimeUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS
            )
            tvContent.text = record.message
            if (isSend) {
                layoutBubble.setBackgroundResource(R.drawable.bg_chat_bubble_send)
                tvContent.setTextColor(Color.WHITE)
                // 右对齐
                ivAvatarRight.visibility = View.VISIBLE
                ivAvatarLeft.visibility = View.GONE
            } else {
                layoutBubble.setBackgroundResource(R.drawable.bg_chat_bubble_receive)
                tvContent.setTextColor(Color.BLACK)
                // 左对齐
                ivAvatarLeft.visibility = View.VISIBLE
                ivAvatarRight.visibility = View.GONE
            }

            val isShowText = !record.message.isNullOrEmpty()
            layoutBubble.isVisible = isShowText
            tvContent.isVisible = isShowText
            // 文本显示
            if (isShowText) {
                tvContent.text = record.message
            }

            val isShowImage = !record.contentImg.isNullOrEmpty()
            ivImage.isVisible = isShowImage
            // 图片显示
            if (isShowImage) {
                Glide.with(holder.binding.root.context).load(record.contentImg)
//                    .placeholder(R.drawable.ic_image_placeholder).error(R.drawable.ic_broken_image)
                    // 圆角
                    .transform(CenterCrop(), RoundedCorners(12)).into(ivImage)
            }
        }
    }

    /**
     * 添加数据
     */
    fun addData(data: GlassesRecordModel) {
        mdatas.add(0, data)
        notifyItemInserted(0)
    }

    /**
     * 添加数据
     */
    fun addDatas(data: List<GlassesRecordModel>) {
        mdatas.addAll(0, data)
        notifyItemRangeInserted(0, data.size)
    }

    /**
     * 清空并添加数据
     */
    fun setDatas(data: List<GlassesRecordModel>) {
        mdatas.clear()
        mdatas.addAll(data)
        notifyDataSetChanged()
    }


}