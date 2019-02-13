package com.example.captchaentry.activity

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.example.captchaentry.R
import com.example.captchaentry.classes.MyApplication
import com.example.captchaentry.classes.T
import com.example.captchaentry.interfaces.Constants
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import org.json.JSONTokener
import java.text.SimpleDateFormat
import java.util.*
import cn.pedant.SweetAlert.SweetAlertDialog
class MainActivity : AppCompatActivity() {



    private lateinit var mdelayHandler : Handler
    var cnt = Constants.TIMER_DELAY
    var receivedCaptcha = ""
    var  timerStatus = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialise the handler
        mdelayHandler = Handler()


        //set count
        setCount();


        setClickListner()

        timerStatus = "first";
        //check account activated or deactivated
        //logout app here
        if(T.isNetworkAvailable())
        {
            checkAcountStatus();


        }

    }

    private fun setCount() {


        correctCount.setText(""+MyApplication.prefs.getString(Constants.CORRECT_COUNT,"0"))
        incorrectCount.setText(""+MyApplication.prefs.getString(Constants.INCORRECT_COUNT,"0"))
        attemptedCount.setText(""+MyApplication.prefs.getString(Constants.ATTEMPTED_COUNT,"0"))
        notAttemptedCount.setText(""+MyApplication.prefs.getString(Constants.NOT_ATTEMPTED_COUNT,"0"))

    }

    private fun getCatchaDetails(captchaId : String) {

        T.e("captchaId : "+captchaId)
        try
        {
            var progressDialog = ProgressDialog(this);
            progressDialog.setMessage("Getting Captcha, please wait...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            val stringRequest = object : StringRequest
                (
                Request.Method.POST,
                Constants.WA_GET_CAPTCHA,
                Response.Listener<String>
                {
                    response ->
                    progressDialog.dismiss()
                    parseCaptchaResponse(response,captchaId)

                },
                object : Response.ErrorListener
                {
                    override fun onErrorResponse(volleyError: VolleyError)
                    {

                        progressDialog.dismiss()
                        getCatchaDetails(captchaId)
                        //return volley error
                        T.s(mainLayout,Constants.SOME_WRONG)

                    }
                }
            )
            {

                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String>
                {
                    val params = HashMap<String, String>()
                    params.put("userid", MyApplication.prefs.getString(Constants.USER_ID,""))
                    params.put("captchaid", captchaId)
                    return params
                }
            }
            //adding request to queue
            MyApplication.instance!!.addToRequestQueue(stringRequest)
        }
        catch (c : Exception)
        {
            T.e("Exception : " + c)
        }

    }

    private fun parseCaptchaResponse(response: String?,captchaId: String?) {

        var json = JSONTokener(response).nextValue();
        if (json is JSONObject)
        {
            var logoutJson = JSONObject(response)

            var success = logoutJson.getString("success")

            if(success.equals("1"))
            {
                var captcha = logoutJson.getString("captcha")

                var correct = logoutJson.getString("correct")
                var incorrect = logoutJson.getString("incorrect")
                var attempted = logoutJson.getString("attempted")
                var notattempted = logoutJson.getString("notattempted")

                //store catcha count

                MyApplication.editor.putString(Constants.CORRECT_COUNT,correct).commit()
                MyApplication.editor.putString(Constants.INCORRECT_COUNT,incorrect).commit()
                MyApplication.editor.putString(Constants.ATTEMPTED_COUNT,attempted).commit()
                MyApplication.editor.putString(Constants.NOT_ATTEMPTED_COUNT,notattempted).commit()


                correctCount.setText(correct)
                incorrectCount.setText(incorrect)
                attemptedCount.setText(attempted)
                notAttemptedCount.setText(notattempted)
                captchaText.setText(captcha)
                receivedCaptcha = captcha

                //save captcha id
                MyApplication.editor.putString(Constants.CATCHA_ID,captchaId).commit()

                //start captcha timer
                cnt = Constants.TIMER_DELAY

               // T.e("timerStatus : "+timerStatus)
                if(timerStatus.equals("first"))
                {
                    timerStatus = "second"
                    startTimer()
                }

            }
            else
            {
                var message = logoutJson.getString("message")
                T.s(mainLayout,message)
            }

        }
        else
        {
            T.t("Incorrect json found")
        }

    }

    private fun checkAcountStatus() {


        try
        {
            var progressDialog = ProgressDialog(this);
            progressDialog.setMessage("Checking account status, please wait...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            val stringRequest = object : StringRequest
                (
                Request.Method.POST,
                Constants.WA_ACCOUNT_STATUS,
                Response.Listener<String>
                {
                        response ->
                    progressDialog.dismiss()
                    parseAccountResponse(response)

                },
                object : Response.ErrorListener
                {
                    override fun onErrorResponse(volleyError: VolleyError)
                    {

                        progressDialog.dismiss()
                        //return volley error
                        T.s(mainLayout,Constants.SOME_WRONG)

                    }
                }
            )
            {

                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String>
                {
                    val params = HashMap<String, String>()
                    params.put("userid", MyApplication.prefs.getString(Constants.USER_ID,""))
                    return params
                }
            }
            //adding request to queue
            MyApplication.instance!!.addToRequestQueue(stringRequest)
        }
        catch (c : Exception)
        {
            T.e("Exception : " + c)
        }
    }

    private fun parseAccountResponse(response: String?) {


        var json = JSONTokener(response).nextValue();
        if (json is JSONObject)
        {
            var logoutJson = JSONObject(response)

            var success = logoutJson.getString("success")
            var message = logoutJson.getString("message")
            if(success.equals("0"))
            {
                accountAppDialog(message)
            }
            /*else if(success.equals("1"))
            {

            }*/

        }
        else
        {
            T.t("Incorrect json found")
        }
    }
    fun accountAppDialog(message : String)
    {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Account Deactivated")
            .setConfirmText("Ok")
            .setContentText(message)
            .setConfirmClickListener { sDialog ->
                sDialog.dismissWithAnimation()
               finish()
            }
            .show()
    }


    private fun setClickListner() {


        logout_button.setOnClickListener {


            logoutAppDialog()


        }
        submit_btn.setOnClickListener {


            proceedToStoreCatcha()

        }
        //reset
        reset_btn.setOnClickListener {

            MyApplication.editor.putString(Constants.CATCHA_ID,"0").commit()
            MyApplication.editor.putString(Constants.CORRECT_COUNT,"0").commit()
            MyApplication.editor.putString(Constants.INCORRECT_COUNT,"0").commit()
            MyApplication.editor.putString(Constants.ATTEMPTED_COUNT,"0").commit()
            MyApplication.editor.putString(Constants.NOT_ATTEMPTED_COUNT,"0").commit()
            setCount()
            T.s(mainLayout,"Success ! captcha reset successfully")

        }
        //start
        start_btn.setOnClickListener {

            if(T.isNetworkAvailable())
            {
                T.t("Success ! start successfully")
                //get captcha details
                var captchaId = MyApplication.prefs.getString(Constants.CATCHA_ID,"0")
                if(captchaId.equals("0"))
                {
                    //get catchaa details
                    getCatchaDetails("1");
                }
                else
                {

                    //get catchaa details
                    getCatchaDetails(captchaId);
                }

            }


        }

    }

    private fun proceedToStoreCatcha() {


        if(!T.validateEditext(mainLayout,catcha_edt,"Oops ! enter catcha"))
        {
            return
        }
        //check catchaa is correct or incorrect

        if(receivedCaptcha.equals(catcha_edt.text.toString()))
        {

            submitCaptcha("1")
        }
        else
        {
            submitCaptcha("0")
        }

    }

    private fun submitCaptcha(captchaAns : String) {


        try
        {
            var progressDialog = ProgressDialog(this);
            progressDialog.setMessage("Submitting captcha, please wait...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            val stringRequest = object : StringRequest
                (
                Request.Method.POST,
                Constants.WA_STORE_CAPTCHA,
                Response.Listener<String>
                {
                        response ->
                    progressDialog.dismiss()
                    parseStoreCatchaResponse(response)

                },
                object : Response.ErrorListener
                {
                    override fun onErrorResponse(volleyError: VolleyError)
                    {

                        progressDialog.dismiss()
                        submitCaptcha(captchaAns)
                        //return volley error
                        T.s(mainLayout,Constants.SOME_WRONG)

                    }
                }
            )
            {

                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String>
                {
                    val params = HashMap<String, String>()
                    params.put("userid", MyApplication.prefs.getString(Constants.USER_ID,""))
                    params.put("captchaid", MyApplication.prefs.getString(Constants.CATCHA_ID,"0"))
                    params.put("captchaans", captchaAns)
                    return params
                }
            }
            //adding request to queue
            MyApplication.instance!!.addToRequestQueue(stringRequest)
        }
        catch (c : Exception)
        {
            T.e("Exception : " + c)
        }

    }

    private fun parseStoreCatchaResponse(response: String?) {


        var json = JSONTokener(response).nextValue();
        if (json is JSONObject)
        {
            var logoutJson = JSONObject(response)

            var success = logoutJson.getString("success")
            var message = logoutJson.getString("message")
            if(success.equals("1"))
            {
                T.t(message)
                catcha_edt.setText("")
                var captch_id = MyApplication.prefs.getString(Constants.CATCHA_ID,"0")

                //get next catcha
                getCatchaDetails(""+(captch_id.toInt() + 1))

            }
            else
            {
                T.s(mainLayout,message)
            }

        }
        else
        {
            T.t("Incorrect json found")
        }

    }

    private fun logoutApp() {


        try
        {
            var progressDialog = ProgressDialog(this);
            progressDialog.setMessage("Logout, please wait...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            val stringRequest = object : StringRequest
                (
                Request.Method.POST,
                Constants.WA_LOGOUT,
                Response.Listener<String>
                {
                        response ->
                    progressDialog.dismiss()
                    parseResponse(response)

                },
                object : Response.ErrorListener
                {
                    override fun onErrorResponse(volleyError: VolleyError)
                    {

                        progressDialog.dismiss()
                        //return volley error
                        T.s(mainLayout,Constants.SOME_WRONG)

                    }
                }
            )
            {

                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String>
                {
                    val params = HashMap<String, String>()
                    params.put("userid", MyApplication.prefs.getString(Constants.USER_ID,""))
                    return params
                }
            }
            //adding request to queue
            MyApplication.instance!!.addToRequestQueue(stringRequest)
        }
        catch (c : Exception)
        {
            T.e("Exception : " + c)
        }
    }

    fun logoutAppDialog()
    {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Logout")
            .setConfirmText("Logout")
            .setCancelText("Cancel")
            .setContentText("Do you want to logout app.")
            .setConfirmClickListener { sDialog ->
                sDialog.dismissWithAnimation()

                //logout app here
                if(T.isNetworkAvailable())
                {
                    logoutApp()
                }
                else
                {
                    T.s(mainLayout, Constants.CONNECTION)
                }

            }
            .setCancelClickListener { sDialog ->
                sDialog.dismissWithAnimation()
            }
            .show()
    }

    private fun parseResponse(response: String?) {


        var json = JSONTokener(response).nextValue();
        if (json is JSONObject)
        {
            var logoutJson = JSONObject(response)

            var success = logoutJson.getString("success")
            var message = logoutJson.getString("message")
            if(success.equals("1"))
            {
                T.t(message)
                MyApplication.editor.clear().commit()
                var i = Intent(this,LoginActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
                finish()
            }
            else
            {
                T.s(mainLayout,message)
            }

        }
        else
        {
            T.t("Incorrect json found")
        }

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
            if(cnt == 1)
            {
               // cancelHandler();
                timer_txt.setText("Time Over")
                //get catcha again
                var captchaId = MyApplication.prefs.getString(Constants.CATCHA_ID,"0")
                //get catchaa details
                getCatchaDetails(""+(captchaId.toInt() + 1));
            }
            else if(cnt <= 5 )
            {
                if(cnt > 0)
                {
                    timer_txt.setText("Time left : "+cnt+" sec")
                    timer_txt.setTextColor(Color.parseColor("#ff3333"))
                }


            }
            else
            {
                timer_txt.setText("Time left : "+cnt+" sec")
                timer_txt.setTextColor(Color.parseColor("#00b33c"))
            }
            cnt--
            mdelayHandler.postDelayed(this, 1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelHandler()
    }
    private fun cancelHandler() {

        mIsRunning = false
        mdelayHandler.removeCallbacks(mStatusChecker)
    }
}
