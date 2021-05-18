package com.android.app.`in`.haystack.network.response.group_members

import com.android.app.`in`.haystack.network.response.group_members.Data

data class GroupMembers(
    val data: List<Data>,
    val message: String,
    val status: String
)