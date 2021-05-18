package com.android.app.`in`.haystack.network.response.login

import com.android.app.`in`.haystack.network.response.login.Data

data class LogIn(
    val data: Data,
    val lognied_User: String,
    val message: String,
    val status: String
)