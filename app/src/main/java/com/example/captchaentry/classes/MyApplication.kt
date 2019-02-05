package com.example.captchaentry.classes

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.captchaentry.interfaces.Constants

class MyApplication : Application()
{
    companion object
    {

        lateinit var context: Context
        lateinit var prefs : SharedPreferences
        lateinit var editor : SharedPreferences.Editor

        //for volley
        private val TAG = MyApplication::class.java.simpleName
        @get:Synchronized var instance: MyApplication? = null
            private set


    }
    override fun onCreate() {
        super.onCreate()

        context = applicationContext
        instance = this

        //shared preferences
        prefs = getSharedPreferences(Constants.PREF_KEY,0)
        editor = prefs.edit()
        editor.commit()

    }
    val requestQueue: RequestQueue? = null
        get()
        {
            if (field == null)
            {
                return Volley.newRequestQueue(applicationContext)
            }
            return field
        }
    fun <T> addToRequestQueue(request: Request<T>)
    {
        request.tag = TAG
        request.retryPolicy = DefaultRetryPolicy(30000, 0, 0F)
        requestQueue?.add(request)
    }
}