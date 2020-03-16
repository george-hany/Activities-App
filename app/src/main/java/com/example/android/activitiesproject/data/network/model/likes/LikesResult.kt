package com.example.android.activitiesproject.data.network.model.likes

import com.google.gson.annotations.SerializedName

class LikesResult {
    @SerializedName("likes")
     var likes:ArrayList<Like>?=null
}