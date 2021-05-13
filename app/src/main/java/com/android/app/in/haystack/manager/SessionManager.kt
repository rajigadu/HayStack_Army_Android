package com.android.app.`in`.haystack.manager

import android.content.Context
import android.content.SharedPreferences

class SessionManager constructor(val context: Context) {



    val sPreference: SharedPreferences
        get() = context.getSharedPreferences("${context.packageName}.session", Context.MODE_PRIVATE)

















    companion object {

        private var mInstance: SessionManager? = null

        fun init(context: Context?) {
            mInstance = SessionManager(context!!)
        }

        val instance: SessionManager
            get() {
                if (mInstance == null)
                    throw RuntimeException("Initialize SessionManager")

                return mInstance as SessionManager
            }
    }
}