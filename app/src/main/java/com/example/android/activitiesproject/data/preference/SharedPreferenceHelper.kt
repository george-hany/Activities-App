package com.example.android.activitiesproject.data.preference

import android.content.Context
import android.content.SharedPreferences
const val IMAGE_KEY="imagePath"
const val USER_NAME_KEY="userName"
const val TOKEN_KEY="token"
const val STATUS_KEY="status"
const val USER_ID="userId"
class SharedPreferenceHelper(val context: Context) {

    private val PREFERENCE_NAME = "userinfo"
    val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun saveInfo(imagePath: String?, userName: String,userId:String,token:String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        if (imagePath==null)
            editor.putString(IMAGE_KEY, "").apply()
        else
            editor.putString(IMAGE_KEY, imagePath).apply()
        editor.putString(USER_NAME_KEY, userName).apply()
        editor.putString(TOKEN_KEY, token).apply()
        editor.putBoolean(STATUS_KEY, true).apply()
        editor.putString(USER_ID,userId).apply()
    }

    fun isLogedin() = sharedPreferences.getBoolean("status", false)

    fun logOut() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(IMAGE_KEY).apply()
        editor.remove(USER_NAME_KEY).apply()
        editor.remove(TOKEN_KEY).apply()
        editor.remove(STATUS_KEY).apply()
        editor.remove(USER_ID).apply()

    }

    fun getImage()=sharedPreferences.getString(IMAGE_KEY,"")
    fun getUserName()=sharedPreferences.getString(USER_NAME_KEY,"")
    fun getToken()=sharedPreferences.getString(TOKEN_KEY,"")
    fun getId()=sharedPreferences.getString(USER_ID,"")



    fun updateProfileInfo(imagePath: String,userName:String){
        val editor:SharedPreferences.Editor=sharedPreferences.edit()
        editor.putString(IMAGE_KEY,imagePath).apply()
        editor.putString(USER_NAME_KEY,userName).apply()
    }
}