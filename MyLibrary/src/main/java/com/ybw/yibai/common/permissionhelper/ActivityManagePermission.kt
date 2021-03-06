/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Rurio Luca
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ybw.yibai.common.permissionhelper

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import java.util.*

/**
 * Created by luca on 2/29/16.
 */
open class ActivityManagePermission : AppCompatActivity() {
    private var permissionResult: PermissionResult? = null
    private lateinit var permissionsAsk: Array<String>
    /**
     * @param context    current Context
     * @param permission String permission to ask
     * @return boolean true/false
     */
    fun isPermissionGranted(context: Context?, permission: String?): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(context!!, permission!!) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * @param context     current Context
     * @param permissions String[] permission to ask
     * @return boolean true/false
     */
    fun isPermissionsGranted(context: Context?, permissions: Array<String?>): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(context!!, permission!!) != PackageManager.PERMISSION_GRANTED) return false
        }
        return true
    }

    private fun internalRequestPermission(permissionAsk: Array<String>) {
        var arrayPermissionNotGranted: Array<String?>
        val permissionsNotGranted = ArrayList<String>()
        for (s in permissionAsk) {
            if (!isPermissionGranted(this@ActivityManagePermission, s)) {
                permissionsNotGranted.add(s)
            }
        }
        if (permissionsNotGranted.isEmpty()) {
            if (permissionResult != null) permissionResult!!.permissionGranted()
        } else {
            arrayPermissionNotGranted = arrayOfNulls(permissionsNotGranted.size)
            arrayPermissionNotGranted = permissionsNotGranted.toArray(arrayPermissionNotGranted)
            ActivityCompat.requestPermissions(this@ActivityManagePermission, arrayPermissionNotGranted, KEY_PERMISSION)
        }
    }

    // we need to add super method call in onRequestPermissionsResult to get PermissionResult callbacks on child fragment
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != KEY_PERMISSION) {
            return
        }
        val permissionDenied: MutableList<String> = LinkedList()
        var granted = true
        for (i in grantResults.indices) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                granted = false
                permissionDenied.add(permissions[i])
            }
        }
        if (permissionResult != null) {
            if (granted) {
                permissionResult!!.permissionGranted()
            } else {
                for (s in permissionDenied) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, s)) {
                        permissionResult!!.permissionForeverDenied()
                        return
                    }
                }
                permissionResult!!.permissionDenied()
            }
        }
    }

    /**
     * @param permission       String permission ask
     * @param permissionResult callback PermissionResult
     */
    fun askCompactPermission(permission: String, permissionResult: PermissionResult?) {
        permissionsAsk = arrayOf(permission)
        this.permissionResult = permissionResult
        internalRequestPermission(permissionsAsk)
    }

    /**
     * @param permissions      String[] permissions ask
     * @param permissionResult callback PermissionResult
     */
    fun askCompactPermissions(permissions: Array<String>, permissionResult: PermissionResult?) {
        permissionsAsk = permissions
        this.permissionResult = permissionResult
        internalRequestPermission(permissionsAsk)
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    fun openSettingsApp(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + context.packageName)
        startActivity(intent)
    }

    companion object {
        private const val KEY_PERMISSION = 200
    }
}