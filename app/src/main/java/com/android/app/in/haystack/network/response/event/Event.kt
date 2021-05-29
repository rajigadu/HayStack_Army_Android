package com.android.app.`in`.haystack.network.response.event

import android.net.Uri
import java.io.File
import java.io.Serializable


data class Event(
    var event_name: String,
    var event_description: String,
    var streetaddress: String,
    var image: Uri,
    var city: String,
    var id: String,
    var state: String,
    var zipcode: String,
    var startdate: String,
    var starttime: String,
    var enddate: String,
    var endtime: String,
    var hostname: String,
    var contactinfo: String,
    var hosttype: String,
    var eventtype: String,
    var country: String,
    var latitude: String,
    var longitude: String,
    var category: String,
    var allmembers: ArrayList<AllMembers>
): Serializable{
    constructor():this("","","",Uri.EMPTY,"", "", "","","", "",
    "", "", "", "", "","", "", "", "",
        "", arrayListOf()
    )
}
