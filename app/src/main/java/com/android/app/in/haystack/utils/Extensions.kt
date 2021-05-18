package com.android.app.`in`.haystack.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.android.app.`in`.haystack.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

object Extensions {




    fun showSnackBar(view: View, message: String) = Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()

    fun View.hideKeyboard() {
        val inputMethodManager = context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    fun showAlertDialog(title: String, context: Context, message: String?){

        val dialog = MaterialAlertDialogBuilder(context, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Ok") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .create()
        if (dialog.window != null)
            dialog.window?.attributes?.windowAnimations = R.style.SlidingDialogAnimation

        dialog.show()
    }

    fun showSnackBarSettings(
        context: Context,
        mainTextStringId: String, actionStringId: String,
        listener: View.OnClickListener) {
        Toast.makeText(context, mainTextStringId, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("HardwareIds")
    fun getDeviceUid(context: Context): String{
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    @SuppressLint("SimpleDateFormat")
    fun getUniqueRandomNumber(): String{
        var random = ""
        val sdf = SimpleDateFormat("yyyyMMdd")
        var currentDate = sdf.format(Date())

        random = currentDate + Random().nextInt(1000000000).toString()

        return random
    }

    fun shortSnackBar(message: String, view: View) = Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}