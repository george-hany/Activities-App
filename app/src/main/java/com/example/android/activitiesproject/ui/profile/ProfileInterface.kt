package com.example.android.activitiesproject.ui.profile

import com.example.android.activitiesproject.data.network.model.User

interface ProfileInterface {
    fun userNameError()
    fun currentPasswordError(msg:String)
    fun newPasswordError(msg:String)
    fun confirmPasswordError()
    fun matchPasswordsError()
    fun changePasswordSuccess()
    fun changeInfoSuccess()
    fun updateProfileInfo(user: User)
}