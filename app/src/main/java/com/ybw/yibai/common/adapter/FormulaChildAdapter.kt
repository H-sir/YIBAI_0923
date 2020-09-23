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

class FormulaChildAdapter(context: Context) : RecyclerArrayAdapter<ItemDesignSceneModel.ItemModel>(context){
    var onDeleteListener: IOnDeleteListener<ItemDesignSceneModel.ItemModel>? = null
    var onDeletePicListener:IOnDeleteListener<ItemDesignSceneModel.ItemImgModel>? = null
    init {
        setOnItemClickListener {
            val item = allData[it]
            onDeleteListener?.onDelete(it,item)
        }
    }

    override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<*> {
        return ViewHolder(parent!!)
    }

    inner class ViewHolder(viewGroup: ViewGroup)
        : BaseViewHolder<ItemDesignSceneModel.ItemModel>(viewGroup, R.layout.item_child_formula){
        val title:TextView = `$`(R.id.titleTv)
        val btnDelete:TextView = `$`(R.id.btnDelete)
        val recyclerView:EasyRecyclerView = `$`(R.id.recyclerView)

        override fun setData(data: ItemDesignSceneModel.ItemModel?) {
            super.setData(data)
            data?.let{
                title.text = it.scheme_name
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    recyclerView.isNestedScrollingEnabled = false
                }
                recyclerView.setLayoutManager(LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false))
                val adapter = FormulaCCAdapter(context)
                adapter.onDeletePicListener = onDeletePicListener
                adapter.addAll(it.imglist)
                recyclerView.adapter = adapter

                btnDelete.tag = it
                btnDelete.setOnClickListener {
                    val item:ItemDesignSceneModel.ItemModel = it.tag as ItemDesignSceneModel.ItemModel
                    val index = allData.indexOf(item)
                    onDeleteListener?.onDelete(index,item)
                }
            }
        }
    }
}