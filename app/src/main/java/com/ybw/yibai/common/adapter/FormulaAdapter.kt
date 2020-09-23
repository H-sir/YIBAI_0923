package com.ybw.yibai.common.adapter

import android.content.Context
import android.os.Build
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import android.widget.TextView
import com.jude.easyrecyclerview.EasyRecyclerView
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.ybw.yibai.R
import com.ybw.yibai.common.callback.IOnDeleteListener
import com.ybw.yibai.common.model.ItemDesignSceneModel

class FormulaAdapter(context: Context) : RecyclerArrayAdapter<ItemDesignSceneModel>(context){
    var onDeleteSceneListener:IOnDeleteListener<ItemDesignSceneModel.ItemModel>? = null
    var onDeletePicListener:IOnDeleteListener<ItemDesignSceneModel.ItemImgModel>? = null

    override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<*> {
        return ViewHolder(parent!!)
    }

    inner class ViewHolder(viewGroup: ViewGroup) : BaseViewHolder<ItemDesignSceneModel>(viewGroup, R.layout.item_formula){
        val number: TextView = `$`(R.id.numberTv)
        val recyclerView:EasyRecyclerView = `$`(R.id.recyclerView)

        override fun setData(data: ItemDesignSceneModel?) {
            super.setData(data)
            data?.let{
                number.text = it.number
                recyclerView.setLayoutManager(LinearLayoutManager(context))
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    recyclerView.isNestedScrollingEnabled = false
                }
                val adapter = FormulaChildAdapter(context)
                adapter.onDeleteListener = onDeleteSceneListener
                adapter.onDeletePicListener = onDeletePicListener
                adapter.addAll(it.schemelist)
                recyclerView.adapter = adapter
            }
        }
    }
}