package com.example.android.activitiesproject.ui.postdetails.model

import com.example.android.activitiesproject.data.network.model.User


class Post(
    val id:String?,
    val userId:String?,
    val insertiondate:String?,
    val text:String?,
    val image:String?,
    val totallikes:String?,
    val filepath:String?,
    val user:User?
)