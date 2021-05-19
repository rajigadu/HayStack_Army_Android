package com.android.app.`in`.haystack.manager

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.android.app.`in`.haystack.network.response.login.LogIn
import com.android.app.`in`.haystack.utils.AppConstants.DOD_ID
import com.android.app.`in`.haystack.utils.AppConstants.F_NAME
import com.android.app.`in`.haystack.utils.AppConstants.GOVT_EMAIL
import com.android.app.`in`.haystack.utils.AppConstants.LOGNIED_USER
import com.android.app.`in`.haystack.utils.AppConstants.L_NAME
import com.android.app.`in`.haystack.utils.AppConstants.UID
import com.android.app.`in`.haystack.utils.AppConstants.USER_ID
import com.android.app.`in`.haystack.utils.AppConstants.USER_NAME

class SessionManager constructor(val context: Context) {


    val sPreference: SharedPreferences
        get() = context.getSharedPreferences("${context.packageName}.session", Context.MODE_PRIVATE)







    fun getLoginUser(): String = sPreference.getString(LOGNIED_USER, "")!!

    fun getUserId(): String = sPreference.getString(USER_ID, "")!!

    fun saveUserCredentials(login: LogIn) {
        val editor = sPreference.edit()

        editor.putString(USER_NAME, login.data.soldier.username)
        editor.putString(F_NAME, login.data.soldier.fname)
        editor.putString(L_NAME, login.data.soldier.lname)
        editor.putString(USER_ID, login.data.soldier.id)
        editor.putString(GOVT_EMAIL, login.data.soldier.govt_email)
        editor.putString(LOGNIED_USER, login.lognied_User)
        editor.putString(DOD_ID, login.data.soldier.dod_id)

        editor.apply()
    }

    fun clearSessionData() = sPreference.edit().clear().apply()

    fun saveUid(uid: String) = sPreference.edit().putString(UID, uid).apply()


    companion object {

        @SuppressLint("StaticFieldLeak")
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