package com.example.captchaentry.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.WindowManager
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.example.captchaentry.R
import com.example.captchaentry.classes.T
import com.example.captchaentry.interfaces.Constants
import com.example.captchaentry.volley.ApiCall
import com.example.captchaentry.volley.VolleyCallbacks
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)


        toolbarTitle.setText("Please, login here")
        //set click listner
        setClickListner()
    }

    private fun setClickListner()
    {
        //login
        login_btn.setOnClickListener {


            processLogin();


        }
        //forgotPass_txt
        forgotPass_txt.setOnClickListener {

            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }
    }

    private fun processLogin()
    {
        //validate data
        if(!T.validateEditext(mainLayout,username_edt,"Oops ! enter username"))
        {
            return
        }
        if(!T.validateEditext(mainLayout,password_edt,"Oops ! enter password"))
        {
            return
        }

        if(T.isNetworkAvailable())
        {

            val loginParams = arrayOf<String>(

                "username#"+username_edt.text.toString(),
                "password#"+password_edt.text.toString()

            )
            //call api for authenticate user
            ApiCall.callApi(
                this,
                Constants.WA_LOGIN,
                "Authenticating...",
                "F",
                object : VolleyCallbacks
                {
                    override fun volleySuccessResponse(response: String)
                    {

                    }
                    override fun volleyErrorResponse(volleyError: VolleyError)
                    {
                        T.s(mainLayout,Constants.SOME_WRONG)
                    }
                },
                loginParams
            )
        }
        else
        {
            T.s(mainLayout,Constants.CONNECTION)
        }
        T.t("Successfully login")
        startActivity(Intent(this,MainActivity::class.java))
    }
}
