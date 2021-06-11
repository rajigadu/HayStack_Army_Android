package com.haystack.app.`in`.army.network.response.event

import java.io.Serializable

data class AllMembers(
    var member: String,
    var email: String,
    var number: String
): Serializable
