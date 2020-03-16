package com.example.android.activitiesproject.ui.home

import com.example.android.activitiesproject.data.network.model.likes.Like

interface OnItemClicked {
    fun onClick(postId: String, commentId: String)

}

interface OnSendComment {
    fun onSend(postId: String, userId: String, comment: String)
}

interface OnSendLike {
    fun onSendLike(userId: String, postId: String)
}

interface GetAllComments {
    fun getAllComments(postId: String)
    fun navtoPostDetailsFragment(postId: String)
}
interface ShowUserProfile{
    fun showProfile(userId: String,image:String)
}

interface AllLikes{
    fun getAllLikes(postId: String):ArrayList<Like>
}