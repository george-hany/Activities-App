package com.example.android.activitiesproject.ui.login

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import com.example.android.activitiesproject.R
import kotlinx.android.synthetic.main.activity_login.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.android.activitiesproject.data.preference.SharedPreferenceHelper
import com.example.android.activitiesproject.base.MainActivity
import com.example.android.activitiesproject.databinding.ActivityLoginBinding
import com.example.android.activitiesproject.app.MyApplication
import es.dmoral.toasty.Toasty


class LoginActivity : AppCompatActivity(), LoginInterface {


    var ischecked = false
    lateinit var loginBinding: ActivityLoginBinding
    lateinit var loginViewModel: LoginViewModel
    lateinit var pref: SharedPreferenceHelper
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.injectLoginActivity(this)
        progressDialog = ProgressDialog(this,R.style.CircularProgress2)
        progressDialog.setMessage("loading")
        progressDialog.setCancelable(false)
        pref = SharedPreferenceHelper(this)
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        loginBinding.lifecycleOwner = this
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        loginViewModel.initLoginInterface(this)
        loginBinding.vm = loginViewModel
        if (pref.isLogedin()) {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        pass_icon.setOnClickListener {

            if (!ischecked) {
                pass_icon.setImageResource(R.drawable.unlock_icon)
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                password.setSelection(password.getText().length)
                ischecked = true
            } else {
                ischecked = false
                password.setTransformationMethod(PasswordTransformationMethod.getInstance())
                password.setSelection(password.getText().length)
                pass_icon.setImageResource(R.drawable.lock_icon)
            }
        }

    }

    override fun onLoginFailed(errorType: String, errorMessage: String) {
        if (errorType.equals("email")) {
            email.error = errorMessage
            email.requestFocus()
            password.error = null
        }
        if (errorType.equals("password")) {
            password.error = errorMessage
            password.requestFocus()
            email.error = null
        }
        if(errorType.equals("login")){
            Toasty.error(this,"invalid email or password",Toasty.LENGTH_LONG).show()
        }
    }

    override fun onLoginSuccess() {
        var image = loginViewModel.loginData.value!!.user.image
        val userName = loginViewModel.loginData.value!!.user.name
        val token = loginViewModel.loginData.value!!.token.access_token
        val userId = loginViewModel.loginData.value!!.user.id
        if(loginViewModel.loginData.value!!.user.image==null)
            image=""
        pref.saveInfo(image, userName, userId, token)
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onStartToLoginLoader() {
        progressDialog.show()
    }

    override fun onStopLoginLoader() {
        progressDialog.dismiss()
    }

}
