package com.ybw.yibai.common.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.ybw.yibai.R
import com.ybw.yibai.common.bean.ListBean
import com.ybw.yibai.common.callback.IOnSelectListener
import com.ybw.yibai.common.utils.ImageUtil

class OPlantAdapter(context: Context):RecyclerArrayAdapter<ListBean>(context){
    var onSelectListener:IOnSelectListener<ListBean>? = null

    init {
        setOnItemClickListener {
            allData.forEach { it.isSelect = false }
            val item = allData[it]
            item.isSelect = true
            notifyDataSetChanged()
            onSelectListener?.onSelect(item)
        }
    }

    override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<*> {
        return ViewHolder(parent!!)
    }

    inner class ViewHolder(viewGroup: ViewGroup) : BaseViewHolder<ListBean>(viewGroup,R.layout.item_oplant_layout){
        val imageView:ImageView = `$`(R.id.imageView)
        override fun setData(data: ListBean?) {
            super.setData(data)
            data?.let {
                Glide.with(context).load(it.pic1)
                        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                        .into(imageView)
                if(it.isSelect)imageView.setBackgroundResource(R.drawable.select_border)
                else imageView.setBackgroundResource(android.R.color.transparent)
            }
        }
    }
}