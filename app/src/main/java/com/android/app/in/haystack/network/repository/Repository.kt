package com.android.app.`in`.haystack.network.repository

import com.android.app.`in`.haystack.network.ApiClient
import com.android.app.`in`.haystack.network.ApiInterface

object Repository {

    private val client by lazy { ApiClient.retrofitService }
    private val authClient by lazy {
        ApiClient.createBearerAuthService(
            ApiInterface::class.java, false)
    }
}