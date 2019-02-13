package com.example.captchaentry.classes

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.View
import android.widget.EditText


class T {

    companion object {

        //toast
        fun t(message : String)
        {
            Toast.makeText(MyApplication.context,message,Toast.LENGTH_LONG).show()
        }
        //log
        fun e(message : String)
        {
            Log.e("CAPTCHA_LOG",message)
        }
        //snack bar
        fun s(view : View, message : String)
        {
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snackbar.show()
        }
        //validate editext
        fun validateEditext(view : View, editext : EditText, message : String) : Boolean
        {
            if(editext.text.toString().isEmpty())
            {
                s(view,message)
                return false
            }
            else
            {
                return true
            }
        }
        //check internet connection
        fun isNetworkAvailable(): Boolean
        {
            val connectivityManager = MyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE)
            return if (connectivityManager is ConnectivityManager) {
                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            } else false
        }


    }
}