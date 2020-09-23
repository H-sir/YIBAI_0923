package com.ybw.yibai.common.adapter

import android.content.Context
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.ybw.yibai.R
import com.ybw.yibai.common.bean.SceneInfo

class SOAdapter(context:Context) : RecyclerArrayAdapter<SceneInfo>(context){
    override fun OnCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<*> {
        return ViewHolder(parent!!)
    }

    override fun addAll(collection: MutableCollection<out SceneInfo>?) {
        super.addAll(collection)
        allData?.forEach {
            it.isSelectCheckBox = false
        }
        allData?.get(count - 1)?.isSelectCheckBox = true

        notifyDataSetChanged()
    }

    inner class ViewHolder(viewGroup: ViewGroup) : BaseViewHolder<SceneInfo>(viewGroup,
            R.layout.item_scene_operate){
        val container:View = `$`(R.id.container)
        val title:TextView = `$`(R.id.titleTv)
        val count:TextView = `$`(R.id.countTv)

        override fun setData(data: SceneInfo?) {
            super.setData(data)
            data?.let{
                title.text = Html.fromHtml(it.sceneName)
                count.text = "${it.count}"
                container.setBackgroundResource(if(it.isEditScene)R.color.trans_grey else R.color.white)
            }
        }
    }
}