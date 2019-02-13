package com.example.captchaentry.activity

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.WindowManager
import com.example.captchaentry.R
import com.example.captchaentry.classes.T
import com.example.captchaentry.interfaces.Constants
import com.example.captchaentry.volley.ApiCall
import com.example.captchaentry.volley.VolleyCallbacks
import kotlinx.android.synthetic.main.activity_login.*
import android.provider.Settings;
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.example.captchaentry.classes.MyApplication
import org.json.JSONObject
import org.json.JSONTokener
import java.util.HashMap

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
            proceedLogin()

        }
        else
        {
            T.s(mainLayout,Constants.CONNECTION)
        }

    }

    private fun proceedLogin() {

        //get device id
        var device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID + "");
        try
        {
            var progressDialog = ProgressDialog(this);
            progressDialog.setMessage("Authenticating...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            val stringRequest = object : StringRequest
                (
                Request.Method.POST,
                Constants.WA_LOGIN,
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
                    params.put("username", username_edt.text.toString())
                    params.put("password", password_edt.text.toString())
                    params.put("device_id", device_id)
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

    private fun parseResponse(response: String)
    {

        var json = JSONTokener(response).nextValue();
        if (json is JSONObject)
        {
            var loginJson = JSONObject(response)

            var success = loginJson.getString("success")
            var message = loginJson.getString("message")
            if(success.equals("1"))
            {
                T.t(message)
                var userid = loginJson.getString("userid")
                MyApplication.editor.putString(Constants.USER_NAME,username_edt.text.toString()).commit()
                MyApplication.editor.putString(Constants.USER_ID,userid).commit()

                startActivity(Intent(this,MainActivity::class.java))
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
}
