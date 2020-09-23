package com.ybw.yibai.common.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.ybw.yibai.R
import com.ybw.yibai.common.callback.IOnDeleteListener
import com.ybw.yibai.common.model.ItemDesignSceneModel

class FormulaCCAdapter(context:Context) : RecyclerArrayAdapter<ItemDesignSceneModel.ItemImgModel>(context){
    var onDeletePicListener: IOnDeleteListener<ItemDesignSceneModel.ItemImgModel>? = null

    override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<*> {
        return ViewHolder(parent!!)
    }

    inner class ViewHolder(viewGroup: ViewGroup):BaseViewHolder<ItemDesignSceneModel.ItemImgModel>(viewGroup, R.layout.item_formula_cc){
        val imageView:ImageView = `$`(R.id.imageView)
        val btnDelete:View = `$`(R.id.btnDelete)

        override fun setData(data: ItemDesignSceneModel.ItemImgModel?) {
            super.setData(data)
            data?.let{
                Glide.with(context).load(it.pic).into(imageView)
                btnDelete.tag = it
                btnDelete.setOnClickListener {
                    val item = it.tag as ItemDesignSceneModel.ItemImgModel
                    val index = allData.indexOf(item)
                    onDeletePicListener?.onDelete(index,item)
                }
            }
        }
    }
}