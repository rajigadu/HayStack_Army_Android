package com.haystack.app.`in`.army.utils

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.haystack.app.`in`.army.R
import retrofit2.HttpException
import java.io.IOException
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

    @SuppressLint("SimpleDateFormat")
    fun convertedDateFormat(date: String?): String{
        var formattedDate: Date? = null
        var convertDate: String? = null
        val sdf = SimpleDateFormat("dd MMM yyyy")
        try {

            formattedDate = sdf.parse(date!!)
            convertDate = SimpleDateFormat("MM-dd-yyyy").format(formattedDate!!)

        }catch (e: Exception){e.printStackTrace()}
        return convertDate!!
    }

    @SuppressLint("HardwareIds")
    fun getDeviceUid(context: Context): String{
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("MM-dd-yyyy")
        return sdf.format(Date())
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentTime(): String {
        var timeState = "AM"
        val calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR_OF_DAY).toString()
        var minute = calendar.get(Calendar.MINUTE).toString()

        if (hour.toInt() > 11) timeState = "PM"
        if (hour.toInt() > 12) hour = (hour.toInt() -12).toString()

        if (hour.length == 1) hour = "0$hour"
        if (minute.length == 1) minute = "0$minute"
        if (hour == "00") hour = "12"

        return "$hour:$minute $timeState"
    }

    @SuppressLint("SimpleDateFormat")
    fun getUniqueRandomNumber(): String{
        var random = ""
        val sdf = SimpleDateFormat("yyyyMMdd")
        val currentDate = sdf.format(Date())

        random = currentDate + Random().nextInt(1000000000).toString()

        return random
    }

    fun longSnackBar(message: String, view: View) = Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()

    fun showErrorResponse(throwable: Throwable, constraintLogin: View) {
        when(throwable){
            is IOException -> {
                longSnackBar("Network Error, Please check your Internet", constraintLogin)
            }
            is HttpException -> {
                val code = throwable.code()
                Log.e("TAG", "code: $code")
                longSnackBar("Some Error occurred, $code!", constraintLogin)
            }
            else -> {
                Log.e("TAG", "error: "+throwable.localizedMessage)
                longSnackBar("Something went wrong, please try again", constraintLogin)
            }
        }
    }

    fun getRealPathUri(context: Context, imageUri: Uri): String?{
        var result: String? = null
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        try {
            val cursor: Cursor = context.contentResolver.query(
                imageUri,
                filePathColumn, null, null, null)!!
            cursor.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            result = cursor.getString(columnIndex)
            cursor.close()
        }catch (e: java.lang.Exception){}
        Log.e("TAG", "real path uri: $result")
        return result
    }
}