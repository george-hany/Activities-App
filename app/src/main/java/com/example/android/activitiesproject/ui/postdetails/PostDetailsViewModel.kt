package com.example.android.activitiesproject.ui.postdetails

import androidx.lifecycle.MutableLiveData
import com.example.android.activitiesproject.data.network.ApiHelper
import com.example.android.activitiesproject.data.network.model.likes.Like
import com.example.android.activitiesproject.data.network.model.likes.LikesResult
import com.example.android.activitiesproject.base.BaseViewModel
import com.example.android.activitiesproject.ui.postdetails.model.Post
import com.example.android.activitiesproject.ui.postdetails.model.Comment
import com.example.android.activitiesproject.ui.postdetails.model.CommentResult
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PostDetailsViewModel @Inject constructor(var apihelper: ApiHelper) :
    BaseViewModel(apihelper) {
    var totalLikes = MutableLiveData<String>()
    var likes = MutableLiveData<ArrayList<Like>>()
    var post = MutableLiveData<Post>()
    var id = ""
    lateinit var postDetailsInterface: PostDetailsInterface
    var comments = MutableLiveData<ArrayList<Comment>>()
    var token = ""

    fun getPost(id: String) {
        val observable = apiHelper.getPost(id)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Post> {
                override fun onComplete() {

                }

                override fun onNext(t: Post) {
                    post.value = t!!
                    totalLikes.value = t.totallikes
                }


                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    System.out.println(e.message.toString())
                }

            })
    }

    fun getAllComments(id: String) {
        val observable = apiHelper.getAllComments(id)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<CommentResult> {
                override fun onError(e: Throwable) {
                    System.out.println(e.message!!)
                }

                override fun onSuccess(t: CommentResult) {
                    comments.value = t!!.comments
                }

                override fun onSubscribe(d: Disposable) {
                }

            })
    }

    fun deletePost() {
        val observable = apiHelper.deletePost(id)

        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Void> {
                override fun onComplete() {
                    postDetailsInterface.onDeletePostSuccess()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Void) {
                }

                override fun onError(e: Throwable) {
                    postDetailsInterface.onDeletePostSuccess()

                    System.out.println(e.message.toString())
                }


            })
    }

    fun sendComment(postId: String, userId: String, text: String) {
        val observable = apihelper.sendComment(postId, userId, text)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Comment> {
                override fun onComplete() {
                    postDetailsInterface.onAddCommentSuccess()
                    getAllComments(id)
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Comment) {
                }

                override fun onError(e: Throwable) {
                    System.out.println(e.message.toString())
                }

            })

    }

    fun sendLike(userId: String, postId: String) {
        val observable = apihelper.sendLike(userId, postId)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Like> {
                override fun onComplete() {
                    getPost(id)
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Like) {

                }

                override fun onError(e: Throwable) {
                    System.out.println(e.message.toString())
                }

            })
    }

    fun getAllLikes() {
        val observable = apihelper.getLikes(id)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<LikesResult> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: LikesResult) {
                    likes.value = t!!.likes
                }

                override fun onError(e: Throwable) {
                    System.out.println(e.message.toString())
                }

            })
    }

    fun deleteComment(commentId: String, postId: String) {
        val observable = apihelper.deleteComment(commentId, postId)
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
                    getAllComments(id)
                }

            })
    }


}


