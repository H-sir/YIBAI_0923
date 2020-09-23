package com.ybw.yibai.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.ybw.yibai.R
import com.ybw.yibai.common.adapter.SDCityAdapter
import com.ybw.yibai.common.constants.Preferences.USER_INFO
import com.ybw.yibai.common.network.response.HotCityResponse
import com.ybw.yibai.common.utils.SPUtil
import com.ybw.yibai.presenter.impl.SelectDeliveryCityAPresenterImpl
import com.ybw.yibai.presenter.inter.ISelectDeliveryCityAPresenter
import com.ybw.yibai.view.inter.ISelectDeliveryCityAView
import com.ybw.yibai.common.permissionhelper.PermissionResult
import com.ybw.yibai.common.permissionhelper.utils.PermissionUtils
import com.ybw.yibai.common.utils.LocUtil
import kotlinx.android.synthetic.main.activity_select_delivery_city.*
import kotlinx.android.synthetic.main.partial_base_title.*


class SelectDeliveryCityActivity : BaseActivity<ISelectDeliveryCityAPresenter?>(),
        ISelectDeliveryCityAView {

    private var cityCode:String? = ""

    private lateinit var mAdapter: SDCityAdapter

    override fun onGetPresenter(): ISelectDeliveryCityAPresenter {
        return SelectDeliveryCityAPresenterImpl(this)
    }

    override fun onGetLayoutRes(): Int {
        return R.layout.activity_select_delivery_city
    }

    override fun onBindCtrlInstance() {
        super.onBindCtrlInstance()
        backImageView.setOnClickListener{
            finish()
        }
        btnGetLocation.setOnClickListener {
            askCompactPermissions(arrayOf(
                    PermissionUtils.Manifest_ACCESS_COARSE_LOCATION,
                    PermissionUtils.Manifest_ACCESS_FINE_LOCATION
            ),object: PermissionResult {
                override fun permissionDenied() {
                    onShowToastShort("拒绝此权限将无法使用定位功能！")
                }
                override fun permissionForeverDenied() {
                    onShowToastShort("请先打开定位全县才能使用此功能！")
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
                override fun permissionGranted() {
                    val loc = LocUtil.getLastKnownLocation(onGetContext())
                    presenter?.onGetCurrentCity(loc)
                }
            })
        }
        current_location_text.setOnClickListener {
            val code = it.tag as String
            if(mAdapter.allData.find{ it.Code == code } != null){
                warn_text.visibility = View.GONE
                presenter?.onSetCity(code)
            }else{
                warn_text.visibility = View.VISIBLE
            }
        }
    }

    override fun onFinish() {
        if(cityCode?.isNotEmpty() == true){
            val intent = Intent()
            intent.putExtra("city_code",cityCode)
            setResult(1,intent)
        }
        super.onFinish()
    }

    override fun onInit(savedInstanceState: Bundle?) {
        titleTextView.text = "选择配送城市"
        val curCity = SPUtil.getValue(onGetContext(),"cur_city",String::class.java)
        current_location_text.text = if(curCity.isNullOrBlank())"正在定位..." else curCity
        mAdapter = SDCityAdapter(onGetContext())
        mAdapter.setOnItemClickListener {
            val item = mAdapter.allData[it]
            presenter?.onSetCity(item.Code)
        }
        recyclerView.adapter = mAdapter
        recyclerView.setLayoutManager(GridLayoutManager(this, 3))

        val telephone = SPUtil.getShared(onGetContext(),USER_INFO).getString("telephone","")

        askCompactPermissions(arrayOf(
                PermissionUtils.Manifest_ACCESS_COARSE_LOCATION,
                PermissionUtils.Manifest_ACCESS_FINE_LOCATION
        ),object:PermissionResult{
            override fun permissionDenied() {
                onShowToastShort("拒绝此权限将无法使用定位功能！")
            }
            override fun permissionForeverDenied() {
                onShowToastShort("请先打开定位全县才能使用此功能！")
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            override fun permissionGranted() {
                val loc = LocUtil.getLastKnownLocation(onGetContext())
                presenter?.onGetCurrentCity(loc)
            }
        })

        presenter?.onGetAllCities(telephone!!)
    }

    override fun onGetCurCityCallback(city: String, code: String) {
        current_location_text.text = city
        current_location_text.tag = code
        SPUtil.putValue(onGetContext(),"cur_city",city)
        //onShowToastShort("定位成功,当前城市为$city")
    }

    override fun onGetCitiesCallback(cities: HotCityResponse) {
        mAdapter.clear()
        mAdapter.addAll(cities.hotcity)
    }

    override fun onSetCityCallback(city: String, code: String) {
        cityCode = code
    }
}