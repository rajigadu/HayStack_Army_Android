package com.android.app.`in`.haystack.network.response.event

import java.io.Serializable


data class Event(
    var eventName: String,
    var streetAddress: String,
    var city: String,
    var id: String,
    var state: String,
    var zipCode: String,
    var startDate: String,
    var startTime: String,
    var endDate: String,
    var endTime: String,
    var hostName: String,
    var contactInfo: String,
    var hostType: String,
    var eventType: String,
    var country: String,
    var latitude: String,
    var longitude: String,
    var category: String,
    var allmembers: ArrayList<AllMembers>
): Serializable{
    constructor():this("","","", "", "","","", "",
    "", "", "", "", "","", "", "", "",
        "", arrayListOf())

    /*constructor(
        eventName: String,
        streetAddress: String,
        city: String,
        id: String,
        state: String,
        zipCode: String,
        startDate: String,
        startTime: String,
        endDate: String,
        endTime: String,
        hostName: String,
        contactInfo: String,
        contactInfo2: String,
    ):this("", "", "", "", "", "", "",
        "", "", "", "","", "")*/
}
