package com.haystack.app.`in`.army.network.response.search_events

data class SearchEvents(
    val data: List<SearchEventsData>,
    val status: String,
    val message: String
)