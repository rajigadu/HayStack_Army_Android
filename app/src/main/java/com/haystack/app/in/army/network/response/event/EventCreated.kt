package com.haystack.app.`in`.army.network.response.event

data class EventCreated(
    val data: Data,
    val eventid: Int,
    val message: String,
    val status: String
)