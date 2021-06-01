package com.haystack.app.`in`.army.network.response.nearest_events

data class NearestEvents(
    val data: List<NearestEventData>,
    val message: String,
    val status: String
)