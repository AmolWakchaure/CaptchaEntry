package com.example.captchaentry.activity

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.captchaentry.R
import com.example.captchaentry.classes.T
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*



class MainActivity : AppCompatActivity() {



    private lateinit var mdelayHandler : Handler
    var cnt = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialise the handler
        mdelayHandler = Handler()
        startTimer();
    }

    fun startTimer() {
        mIsRunning = true
        mStatusChecker.run()

    }
    internal var mIsRunning: Boolean = false
    internal var mStatusChecker: Runnable = object : Runnable {
        override fun run() {

            if (!mIsRunning) {
                return  // stop when told to stop
            }
            if(cnt == 0)
            {
                cancelHandler();
            }
            else if(cnt == 1)
            {
                timer_txt.setText("Time Over")
            }
            else if(cnt <= 5 )
            {
                timer_txt.setText("Time remaining : "+cnt+" sec")
                timer_txt.setTextColor(Color.parseColor("#ff3333"))

            }
            else
            {
                timer_txt.setText("Time remaining : "+cnt+" sec")
                timer_txt.setTextColor(Color.parseColor("#00b33c"))
            }
            cnt--
            mdelayHandler.postDelayed(this, 1000)
        }
    }

    /*override fun onStop() {
        super.onStop()
        mIsRunning = false
        mdelayHandler.removeCallbacks(mStatusChecker)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mIsRunning = false
        mdelayHandler.removeCallbacks(mStatusChecker)
    }*/
    private fun cancelHandler() {

        mIsRunning = false
        mdelayHandler.removeCallbacks(mStatusChecker)
    }
}
