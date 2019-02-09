package com.example.captchaentry.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.captchaentry.R
import com.example.captchaentry.classes.MyApplication
import com.example.captchaentry.interfaces.Constants

class SplashActivity : AppCompatActivity() {


    private lateinit var mdelayHandler : Handler
    private var splashDelay : Long = Constants.SPLASH_DELAY//2 sec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //initialise the handler
        mdelayHandler = Handler()
        //navigate with delay
        mdelayHandler.postDelayed(mRunnable,splashDelay)

    }
    internal  val mRunnable : Runnable = Runnable {

        if(!isFinishing)
        {

            val USER_NAME = MyApplication.prefs.getString(Constants.USER_NAME,"0")
            if(USER_NAME.equals("0"))
            {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else
            {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }

        }
    }
    override fun onDestroy()
    {
        super.onDestroy()
        mdelayHandler.removeCallbacks(mRunnable)

    }
}
