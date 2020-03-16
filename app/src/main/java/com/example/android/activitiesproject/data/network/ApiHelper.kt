package com.example.android.activitiesproject.data.network

import com.example.android.activitiesproject.data.network.model.*
import com.example.android.activitiesproject.data.network.model.likes.Like
import com.example.android.activitiesproject.data.network.model.likes.LikesResult
import com.example.android.activitiesproject.ui.postdetails.model.Post
import com.example.android.activitiesproject.ui.postdetails.model.PostResult
import com.example.android.activitiesproject.ui.postdetails.model.Comment
import com.example.android.activitiesproject.ui.postdetails.model.CommentResult
import io.reactivex.Observable
import io.reactivex.Single
import org.json.JSONObject

interface ApiHelper {
    fun getAllRanks(): Observable<RankResult>
    fun getProfileInfo(id:String):Observable<User>
    fun updateProfileInfo(id: String,userName:String,imagePath:String):Observable<User>
    fun getAllEvents():Observable<EventResult>
    fun addEvent(eventJsonObject: JSONObject):Observable<Event>
    fun getEventsDetails(id:String):Observable<EventResponse>
    fun updateEvent(eventJsonObject: JSONObject):Observable<Event>
    fun deleteEvent(eventId: String):Observable<Void>
    fun getAllPosts(token:String):Observable<PostResult>
    fun addPost(userId:String,postText:String,imagePath: String):Observable<Post>
    fun getUserPosts(userId: String):Observable<PostResult>
    fun getAllComments(postId:String): Single<CommentResult>
    fun deleteComment(commentId:String,postId:String):Observable<Void>
    fun sendComment(postId: String,userId: String,comment: String):Observable<Comment>
    fun sendLike(userId: String,postId: String):Observable<Like>
    fun getPost(id: String): Observable<Post>
    fun deletePost(postId:String):Observable<Void>
    fun getLikes(postId: String):Observable<LikesResult>
    fun login(loginJson:JSONObject):Observable<LoginResult>
    fun editPassword(newPassword:JSONObject,userId: String):Observable<User>


}