package com.haystack.app.`in`.army.manager

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.android.gms.maps.model.LatLng
import com.haystack.app.`in`.army.network.response.login.LogIn
import com.haystack.app.`in`.army.utils.AppConstants.DOD_ID
import com.haystack.app.`in`.army.utils.AppConstants.FB_TOKEN
import com.haystack.app.`in`.army.utils.AppConstants.F_NAME
import com.haystack.app.`in`.army.utils.AppConstants.GOVT_EMAIL
import com.haystack.app.`in`.army.utils.AppConstants.LOGNIED_USER
import com.haystack.app.`in`.army.utils.AppConstants.L_NAME
import com.haystack.app.`in`.army.utils.AppConstants.UID
import com.haystack.app.`in`.army.utils.AppConstants.USER_ID
import com.haystack.app.`in`.army.utils.AppConstants.USER_LATITUDE
import com.haystack.app.`in`.army.utils.AppConstants.USER_LONGITUDE
import com.haystack.app.`in`.army.utils.AppConstants.USER_NAME

class SessionManager constructor(val context: Context) {


    val sPreference: SharedPreferences
        get() = context.getSharedPreferences("${context.packageName}.session", Context.MODE_PRIVATE)







    fun getUserLatLng(): LatLng = LatLng(
            sPreference.getString(USER_LATITUDE, "")!!.toDouble(),
            sPreference.getString(USER_LONGITUDE, "")!!.toDouble()
        )

    fun getLoginUser(): String = sPreference.getString(LOGNIED_USER, "")!!

    fun getUserId(): String = sPreference.getString(USER_ID, "")!!

    fun getUserToken(): String = sPreference.getString(FB_TOKEN, "")!!

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

    fun saveUserLatLong(latitude: Double, longitude: Double) {
        val editor = sPreference.edit()
        editor.putString(USER_LATITUDE, latitude.toString())
        editor.putString(USER_LONGITUDE, longitude.toString())
        editor.apply()
    }

    fun saveUserFbToken(token: String) {
        val editor = sPreference.edit()
        editor.putString(FB_TOKEN, token)
        editor.apply()
    }


    companion object {

        @SuppressLint("StaticFieldLeak")
        private var mInstance: com.haystack.app.`in`.army.manager.SessionManager? = null

        fun init(context: Context?) {
            com.haystack.app.`in`.army.manager.SessionManager.Companion.mInstance =
                com.haystack.app.`in`.army.manager.SessionManager(context!!)
        }

        val instance: com.haystack.app.`in`.army.manager.SessionManager
            get() {
                if (com.haystack.app.`in`.army.manager.SessionManager.Companion.mInstance == null)
                    throw RuntimeException("Initialize SessionManager")

                return com.haystack.app.`in`.army.manager.SessionManager.Companion.mInstance as com.haystack.app.`in`.army.manager.SessionManager
            }
    }
}