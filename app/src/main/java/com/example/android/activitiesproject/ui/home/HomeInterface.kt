package com.example.android.activitiesproject.ui.home

import android.net.Uri

interface HomeInterface {
    fun onPublishSuccess(a:String,path:String?)
    fun onPublishFailed()
    fun onDeletePostSuccess()
}