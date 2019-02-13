package com.example.captchaentry.interfaces

interface Constants {

    companion object {

        var SPLASH_DELAY : Long = 2000
        var TIMER_DELAY : Int = 30

        var USER_NAME : String = "username"
        var USER_ID : String = "user_id"
        var CATCHA_ID : String = "catcha_id"
        var CORRECT_COUNT : String = "c_count"
        var INCORRECT_COUNT : String = "inc_count"
        var ATTEMPTED_COUNT : String = "att_count"
        var NOT_ATTEMPTED_COUNT : String = "nota_count"

        //shared preferences
        var PREF_KEY : String = "pref_key"
        var CONNECTION : String = "Oops ! please check your internet connection."
        var SOME_WRONG : String = "Oops ! something went wrong, please try after some time."

        //api details
        //server url
        //var WEB_API : String = "http://192.168.0.102/captcha/api/"
        var WEB_API : String = "http://onlinebizdigitalschool.in/captcha/api/"
        //api name
        var WA_LOGIN : String = WEB_API+"userLogin"
        var WA_LOGOUT : String = WEB_API+"userLogout"
        var WA_ACCOUNT_STATUS : String = WEB_API+"accountStatus"
        var WA_GET_CAPTCHA : String = WEB_API+"getCaptcha"
        var WA_STORE_CAPTCHA : String = WEB_API+"storeCaptcha"
    }
}