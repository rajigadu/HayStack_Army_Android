package com.haystack.app.`in`.army.network.response.near_events

data class NearEvents(
    val data: List<NearEventsData>,
    val message: String,
    val status: String
)