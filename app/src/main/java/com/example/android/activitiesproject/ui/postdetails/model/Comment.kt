package com.example.android.activitiesproject.ui.postdetails.model

import com.example.android.activitiesproject.data.network.model.User

class Comment (
    val id:String,
    val postid:String,
    val text:String,
    val insertiondate:String,
    val user:User
)
