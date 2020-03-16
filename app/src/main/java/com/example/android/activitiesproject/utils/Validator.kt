package com.example.android.activitiesproject.utils

import android.util.Patterns

const val PASSWORD_ERROR_LENGTH="password should be at least 8 chars"
const val PASSWORD_ERROR_NUMBER="password should contain at least one number"
const val PASSWORD_ERROR_UPPERCASE="password should contain at least one upper character"
const val PASSWORD_ERROR_LOWERCASE="password should contain at least one lower character"

class Validator {


    fun checkEmail(email:String):Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()

    }

    fun checkPassword(pass:String):String{


//
//        if (pass.length<8)
//            return PASSWORD_ERROR_LENGTH


//        var exp="[0-9]"
//        var pattern =Pattern.compile(exp,Pattern.CASE_INSENSITIVE)
//        var matcher=pattern.matcher(pass)
//         if (!matcher.find())
//            return PASSWORD_ERROR_NUMBER
//
//        exp ="[a-z]"
//        pattern= Pattern.compile(exp)
//        matcher=pattern.matcher(pass)
//        if (!matcher.find())
//            return PASSWORD_ERROR_LOWERCASE
//
//        exp="[A-Z]"
//        pattern= Pattern.compile(exp)
//        matcher=pattern.matcher(pass)
//        if (!matcher.find())
//            return PASSWORD_ERROR_UPPERCASE

        return "valid"
    }
}