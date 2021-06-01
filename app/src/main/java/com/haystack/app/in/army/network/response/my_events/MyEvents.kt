package com.haystack.app.`in`.army.network.response.my_events

data class MyEvents(
    val data: List<MyEventsData>,
    val message: String,
    val status: String
)