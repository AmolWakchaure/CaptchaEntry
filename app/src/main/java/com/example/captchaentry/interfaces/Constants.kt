package com.example.captchaentry.interfaces

interface Constants {

    companion object {

        //shared preferences
        var PREF_KEY : String = "pref_key"
        var CONNECTION : String = "Oops ! please check your internet connection."
        var SOME_WRONG : String = "Oops ! something went wrong, please try after some time."

        //api details
        //server url
        var WEB_API : String = ""
        //api name
        var WA_LOGIN : String = WEB_API+"login"
    }
}