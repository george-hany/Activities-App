package com.example.android.activitiesproject.ui.login

interface LoginInterface {
    fun onLoginSuccess()
    fun onLoginFailed(errorType:String,errorMessage:String)
    fun onStartToLoginLoader()
    fun onStopLoginLoader()

}