package com.example.captchaentry.volley

import android.app.ProgressDialog
import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.example.captchaentry.classes.MyApplication
import com.example.captchaentry.classes.T
import com.example.captchaentry.interfaces.Constants
import java.util.HashMap

class ApiCall
{
    companion object
    {

        // call api
        fun callApi(
            context : Context,
            apiUrl : String,
            message : String,
            dialogFlag : String,
            volleyCallback : VolleyCallbacks,
            paramsArray : Array<String>)
        {
            try
            {
                var progressDialog = ProgressDialog(context);
                progressDialog.setMessage(message)
                progressDialog.setCancelable(false)

                if(dialogFlag.equals("F"))
                {
                    progressDialog.show()

                }

                // F.e("Constants.LOGIN : "+Constants.LOGIN)
                val stringRequest = object : StringRequest
                    (
                    Request.Method.POST,
                    apiUrl,
                    Response.Listener<String>
                    {
                        response ->
                        if(dialogFlag.equals("F"))
                        {
                            progressDialog.dismiss()
                        }
                        //return volley response
                        volleyCallback.volleySuccessResponse(response)

                    },
                    object : Response.ErrorListener
                    {
                        override fun onErrorResponse(volleyError: VolleyError)
                        {
                            if(dialogFlag.equals("F"))
                            {
                                progressDialog.dismiss()

                            }
                            //return volley error
                            volleyCallback.volleyErrorResponse(volleyError)

                        }
                    }
                )
                {

                    @Throws(AuthFailureError::class)
                    override fun getParams(): Map<String, String>
                    {
                        val params = HashMap<String, String>()
                        for (i in 0.. paramsArray.size)
                        {
                            var param = paramsArray.get(i).split("#")
                            params.put(param.get(0), param.get(1))
                        }
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

    }
}