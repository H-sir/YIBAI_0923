package com.ybw.yibai.common.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import android.widget.TextView
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.ybw.yibai.R
import com.ybw.yibai.common.bean.BTCBean
import com.ybw.yibai.common.callback.IOnSelectListener

class BTCAdapter(context: Context) : RecyclerArrayAdapter<BTCBean>(context) {
    var onSelectListener: IOnSelectListener<BTCBean>? = null

    init {
        setOnItemClickListener {
            val item = allData[it]
            allData?.forEach { __it ->
                __it.select = false
            }
            item.select = true
            notifyDataSetChanged()
            onSelectListener?.onSelect(item)
        }
    }

    override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<BTCBean> {
        return ViewHolder(parent!!)
    }

    inner class ViewHolder(viewGroup: ViewGroup) : BaseViewHolder<BTCBean>(viewGroup, R.layout.item_bottom_tab) {
        val text: TextView = `$`(R.id.text1)

        override fun setData(data: BTCBean?) {
            super.setData(data)
            data?.let {
                if (it.mName != null && !it.mName.isEmpty())
                    text.text = it.mName
                else
                    text.text = it.name
                text.setTextColor(ContextCompat.getColor(context, if (it.select) R.color.blue else R.color.black))
            }
        }
    }
}