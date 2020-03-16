package com.example.android.activitiesproject.data.network

import android.util.Log
import com.example.android.activitiesproject.data.network.model.*
import com.example.android.activitiesproject.data.network.model.likes.Like
import com.example.android.activitiesproject.data.network.model.likes.LikesResult
import com.example.android.activitiesproject.ui.postdetails.model.Post
import com.example.android.activitiesproject.ui.postdetails.model.PostResult
import com.example.android.activitiesproject.ui.postdetails.model.Comment
import com.example.android.activitiesproject.ui.postdetails.model.CommentResult
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Observable
import io.reactivex.Single
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

const val BASE_URL = "http://192.168.1.75:61595"

class AppApiHelper @Inject constructor() : ApiHelper {


    override fun updateProfileInfo(
        id: String,
        userName: String,
        imagePath: String
    ): Observable<User> {
        Log.e("name", userName)
        if (imagePath != "") {
            val file = File(imagePath)
            return Rx2AndroidNetworking.upload("$BASE_URL/api/user/$id")
                .addMultipartFile("image", file)
                .addMultipartParameter("name", userName)
                .build()
                .getObjectObservable(User::class.java)
        } else
            return Rx2AndroidNetworking.post("$BASE_URL/api/user/$id")
                .addBodyParameter("name", userName)
                .build()
                .getObjectObservable(User::class.java)
    }

    override fun getProfileInfo(id: String): Observable<User> {
        return Rx2AndroidNetworking.get("$BASE_URL/api/user/$id")
            .build()
            .getObjectObservable(User::class.java)
    }

    override fun getAllRanks(): Observable<RankResult> {
        return Rx2AndroidNetworking.get("$BASE_URL/api/rank")
            .build()
            .getObjectObservable(RankResult::class.java)
    }

    override fun getAllEvents(): Observable<EventResult> {

        return Rx2AndroidNetworking.get("$BASE_URL/api/event")
            .build()
            .getObjectObservable(EventResult::class.java)
    }

    override fun addEvent(eventJsonObject: JSONObject): Observable<Event> {

        return Rx2AndroidNetworking.post("$BASE_URL/api/event")
            .addJSONObjectBody(eventJsonObject)
            .build()
            .getObjectObservable(Event::class.java)
    }

    override fun updateEvent(eventJsonObject: JSONObject): Observable<Event> {
        return Rx2AndroidNetworking.put("$BASE_URL/api/event/")
            .addJSONObjectBody(eventJsonObject)
            .build()
            .getObjectObservable(Event::class.java)
    }

    override fun getEventsDetails(id: String): Observable<EventResponse> {
        return Rx2AndroidNetworking.get(BASE_URL + "/api/event/{id}")
            .addPathParameter("id", id)
            .build()
            .getObjectObservable(EventResponse::class.java)
    }

    override fun deleteEvent(eventId: String): Observable<Void> {
        return Rx2AndroidNetworking.delete("$BASE_URL/api/event/$eventId")
            .build()
            .getObjectObservable(Void::class.java)
    }

    override fun getAllPosts(token: String): Observable<PostResult> {
        return Rx2AndroidNetworking.get(BASE_URL + "/api/post")
            .build()
            .getObjectObservable(PostResult::class.java)

//            .addHeaders("Authorization", token)

    }


    override fun addPost(userId: String, postText: String, imagePath: String): Observable<Post> {
        if (postText==""){
            val file = File(imagePath)
            return Rx2AndroidNetworking.upload(BASE_URL + "/api/post")
                .addMultipartFile("image", file)
                .addMultipartParameter("userid", userId)
                .build()
                .getObjectObservable(Post::class.java)
        }
        else if (imagePath == "") {

            return Rx2AndroidNetworking.upload(BASE_URL + "/api/post")
                .addMultipartParameter("text", postText)
                .addMultipartParameter("userid", userId)
                .build()
                .getObjectObservable(Post::class.java)

        } else{
            val file = File(imagePath)
            return Rx2AndroidNetworking.upload(BASE_URL + "/api/post")
                .addMultipartFile("image", file)
                .addMultipartParameter("text", postText)
                .addMultipartParameter("userid", userId)
                .build()
                .getObjectObservable(Post::class.java)
        }

    }

    override fun getUserPosts(userId: String): Observable<PostResult> {
        return Rx2AndroidNetworking.get(BASE_URL + "/api/user/$userId/post")
            .build()
            .getObjectObservable(PostResult::class.java)
    }

    override fun getAllComments(postId: String): Single<CommentResult> {
        return Rx2AndroidNetworking.get(BASE_URL + "/api/post/$postId/comment")
            .build()
            .getObjectSingle(CommentResult::class.java)
    }

    override fun deleteComment(commentId: String, postId: String): Observable<Void> {

        return Rx2AndroidNetworking.delete("$BASE_URL/api/post/$postId/comment/$commentId")
            .build()
            .getObjectObservable(Void::class.java)


    }

    override fun sendComment(postId: String, userId: String, comment: String): Observable<Comment> {
        return Rx2AndroidNetworking.post("$BASE_URL/api/comment")
            .addBodyParameter("text", comment)
            .addBodyParameter("userid", userId)
            .addBodyParameter("postid", postId)
            .build()
            .getObjectObservable(Comment::class.java)


    }

    override fun sendLike(userId: String, postId: String): Observable<Like> {
        val likeJson = JSONObject()
        likeJson.put("userid", userId)
        likeJson.put("postid", postId)
        return Rx2AndroidNetworking.post("$BASE_URL/api/like")
            .addJSONObjectBody(likeJson)
            .build()
            .getObjectObservable(Like::class.java)
    }

    override fun getPost(id: String): Observable<Post> {
        return Rx2AndroidNetworking.get(BASE_URL + "/api/post/$id")
            .build()
            .getObjectObservable(Post::class.java)
    }

    override fun deletePost(postId: String): Observable<Void> {
        return Rx2AndroidNetworking.delete("$BASE_URL/api/post/$postId")
            .build()
            .getObjectObservable(Void::class.java)
    }

    override fun getLikes(postId: String): Observable<LikesResult> {
        return Rx2AndroidNetworking.get("$BASE_URL/api/post/$postId/like")
            .build()
            .getObjectObservable(LikesResult::class.java)
    }

    override fun login(loginJson: JSONObject): Observable<LoginResult> {
        return Rx2AndroidNetworking.post("$BASE_URL/Login")
            .addJSONObjectBody(loginJson)
            .build()
            .getObjectObservable(LoginResult::class.java)
    }

    override fun editPassword(newPassword: JSONObject,userId: String): Observable<User> {
        return Rx2AndroidNetworking.put("$BASE_URL/api/user/$userId")
            .addJSONObjectBody(newPassword)
            .build()
            .getObjectObservable(User::class.java)
    }
}