package com.example.android.activitiesproject.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.android.activitiesproject.data.network.ApiHelper
import com.example.android.activitiesproject.data.network.model.likes.Like
import com.example.android.activitiesproject.data.network.model.likes.LikesResult
import com.example.android.activitiesproject.base.BaseViewModel
import com.example.android.activitiesproject.ui.postdetails.model.Post
import com.example.android.activitiesproject.ui.postdetails.model.PostResult
import com.example.android.activitiesproject.ui.postdetails.model.Comment
import com.example.android.activitiesproject.ui.postdetails.model.CommentResult
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel @Inject constructor(var apihelper: ApiHelper) : BaseViewModel(apihelper) {
    var activityText = ""
    var imagePath = ""
    lateinit var homeInterface: HomeInterface
    var posts = MutableLiveData<ArrayList<Post>>()
    var userId = "9a95d652-5f5f-403b-a62e-6ba69e797f31"
    var comments = MutableLiveData<ArrayList<Comment>>()
    var token = ""
    var likes = MutableLiveData<HashMap<String, ArrayList<Like>>>()


    fun publish() {
        if (activityText != "" || imagePath != "") {

            val observable = apihelper.addPost(userId, activityText, imagePath)
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Post> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        Log.e("erroopost", e!!.message)
                    }

                    override fun onNext(t: Post) {
                        posts.value!!.add(t!!)
                    }

                    override fun onComplete() {
                        homeInterface.onPublishSuccess(activityText, imagePath)
                    }

                })
        }
//        homeInterface.onPublishSuccess(activityText, imagePath)
    }

    fun initHomeInterface(home: HomeInterface) {
        homeInterface = home
    }

    fun loadPosts() {
        var observable = apihelper.getAllPosts(token)

        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<PostResult> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: PostResult) {
                    posts.value = t!!.posts
                }

                override fun onComplete() {
                    Log.e("errooposts", "done")
                }


                override fun onError(e: Throwable) {
                    println(e.message)
                }
            })
    }

    fun getComments(postId: String) {
        val observable = apihelper.getAllComments(postId)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<CommentResult> {
                override fun onSuccess(t: CommentResult) {
                    comments.value = t!!.comments
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    Log.e("errcomment", e!!.message)
                }
            })
    }

    fun deleteComment(commentId: String, postId: String) {
        val observable = apihelper.deleteComment(commentId, postId)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Void> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    Log.e("errpost", e!!.message)
                }

                override fun onNext(t: Void) {

                }

                override fun onComplete() {
                    Log.e("errpost", "done")

                }

            })
    }

    fun sendComment(userId: String, postId: String, comment: String) {
        val observable = apihelper.sendComment(postId, userId, comment)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Comment> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    Log.e("errpost", e.message.toString())
                }

                override fun onNext(t: Comment) {

                }

                override fun onComplete() {
                    Log.e("errpost", "done")
                }

            })
    }

    fun sendLike(userId: String, postId: String) {
        val observable = apihelper.sendLike(userId, postId)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Like> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.e("errlikes", e!!.message)
                }

                override fun onNext(t: Like) {
                }

                override fun onComplete() {
                    Log.e("errlikes", "done")

                }

            })
    }

    fun deletePost(postId: String) {
        val observable = apihelper.deletePost(postId)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Void> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Void) {

                }

                override fun onError(e: Throwable) {
                    homeInterface.onDeletePostSuccess()
                }

            })
    }

    var m = HashMap<String, ArrayList<Like>>()

    fun getLikes(postId: String) {

        val observable = apihelper.getLikes(postId)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<LikesResult> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: LikesResult) {
                    m.put(postId, t.likes!!)
                    likes.value = m
                }

                override fun onError(e: Throwable) {

                }

            })

    }


}