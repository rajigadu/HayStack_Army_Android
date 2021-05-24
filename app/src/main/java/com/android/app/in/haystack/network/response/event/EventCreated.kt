package com.android.app.`in`.haystack.network.response.event

data class EventCreated(
    val data: Data,
    val eventid: Int,
    val message: String,
    val status: String
)