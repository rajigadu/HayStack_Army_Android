package com.android.app.`in`.haystack.network.repository

import com.android.app.`in`.haystack.manager.SessionManager
import com.android.app.`in`.haystack.network.ApiClient
import com.android.app.`in`.haystack.network.ApiInterface
import com.android.app.`in`.haystack.network.response.all_groups.AllGroups
import com.android.app.`in`.haystack.network.response.create_group.Group
import com.android.app.`in`.haystack.network.response.group_members.DefaultResponse
import com.android.app.`in`.haystack.network.response.login.LogIn
import com.android.app.`in`.haystack.network.response.soldier_signup.SignUpResponse
import com.android.app.`in`.haystack.utils.AppConstants.DEVICE_TYPE
import com.android.app.`in`.haystack.utils.Extensions.getUniqueRandomNumber
import com.android.app.`in`.haystack.view.activity.SpouseRegistration
import retrofit2.Call

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

    fun getGroupMembers(groupId: String, userId: String) = client.getGroupMembers(groupId, userId)

    fun editGroupMember(groupId: String, member: String, number: String, email: String, userId: String) =
        client.editGroupMember(groupId, member, number, email, userId)

    fun deleteGroupMember(groupId: String, memberId: String, userId: String) =
        client.deleteGroupMember(groupId, userId, memberId)

    fun deleteGroup(groupId: String, userId: String) = client.deleteGroup(groupId, userId)

}