package com.example.android.activitiesproject.ui.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.android.activitiesproject.data.network.ApiHelper
import com.example.android.activitiesproject.data.network.model.User
import com.example.android.activitiesproject.data.network.model.likes.Like
import com.example.android.activitiesproject.base.BaseViewModel
import com.example.android.activitiesproject.ui.postdetails.model.Post
import com.example.android.activitiesproject.ui.postdetails.model.PostResult
import com.example.android.activitiesproject.ui.postdetails.model.Comment
import com.example.android.activitiesproject.ui.postdetails.model.CommentResult
import com.example.android.activitiesproject.utils.Validator
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import javax.inject.Inject

const val NEW_PASSWORD_EMPTY_ERROR = "new password is empty"
const val CURRENT_PASSWORD_EMPTY_ERROR = "current password is empty"
const val CURRENT_PASSWORD_INCORRECT_ERROR="in correct password"

class ProfileViewModel @Inject constructor(var apihelper: ApiHelper) : BaseViewModel(apihelper) {
    var userPosts = MutableLiveData<ArrayList<Post>>()
    var user = MutableLiveData<User>()
    var userName = MutableLiveData<String>()
    var currentPassword = ""
    var newPassword = ""
    var confirmPassword = ""
    var userTeam = MutableLiveData<String>()
    var imagePath = ""
    var totalLikes = MutableLiveData<Int>()
    var dialogUserName = ""
    var likesRank = MutableLiveData<Int>()
    var userImagePath = MutableLiveData<String?>()
    var id = ""
    val validator = Validator()
    lateinit var profileInterface: ProfileInterface
    var comments = MutableLiveData<ArrayList<Comment>>()
    var token = ""
    var userId=""
    fun initProfileInterface(Iprofile: ProfileInterface) {
        profileInterface = Iprofile
    }

    fun saveProfileInfo() {
        if (dialogUserName!!.length != 0) {
            Log.e("erroo", dialogUserName)
            Log.e("erroo", imagePath)

            val observable = apihelper.updateProfileInfo(
                id,
                dialogUserName,
                imagePath
            )
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<User> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        Log.e("erroo", e.toString())
                    }

                    override fun onNext(t: User) {
                        profileInterface.updateProfileInfo(t!!)
                        userName.value = t!!.name
                        userTeam.value = t.team
                        totalLikes.value = t.totallikes
                        likesRank.value = t.rank
                        userImagePath.value = t.image
                        user.value = t
                    }

                    override fun onComplete() {
                        profileInterface.changeInfoSuccess()
                    }

                })

        } else {
            profileInterface.userNameError()
        }
    }

    fun savePassword() {
        if (currentPassword.length != 0) {
            if (validator.checkPassword(currentPassword).equals("valid")) {
                if (newPassword.length != 0) {
                    if (validator.checkPassword(newPassword).equals("valid")) {
                        if (confirmPassword.length != 0) {
                            if (newPassword.equals(confirmPassword)) {
                                val passwordJson=JSONObject()
                                passwordJson.put("newpassword",newPassword)
                                passwordJson.put("oldpassword",currentPassword)
                                val observable=apihelper.editPassword(passwordJson,userId)
                                observable.subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(object :Observer<User>{
                                        override fun onComplete() {
                                            profileInterface.changePasswordSuccess()
                                            currentPassword = ""
                                            newPassword = ""
                                            confirmPassword = ""
                                        }

                                        override fun onSubscribe(d: Disposable) {
                                        }

                                        override fun onNext(t: User) {
                                        }

                                        override fun onError(e: Throwable) {
                                            Log.e("passwordd",e.message!!)
                                            profileInterface.currentPasswordError(
                                                CURRENT_PASSWORD_INCORRECT_ERROR)
                                        }

                                    })




                            } else {
                                profileInterface.matchPasswordsError()
                            }
                        } else {
                            profileInterface.confirmPasswordError()
                        }
                    } else {
                        profileInterface.newPasswordError(validator.checkPassword(newPassword))
                    }
                } else {
                    profileInterface.newPasswordError(NEW_PASSWORD_EMPTY_ERROR)
                }
            } else {
                profileInterface.currentPasswordError(validator.checkPassword(currentPassword))
            }
        } else {
            profileInterface.currentPasswordError(CURRENT_PASSWORD_EMPTY_ERROR)
        }

    }

    fun LoadProfileData(id: String) {
        this.id = id
        var observable = apiHelper.getProfileInfo(id)

        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<User> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: User) {
//                    profileInterface.updateProfileInfo(t!!)
                    userName.value = t!!.name
                    userTeam.value = t.team
                    totalLikes.value = t.totallikes
                    likesRank.value = t.rank
                    userImagePath.value = t.image
                    user.value = t
                }

                override fun onComplete() {
                    Log.e("errorrcom", "doneprof")
                }

                override fun onError(e: Throwable) {
                    Log.e("errorr", e.message)
                }
            })


    }

    fun loadUserPosts(userId: String) {
        val observable = apihelper.getUserPosts(userId)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<PostResult> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.e("errprofpost", e!!.message)
                }

                override fun onNext(t: PostResult) {
                    userPosts.value = t!!.posts
                }

                override fun onComplete() {

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

    fun sendComment(userId: String, postId: String, comment: String) {
        val observable = apihelper.sendComment(postId, userId, comment)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Comment> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    Log.e("errpost", e!!.message)
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


}