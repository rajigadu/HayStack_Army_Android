package com.android.app.`in`.haystack.network.repository

import android.content.Context
import android.util.Log
import com.android.app.`in`.haystack.manager.SessionManager
import com.android.app.`in`.haystack.network.ApiClient
import com.android.app.`in`.haystack.network.ApiInterface
import com.android.app.`in`.haystack.network.response.all_groups.AllGroups
import com.android.app.`in`.haystack.network.response.attend_events.AttendEvents
import com.android.app.`in`.haystack.network.response.categories.AllCategories
import com.android.app.`in`.haystack.network.response.countries.Countries
import com.android.app.`in`.haystack.network.response.create_group.Group
import com.android.app.`in`.haystack.network.response.event.Event
import com.android.app.`in`.haystack.network.response.event.EventCreated
import com.android.app.`in`.haystack.network.response.group_members.DefaultResponse
import com.android.app.`in`.haystack.network.response.group_members.GroupMembers
import com.android.app.`in`.haystack.network.response.interest_events.InterestEvents
import com.android.app.`in`.haystack.network.response.login.LogIn
import com.android.app.`in`.haystack.network.response.my_events.MyEvents
import com.android.app.`in`.haystack.network.response.soldier_signup.SignUpResponse
import com.android.app.`in`.haystack.network.response.states.States
import com.android.app.`in`.haystack.utils.AppConstants.DEVICE_TYPE
import com.android.app.`in`.haystack.utils.Extensions.getRealPathUri
import com.android.app.`in`.haystack.utils.Extensions.getUniqueRandomNumber
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

object Repository {

    private val client by lazy { ApiClient.retrofitService }
    private val authClient by lazy {
        ApiClient.createBearerAuthService(
            ApiInterface::class.java, false)
    }



    fun soldierRegistration(fName: String, lName: String, email: String,
                            userName: String, password: String, dodId: String, deviceId: String): Call<SignUpResponse> {
        val deviceType = DEVICE_TYPE
        val deviceToken = getUniqueRandomNumber()
        return client.soldierRegistration(fName, lName, email, dodId, userName, password, deviceType, deviceId, deviceToken)
    }

    fun spouseRegistration(fName: String, lName: String, email: String, userName: String,
                           password: String, sponsorsEmail: String, relation: String, deviceId: String)
    : Call<SignUpResponse> {
        val deviceType = DEVICE_TYPE
        val deviceToken = getUniqueRandomNumber()
        return client.spouseRegistration(fName, lName, email, userName, password, sponsorsEmail, relation, deviceType, deviceId, deviceToken)
    }

    fun userLogIn(userName: String, password: String, deviceId: String): Call<LogIn> {
        val deviceType = DEVICE_TYPE
        val deviceToken = getUniqueRandomNumber()
        return client.userLogIn(userName, password, deviceType, deviceId, deviceToken)
    }

    fun changePassword(oldPassword: String, newPassword: String, userId: String): Call<DefaultResponse>{
        val loginUser = SessionManager.instance.getLoginUser()
        return client.changePassword(oldPassword, newPassword, userId, loginUser)
    }

    fun forgotPassword(email: String): Call<DefaultResponse> = client.forgotPassword(email)

    fun editProfile(firstName: String, lastName: String, userName: String, logniedUser: String, userId: String):
            Call<DefaultResponse>{
        return client.editUserProfile(firstName,lastName, userName, logniedUser, userId)
    }

    fun createNewGroup(groupName: String, groupDesc: String, userId: String): Call<Group>{
        return client.createGroup(groupName, groupDesc, userId)
    }

    fun contactUs(fullName: String, email: String, message: String) = client.contactUs(fullName, email, message)

    fun getAllGroupsList(id: String): Call<AllGroups> = client.getAllGroups(id)

    fun editGroup(groupName: String, groupDesc: String, groupId: String, userId: String): Call<DefaultResponse> =
        client.editGroup(groupName, groupDesc, groupId, userId)

    fun addMemberToGroup(groupId: String, userId: String, member: String, number: String, email: String):
            Call<DefaultResponse> = client.addMemberToGroup(groupId, userId, member, number, email)

    fun getGroupMembers(groupId: String, userId: String): Call<GroupMembers> = client.getGroupMembers(groupId, userId)

    fun editGroupMember(groupId: String, member: String, number: String, email: String, userId: String) =
        client.editGroupMember(groupId, member, number, email, userId)

    fun deleteGroupMember(groupId: String, memberId: String, userId: String): Call<DefaultResponse> =
        client.deleteGroupMember(groupId, userId, memberId)

    fun deleteGroup(groupId: String, userId: String): Call< DefaultResponse> = client.deleteGroup(groupId, userId)

    fun getAllCategories(): Call<AllCategories> = client.getAllCategories()

