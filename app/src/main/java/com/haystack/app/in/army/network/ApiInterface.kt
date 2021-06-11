package com.haystack.app.`in`.army.network

import com.haystack.app.`in`.army.network.config.AppConfig.ADD_ATTEND_EVENTS
import com.haystack.app.`in`.army.network.config.AppConfig.ADD_INTEREST_EVENTS
import com.haystack.app.`in`.army.network.config.AppConfig.ADD_MEMBER_GROUP
import com.haystack.app.`in`.army.network.config.AppConfig.ALL_CATEGORIES
import com.haystack.app.`in`.army.network.config.AppConfig.ALL_MEMBERS
import com.haystack.app.`in`.army.network.config.AppConfig.CHANGE_PASSWORD
import com.haystack.app.`in`.army.network.config.AppConfig.CONTACT_US
import com.haystack.app.`in`.army.network.config.AppConfig.CREATE_EVENT
import com.haystack.app.`in`.army.network.config.AppConfig.CREATE_GROUP
import com.haystack.app.`in`.army.network.config.AppConfig.DELETE_GROUP
import com.haystack.app.`in`.army.network.config.AppConfig.DELETE_GROUP_MEMBER
import com.haystack.app.`in`.army.network.config.AppConfig.DELETE_MY_EVENTS
import com.haystack.app.`in`.army.network.config.AppConfig.DELETE_OTHER_EVENTS
import com.haystack.app.`in`.army.network.config.AppConfig.EDIT_EVENT
import com.haystack.app.`in`.army.network.config.AppConfig.EDIT_GROUP
import com.haystack.app.`in`.army.network.config.AppConfig.EDIT_GROUP_MEMBER
import com.haystack.app.`in`.army.network.config.AppConfig.EDIT_PROFILE
import com.haystack.app.`in`.army.network.config.AppConfig.FORGOT_PASSWORD
import com.haystack.app.`in`.army.network.config.AppConfig.GET_ALL_GROUPS
import com.haystack.app.`in`.army.network.config.AppConfig.GROUP_MEMBERS
import com.haystack.app.`in`.army.network.config.AppConfig.LIST_ATTEND_EVENTS
import com.haystack.app.`in`.army.network.config.AppConfig.LIST_COUNTRIES
import com.haystack.app.`in`.army.network.config.AppConfig.LIST_INTEREST_EVENTS
import com.haystack.app.`in`.army.network.config.AppConfig.LIST_STATES
import com.haystack.app.`in`.army.network.config.AppConfig.LOG_IN
import com.haystack.app.`in`.army.network.config.AppConfig.MY_EVENTS
import com.haystack.app.`in`.army.network.config.AppConfig.NEAREST_EVENTS
import com.haystack.app.`in`.army.network.config.AppConfig.NEAR_EVENTS
import com.haystack.app.`in`.army.network.config.AppConfig.SEARCH_EVENTS
import com.haystack.app.`in`.army.network.config.AppConfig.SIGN_UP_SOLDIER
import com.haystack.app.`in`.army.network.config.AppConfig.SIGN_UP_SPOUSE
import com.haystack.app.`in`.army.network.response.add_attend_events.AddAttendEvent
import com.haystack.app.`in`.army.network.response.add_interest_events.AddInterestEvents
import com.haystack.app.`in`.army.network.response.group_members.DefaultResponse
import com.haystack.app.`in`.army.network.response.all_groups.AllGroups
import com.haystack.app.`in`.army.network.response.attend_events.AttendEvents
import com.haystack.app.`in`.army.network.response.categories.AllCategories
import com.haystack.app.`in`.army.network.response.countries.Countries
import com.haystack.app.`in`.army.network.response.create_group.Group
import com.haystack.app.`in`.army.network.response.event.EventCreated
import com.haystack.app.`in`.army.network.response.group_members.GroupMembers
import com.haystack.app.`in`.army.network.response.interest_events.InterestEvents
import com.haystack.app.`in`.army.network.response.login.LogIn
import com.haystack.app.`in`.army.network.response.members.Members
import com.haystack.app.`in`.army.network.response.my_events.MyEvents
import com.haystack.app.`in`.army.network.response.near_events.NearEvents
import com.haystack.app.`in`.army.network.response.nearest_events.NearestEvents
import com.haystack.app.`in`.army.network.response.search_events.SearchEvents
import com.haystack.app.`in`.army.network.response.soldier_signup.SignUpResponse
import com.haystack.app.`in`.army.network.response.states.States
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


    @FormUrlEncoded
    @POST(SIGN_UP_SOLDIER)
    fun soldierRegistration(
        @Field("fname") firstName: String,
        @Field("lname") lastName: String,
        @Field("email") email: String,
        @Field("dod_id") dod_id: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("device_type") deviceType: String,
        @Field("device_id") deviceId: String,
        @Field("device_token") deviceToken: String,
    ): Call<SignUpResponse>

    @FormUrlEncoded
    @POST(SIGN_UP_SPOUSE)
    fun spouseRegistration(
        @Field("fname") firstName: String,
        @Field("lname") lastName: String,
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("sponsors_email") sponsorsEmail: String,
        @Field("relation_to_sm") relationToSm: String,
        @Field("device_type") deviceType: String,
        @Field("device_id") deviceId: String,
        @Field("device_token") deviceToken: String,
    ): Call<SignUpResponse>

    @FormUrlEncoded
    @POST(LOG_IN)
    fun userLogIn(
        @Field("username") email: String?,
        @Field("password") password: String?,
        @Field("device_type") deviceType: String,
        @Field("device_id") deviceId: String,
        @Field("device_token") deviceToken: String,
    ): Call<LogIn>

    @FormUrlEncoded
    @POST(EDIT_PROFILE)
    fun editUserProfile(
        @Field("fname") firstName: String,
        @Field("lname") lastName: String,
        @Field("username") userName: String,
        @Field("lognied_User") logniedUser: String,
        @Field("id") userId: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST(FORGOT_PASSWORD)
    fun forgotPassword(
        @Field("email") email: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST(CHANGE_PASSWORD)
    fun changePassword(
        @Field("oldpassword") oldPassword: String,
        @Field("newpassword") newPassword: String,
        @Field("id") userId: String,
        @Field("lognied_User") loginUser: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST(CONTACT_US)
    fun contactUs(
        @Field("full_name") fullName: String,
        @Field("email") email: String,
        @Field("message") message: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST(CREATE_GROUP)
    fun createGroup(
        @Field("gname") groupName: String,
        @Field("gdesc") groupDesc: String,
        @Field("id") userId: String
    ): Call<Group>

    @FormUrlEncoded
    @POST(ADD_MEMBER_GROUP)
    fun addMemberToGroup(
        @Field("groupid") groupId: String,
        @Field("id") hostId: String,
        @Field("member") memberName: String,
        @Field("number") number: String,
        @Field("email") email: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST(GET_ALL_GROUPS)
    fun getAllGroups(
        @Field("id") userId: String
    ): Call<AllGroups>

    @FormUrlEncoded
    @POST(EDIT_GROUP)
    fun editGroup(
        @Field("gname") groupName: String,
        @Field("gdesc") groupDesc: String,
        @Field("groupid") groupId: String,
        @Field("id") userId: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST(ALL_MEMBERS)
    fun getAllMembers(
        @Field("gname") groupName: String,
        @Field("gdesc") groupDesc: String,
        @Field("groupid") groupId: String,
        @Field("id") userId: String
    ): Call<Members>

    @FormUrlEncoded
    @POST(GROUP_MEMBERS)
    fun getGroupMembers(
        @Field("groupid") groupId: String,
        @Field("id") userId: String
    ): Call<GroupMembers>

    @FormUrlEncoded
    @POST(EDIT_GROUP_MEMBER)
    fun editGroupMember(
        @Field("groupid") groupId: String,
        @Field("member") member: String,
        @Field("number") number: String,
        @Field("email") email: String,
        @Field("id") userId: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST(DELETE_GROUP_MEMBER)
    fun deleteGroupMember(
        @Field("groupid") groupId: String,
        @Field("userid") userId: String,
        @Field("memberid") memberId: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST(DELETE_GROUP)
    fun deleteGroup(
        @Field("groupid") groupId: String,
        @Field("id") userId: String
    ): Call<DefaultResponse>

    @GET(ALL_CATEGORIES)
    fun getAllCategories(): Call<AllCategories>

    @Multipart
    @POST(CREATE_EVENT)
    fun createNewEvent(@PartMap hashMap: HashMap<String, RequestBody>,
                       @Part body: MultipartBody.Part?
    ): Call<EventCreated>

    @Multipart
    @POST(EDIT_EVENT)
    fun editEvent(@PartMap hashMap: HashMap<String, RequestBody>): Call<DefaultResponse>

    @FormUrlEncoded
    @POST(MY_EVENTS)
    fun myEvents(
        @Field("id") userId: String,
        @Field("currentdate") currentDate: String,
        @Field("endtime") endTime: String?
    ): Call<MyEvents>

    @FormUrlEncoded
    @POST(LIST_ATTEND_EVENTS)
    fun attendEvents(
        @Field("id") userId: String,
        @Field("currentdate") currentDate: String,
        @Field("endtime") endtime: String?,
    ): Call<AttendEvents>

    @FormUrlEncoded
    @POST(ADD_ATTEND_EVENTS)
    fun addAttendEvents(
        @Field("eventid") eventId: String,
        @Field("id") userId: String,
        @Field("userid") hostId: String?,
    ): Call<AddAttendEvent>

    @FormUrlEncoded
    @POST(ADD_INTEREST_EVENTS)
    fun addInterestEvents(
        @Field("eventid") eventId: String,
        @Field("id") hostId: String,
        @Field("userid") userId: String?,
    ): Call<AddInterestEvents>

    @FormUrlEncoded
    @POST(NEAREST_EVENTS)
    fun nearestEvents(
        @Field("device_id") deviceId: String,
        @Field("device_type") deviceType: String,
        @Field("device_token") deviceToken: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("category") category: String?,
        @Field("searchtype") searchType: String?,
        @Field("currentdate") currentDate: String?,
        @Field("endtime") endTime: String?,
    ): Call<NearestEvents>

    @FormUrlEncoded
    @POST(SEARCH_EVENTS)
    fun searchEvents(
        @Field("id") userId: String?,
        @Field("searchType") searchType: String?,
        @Field("country") country: String?,
        @Field("state") state: String?,
        @Field("city") city: String?,
        @Field("startdate") startDate: String?,
        @Field("enddate") endDate: String?,
        @Field("starttime") startTime: String?,
        @Field("endtime") endTime: String?,
        @Field("distance_miles") distanceMile: String?,
        @Field("nationwide") nationWide: String?,
        @Field("latitude") latitude: String?,
        @Field("longitude") longitude: String?,
        @Field("category") category: String?,
        @Field("zip") zipcode: String?,
    ): Call<SearchEvents>

    @FormUrlEncoded
    @POST(LIST_INTEREST_EVENTS)
    fun interestEvents(
        @Field("id") userId: String,
        @Field("currentdate") currentDate: String,
        @Field("endtime") endtime: String?,
    ): Call<InterestEvents>

    @GET(LIST_COUNTRIES)
    fun getCountries(): Call<Countries>

    @FormUrlEncoded
    @POST(LIST_STATES)
    fun getStates(
        @Field("countryname") countryName: String
    ): Call<States>

    @FormUrlEncoded
    @POST(NEAR_EVENTS)
    fun nearEvents(
        @Field("currentdate") currentDate: String?,
        @Field("DistanceinMiles") distanceInMile: String?,
        @Field("searchtype") searchType: String?,
        @Field("city") city: String?,
        @Field("lat") latitude: String?,
        @Field("long") longitude: String?,
        @Field("endtime") endTime: String?,
        @Field("categorys") categorys: String?,
        @Field("id") userId: String?,
        @Field("NationWide") nationWide: String?
    ): Call<NearEvents>

    @FormUrlEncoded
    @POST(DELETE_MY_EVENTS)
    fun deleteMyEvents(
        @Field("eventid") eventId: String,
        @Field("id") userId: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST(DELETE_OTHER_EVENTS)
    fun deleteOtherEvents(
        @Field("eventid") eventId: String,
        @Field("id") userId: String,
        @Field("type") eventType: String
    ): Call<DefaultResponse>

}