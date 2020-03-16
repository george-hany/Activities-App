package com.example.android.activitiesproject.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.activitiesproject.data.network.AppApiHelper
import com.example.android.activitiesproject.data.network.model.LoginResult
import com.example.android.activitiesproject.utils.Validator
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

const val error_empty_email = "Email fieled is empty!"
const val error_invalid_email = "Email is not valid!"
const val error_notFound_email = "Email is not found!"
const val error_empty_password = "Password fieled is empty!"
const val error_wrong_password = "Wrong Password!!"

class LoginViewModel : ViewModel() {
    val loginData = MutableLiveData<LoginResult>()
    var email = ""
    var password = ""
    lateinit var loginInterface: LoginInterface
    val validator = Validator()
    val apiHelper = AppApiHelper()

    fun initLoginInterface(loginInterface: LoginInterface) {
        this.loginInterface = loginInterface
    }

    fun login() {
        if (email.length != 0) {
            if (validator.checkEmail(email)) {
                if (password.length != 0) {
                    if (validator.checkPassword(password).equals("valid")) {
                        val loginJson = JSONObject()
                        loginJson.put("email", email)
                        loginJson.put("password", password)
                        val observable = apiHelper.login(loginJson)
                        observable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Observer<LoginResult> {
                                override fun onComplete() {
                                    loginInterface.onLoginSuccess()
                                    loginInterface.onStopLoginLoader()
                                }

                                override fun onSubscribe(d: Disposable) {
                                    loginInterface.onStartToLoginLoader()
                                }

                                override fun onNext(t: LoginResult) {
                                    loginData.value = t
                                }

                                override fun onError(e: Throwable) {
                                    loginInterface.onLoginFailed("login", "error")
                                    loginInterface.onStopLoginLoader()

                                }

                            })

                    } else {
                        loginInterface.onLoginFailed(
                            "password",
                            (validator.checkPassword(password))
                        )

                    }

                } else {
                    loginInterface.onLoginFailed("password", error_empty_password)

                }
            } else {
                loginInterface.onLoginFailed("email", error_invalid_email)

            }

        } else {
            loginInterface.onLoginFailed("email", error_empty_email)
        }


//        if (password.length == 0)
//            loginInterface.onLoginFailed("password", error_empty_password)
//        if (email.length != 0 && password.length != 0)
//            loginInterface.onLoginSuccess()
    }
}