    fun createNewEvent(event: Event, requireContext: Context): Call<EventCreated> {
        val multipartMap = HashMap<String, RequestBody>()

        val rqEventName = RequestBody.create(MediaType.parse("text/plain"), event.event_name)
        val rqEventDesc = RequestBody.create(MediaType.parse("text/plain"), event.event_description)
        val rqStreetAddress = RequestBody.create(MediaType.parse("text/plain"), event.streetaddress)
        val rqUserId = RequestBody.create(MediaType.parse("text/plain"), event.id)
        val rqState = RequestBody.create(MediaType.parse("text/plain"), event.state)
        val rqZipCode = RequestBody.create(MediaType.parse("text/plain"), event.zipcode)
        val rqCity = RequestBody.create(MediaType.parse("text/plain"), event.city)
        val rqStartDate = RequestBody.create(MediaType.parse("text/plain"), event.startdate)
        val rqStartTime = RequestBody.create(MediaType.parse("text/plain"), event.starttime)
        val rqEndDate = RequestBody.create(MediaType.parse("text/plain"), event.enddate)
        val rqEndTime = RequestBody.create(MediaType.parse("text/plain"), event.endtime)
        val rqHostName = RequestBody.create(MediaType.parse("text/plain"), event.hostname)
        val rqContactInfo = RequestBody.create(MediaType.parse("text/plain"), event.contactinfo)
        val rqHostType = RequestBody.create(MediaType.parse("text/plain"), event.hosttype)
        val rqEventType = RequestBody.create(MediaType.parse("text/plain"), event.eventtype)
        val rqCountry = RequestBody.create(MediaType.parse("text/plain"), event.country)
        val rqLatitude = RequestBody.create(MediaType.parse("text/plain"), event.latitude)
        val rqLongitude = RequestBody.create(MediaType.parse("text/plain"), event.longitude)
        val rqCategory = RequestBody.create(MediaType.parse("text/plain"), event.category)

        var rqImage: RequestBody? = null

        Log.e("TAG","uri: "+event.image)
        if (event.image != null){
            val file = File(getRealPathUri(requireContext, event.image))
            rqImage = RequestBody.create(MediaType.parse("application/octet-stream"), file)
        }


        /*multipartMap["event_name"] = event.event_name
        multipartMap["event_description"] = event.event_description
        multipartMap["streetaddress"] = event.streetaddress
        multipartMap["id"] = event.id
        multipartMap["state"] = event.state
        multipartMap["zipcode"] = event.zipcode
        multipartMap["startdate"] = event.startdate
        multipartMap["starttime"] = event.starttime
        multipartMap["enddate"] = event.enddate
        multipartMap["endtime"] = event.endtime
        multipartMap["hostname"] = event.hostname
        multipartMap["contactinfo"] = event.contactinfo
        multipartMap["hosttype"] = event.hosttype
        multipartMap["eventtype"] = event.eventtype
        multipartMap["country"] = event.country
        multipartMap["latitude"] = event.latitude
        multipartMap["longitude"] = event.longitude
        multipartMap["category"] = event.category

        //multipartMap["image"] = image

        for (elements in 0 until event.allmembers.size){
            multipartMap["allmembers[$elements][member]"] = event.allmembers[elements].member
            multipartMap["allmembers[$elements][email]"] = event.allmembers[elements].email
            multipartMap["allmembers[$elements][number]"] = event.allmembers[elements].number
        }*/

        multipartMap["event_name"] = rqEventName
        multipartMap["event_description"] = rqEventDesc
        multipartMap["streetaddress"] = rqStreetAddress
        multipartMap["id"] = rqUserId
        multipartMap["state"] = rqState
        multipartMap["city"] = rqCity
        multipartMap["zipcode"] = rqZipCode
        multipartMap["startdate"] = rqStartDate
        multipartMap["starttime"] = rqStartTime
        multipartMap["enddate"] = rqEndDate
        multipartMap["endtime"] = rqEndTime
        multipartMap["hostname"] = rqHostName
        multipartMap["contactinfo"] = rqContactInfo
        multipartMap["hosttype"] = rqHostType
        multipartMap["eventtype"] = rqEventType
        multipartMap["country"] = rqCountry
        multipartMap["latitude"] = rqLatitude
        multipartMap["longitude"] = rqLongitude
        multipartMap["category"] = rqCategory
        multipartMap["image"] = rqImage!!

        for (elements in 0 until event.allmembers.size){
            multipartMap["allmembers[$elements][member]"] = RequestBody.create(MediaType.parse("text/plain"), event.allmembers[elements].member)
            multipartMap["allmembers[$elements][email]"] = RequestBody.create(MediaType.parse("text/plain"), event.allmembers[elements].email)
            multipartMap["allmembers[$elements][number]"] = RequestBody.create(MediaType.parse("text/plain"), event.allmembers[elements].number)
        }

        Log.e("TAG", "multipart: $multipartMap")

        return client.createNewEvent(multipartMap)
    }

    fun getAllCountries(): Call<Countries> = client.getCountries()

    fun getAllStatesOfTheCountry(countryName: String): Call<States> = client.getStates(countryName)

    fun getAttendEvents(currentDate: String, endTime: String): Call<AttendEvents> = client.attendEvents(
        SessionManager.instance.getUserId(),
        currentDate, endTime
    )

    fun getInterestEvents(currentDate: String, endTime: String): Call<InterestEvents> = client.interestEvents(
        SessionManager.instance.getUserId(),
        currentDate, endTime
    )

    fun getMyEvents(currentDate: String, endTime: String?): Call<MyEvents> {
        val userId = SessionManager.instance.getUserId()
        return client.myEvents(userId, currentDate, endTime!!)
    }
}