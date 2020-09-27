package com.ybw.yibai.common.adapter

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.ybw.yibai.R
import com.ybw.yibai.common.bean.BTCBean
import com.ybw.yibai.common.callback.IOnSelectListener
import com.ybw.yibai.common.utils.ImageUtil
import com.ybw.yibai.common.utils.OtherUtil
import com.ybw.yibai.common.widget.RectangleView

class BTNAdapter(context: Context) : RecyclerArrayAdapter<BTCBean.Child>(context) {
    var key: String = ""
    var field: String = "0"
    var multipleSelect: Boolean = false
    var onSelectListener: IOnSelectListener<Pair<String, BTCBean.Child>>? = null
    var onTimeSelectListener: IOnSelectListener<Pair<String, List<BTCBean.Child>>>? = null

    init {
        super.setOnItemClickListener {
            onTimeSelectListener?.onSelect(Pair(key, allData))
            if (allData[it].select) {
                allData[it].select = false;
            } else {
                allData.forEach { it.select = false }
                allData[it].select = true;
            }
            notifyDataSetChanged()
            onSelectListener?.onSelect(Pair(key, allData[it]))
        }
    }

    override fun getViewType(position: Int): Int {
        return allData[position].screen_type?.toInt() ?: -1
    }

    override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<BTCBean.Child> {
        return when (viewType) {
            1 -> ViewHolderText(parent!!)
            2 -> ViewHolderPicAndText(parent!!)
            3 -> ViewHolderColor(parent!!)
            else -> ViewHolderText(parent!!)
        }
    }

    inner class ViewHolderPicAndText(viewGroup: ViewGroup) : BaseViewHolder<BTCBean.Child>(viewGroup, R.layout.item_adapter_1) {
        val container: RelativeLayout = `$`(R.id.unSelectLayout)
        val name: TextView = `$`(R.id.unSelectTextView)
        val image: ImageView = `$`(R.id.unSelectImageView)

        override fun setData(data: BTCBean.Child?) {
            super.setData(data)
            data?.let {
                name.text = it.name
                ImageUtil.displayImage(context, image, it.pic)
                if (it.select) {
                    name.setTextColor(ContextCompat.getColor(context, R.color.embellishment_text_color))
                    container.setBackgroundResource(R.drawable.blue_right_top_bottom_radius)
                } else {
                    name.setTextColor(ContextCompat.getColor(context, R.color.prompt_low_text_color))
                    container.setBackgroundResource(R.drawable.gray_right_top_bottom_radius)
                }
            }
        }
    }

    inner class ViewHolderText(viewGroup: ViewGroup) : BaseViewHolder<BTCBean.Child>(viewGroup, R.layout.item_adapter_2) {
        val name: TextView = `$`(R.id.nameTextView)
        override fun setData(data: BTCBean.Child?) {
            super.setData(data)
            data?.let {
                name.text = it.name
                if (it.select) {
                    name.setTextColor(ContextCompat.getColor(context, R.color.embellishment_text_color))
                    name.background = ContextCompat.getDrawable(context, R.drawable.background_product_screening_text_selected)
                } else {
                    name.setTextColor(ContextCompat.getColor(context, R.color.prompt_low_text_color))
                    name.background = ContextCompat.getDrawable(context, R.drawable.background_product_screening_text_unselected)
                }
            }
        }
    }

    inner class ViewHolderColor(viewGroup: ViewGroup) : BaseViewHolder<BTCBean.Child>(viewGroup, R.layout.item_adapter_3) {
        val container: View = `$`(R.id.container)
        var mRectangleView: RectangleView = `$`(R.id.rectangleView)
        var mView: View = `$`(R.id.view)
        override fun setData(data: BTCBean.Child?) {
            super.setData(data)
            data?.let {
                if (!TextUtils.isEmpty(it.color) && OtherUtil.isColor(it.color)) {
                    mRectangleView.setBackgroundColor(Color.parseColor(it.color))
                } else {
                    mRectangleView.setBackgroundColor(Color.TRANSPARENT)
                }
                if (it.select) {
                    mView.visibility = View.VISIBLE
                } else {
                    mView.visibility = View.INVISIBLE
                }
            }
        }
    }
}