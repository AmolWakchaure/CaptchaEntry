package com.example.captchaentry.volley

import com.android.volley.VolleyError

interface VolleyCallbacks {

    fun volleySuccessResponse(response : String)
    fun volleyErrorResponse(volleyError: VolleyError)
}