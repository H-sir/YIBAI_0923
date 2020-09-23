package com.ybw.yibai.view.activity

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.ybw.yibai.R
import com.ybw.yibai.common.adapter.FormulaAdapter
import com.ybw.yibai.common.callback.IOnDeleteListener
import com.ybw.yibai.common.model.ItemDesignSceneModel
import com.ybw.yibai.common.utils.SPUtil
import com.ybw.yibai.presenter.impl.FormulaCreatorAPresenterImpl
import com.ybw.yibai.presenter.inter.IFormulaCreatorAPresenter
import com.ybw.yibai.view.inter.IFormulaCreatorAView
import kotlinx.android.synthetic.main.activity_formula_creator.*
import kotlinx.android.synthetic.main.item_formula.recyclerView
import kotlinx.android.synthetic.main.partial_base_title.*

class FormulaCreatorActivity : BaseActivity<IFormulaCreatorAPresenter>(), IFormulaCreatorAView {
    companion object {
        val REQUEST_CODE = 1002
    }

    private lateinit var mAdapter: FormulaAdapter
    private var mModel: ItemDesignSceneModel? = null

    override fun onGetPresenter(): IFormulaCreatorAPresenter {
        return FormulaCreatorAPresenterImpl(this)
    }

    override fun onGetLayoutRes(): Int {
        return R.layout.activity_formula_creator
    }

    override fun onBindCtrlInstance() {
        super.onBindCtrlInstance()
        backImageView.setOnClickListener {
            finish()
        }
        // 删除场景
        btnDelDesign.setOnClickListener {
            val number = SPUtil.getValue(onGetContext(), "number", String::class.java) ?: ""
            presenter.onDeleteDesign(number)
        }
        // 完成设计
        btnDone.setOnClickListener {
            presenter.onDoneDesign(mModel?.number ?: "")
        }
    }

    override fun onInit(savedInstanceState: Bundle?) {
        titleTextView.text = "创建设计方案"

        mAdapter = FormulaAdapter(onGetContext())
        // 删除场景被点击
        mAdapter.onDeleteSceneListener = object : IOnDeleteListener<ItemDesignSceneModel.ItemModel> {
            override fun onDelete(index: Int, item: ItemDesignSceneModel.ItemModel) {
                presenter.onDeleteScene(item.scheme_id ?: "")
            }
        }
        mAdapter.onDeletePicListener = object:IOnDeleteListener<ItemDesignSceneModel.ItemImgModel>{
            override fun onDelete(index: Int, item: ItemDesignSceneModel.ItemImgModel) {
                presenter.onDeleteSchemePic(item.id ?: "")
            }
        }

        recyclerView.setLayoutManager(LinearLayoutManager(onGetContext()))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            recyclerView.isNestedScrollingEnabled = false
        }
        recyclerView.adapter = mAdapter

        val number = SPUtil.getValue(onGetContext(), "number", String::class.java) ?: ""
        presenter.onGetDesignDetail(number)
    }

    override fun onGetDesignListCallback(model: ItemDesignSceneModel?) {
        mModel = model
        mAdapter.clear()
        mAdapter.add(model)
    }

    override fun onDeleteDesignCallback(any: Any) {
        setResult(1)
        finish()
    }

    override fun onDeleteSceneCallback(schemeId: String) {
        if(mAdapter.count > 0) {
            val item = mAdapter.getItem(0)
            item.schemelist?.find { it.scheme_id == schemeId }
                    ?.let {
                        item?.schemelist?.remove(it)
                    }
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun onDeleteSchemePic(id: String) {
        if(mAdapter.count > 0) {
            val item = mAdapter.getItem(0)
            item.schemelist?.forEach {
                it.imglist?.find { _it -> _it.id == id }?.let{ __it ->
                    it.imglist?.remove(__it)
                }
            }
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun onGetActivity(): Activity {
        return this@FormulaCreatorActivity
    }

    override fun onDelSchemeId() {
        SPUtil.remove(onGetContext(),"number")
        setResult(3)
        finish()
    }
}