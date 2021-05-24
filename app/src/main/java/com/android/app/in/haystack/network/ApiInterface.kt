package com.android.app.`in`.haystack.network

import com.android.app.`in`.haystack.network.config.AppConfig.ADD_MEMBER_GROUP
import com.android.app.`in`.haystack.network.config.AppConfig.ALL_CATEGORIES
import com.android.app.`in`.haystack.network.config.AppConfig.ALL_MEMBERS
import com.android.app.`in`.haystack.network.config.AppConfig.CHANGE_PASSWORD
import com.android.app.`in`.haystack.network.config.AppConfig.CONTACT_US
import com.android.app.`in`.haystack.network.config.AppConfig.CREATE_EVENT
import com.android.app.`in`.haystack.network.config.AppConfig.CREATE_GROUP
import com.android.app.`in`.haystack.network.config.AppConfig.DELETE_GROUP
import com.android.app.`in`.haystack.network.config.AppConfig.DELETE_GROUP_MEMBER
import com.android.app.`in`.haystack.network.config.AppConfig.EDIT_GROUP
import com.android.app.`in`.haystack.network.config.AppConfig.EDIT_GROUP_MEMBER
import com.android.app.`in`.haystack.network.config.AppConfig.EDIT_PROFILE
import com.android.app.`in`.haystack.network.config.AppConfig.FORGOT_PASSWORD
import com.android.app.`in`.haystack.network.config.AppConfig.GET_ALL_GROUPS
import com.android.app.`in`.haystack.network.config.AppConfig.GROUP_MEMBERS
import com.android.app.`in`.haystack.network.config.AppConfig.LOG_IN
import com.android.app.`in`.haystack.network.config.AppConfig.SIGN_UP_SOLDIER
import com.android.app.`in`.haystack.network.config.AppConfig.SIGN_UP_SPOUSE
import com.android.app.`in`.haystack.network.config.AppConfig.TERMS_AND_CONDITIONS
import com.android.app.`in`.haystack.network.response.group_members.DefaultResponse
import com.android.app.`in`.haystack.network.response.all_groups.AllGroups
import com.android.app.`in`.haystack.network.response.categories.AllCategories
import com.android.app.`in`.haystack.network.response.create_group.Group
import com.android.app.`in`.haystack.network.response.event.EventCreated
import com.android.app.`in`.haystack.network.response.group_members.GroupMembers
import com.android.app.`in`.haystack.network.response.login.LogIn
import com.android.app.`in`.haystack.network.response.members.Members
import com.android.app.`in`.haystack.network.response.soldier_signup.SignUpResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

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
        @Field("email") email: String?,
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

    @FormUrlEncoded
    @POST(CREATE_EVENT)
    fun createEvent(
        @Field("event_name") eventName: String,
        @Field("streetaddress") streetAddress: String,
        @Field("city") city: String,
        @Field("id") id: String,
        @Field("state") state: String,
        @Field("zipcode") zipCode: String,
        @Field("startdate") startDate: String,
        @Field("starttime") startTime: String,
        @Field("enddate") endDate: String,
        @Field("endtime") endTime: String,
        @Field("hostname") hostName: String,
        @Field("contactinfo") contactInfo: String,
        @Field("hosttype") hostType: String,
        @Field("eventtype") eventType: String,
        @Field("country") country: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("category") category: String
    ): Call<EventCreated>

}