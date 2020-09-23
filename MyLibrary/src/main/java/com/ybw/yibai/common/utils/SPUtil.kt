package com.ybw.yibai.common.utils

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson

object SPUtil {
    fun getEditor(cxt:Context):SharedPreferences.Editor{
        return cxt.getSharedPreferences("CONFIG_SHARED_PREFERENCES_NAME", AppCompatActivity.MODE_PRIVATE).edit()
    }

    fun getShared(cxt:Context):SharedPreferences{
        return cxt.getSharedPreferences("CONFIG_SHARED_PREFERENCES_NAME", AppCompatActivity.MODE_PRIVATE)
    }

    fun getShared(cxt:Context,name:String):SharedPreferences{
        return cxt.getSharedPreferences(name, AppCompatActivity.MODE_PRIVATE)
    }

    fun remove(cxt: Context,key:String){
        val editor = getEditor(cxt)
        editor.remove(key)
        editor.apply()
        editor.commit()
    }

    fun clear(cxt:Context){
        val editor = getEditor(cxt)
        editor.clear()
        editor.apply()
        editor.commit()
    }

    fun<T> getValue(cxt:Context,key:String,classOfT:Class<T>):T?{
        val sp = cxt.getSharedPreferences("CONFIG_SHARED_PREFERENCES_NAME", AppCompatActivity.MODE_PRIVATE)
        val value:String? = sp.getString(key,"")
        return if(value != null)Gson().fromJson(value,classOfT) else null
    }

    fun putValue(cxt: Context,key:String,value:Any?){
        val editor = getEditor(cxt)
        val content = Gson().toJson(value)
        editor.putString(key,content)
        editor.apply()
        editor.commit()
    }
}