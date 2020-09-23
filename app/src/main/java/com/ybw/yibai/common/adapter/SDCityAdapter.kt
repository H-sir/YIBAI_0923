package com.ybw.yibai.common.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.ybw.yibai.R
import com.ybw.yibai.common.model.SCTModel
import com.ybw.yibai.common.network.response.CityItemResponse

class SDCityAdapter(context: Context) : RecyclerArrayAdapter<CityItemResponse>(context){
    override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<CityItemResponse> {
        return ViewHolder(parent!!)
    }

    class ViewHolder(viewGroup: ViewGroup):BaseViewHolder<CityItemResponse>(viewGroup, R.layout.item_sct_layout){
        val name:TextView = `$`(R.id.nameTv)
        override fun setData(data: CityItemResponse?) {
            super.setData(data)
            data?.let{
                name.text = it.RegionName
            }
        }
    }
}