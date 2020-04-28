package com.thoughtworks.xinguanmap

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.ViewGroup
import android.webkit.JsResult
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class ChinaMapView(context: Context?) : WebView(context) {

    private var dataUrl = ""
    init {
        settings.javaScriptEnabled = true
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                requestData()
            }
        }
        settings.defaultTextEncodingName = "utf-8";
        layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    fun setDataUrl(url: String) {
        this.dataUrl = url
        if (this.dataUrl.isNotEmpty()) {
           loadUrl("file:///android_asset/echarts/test/map-default.html")
        }
    }

    private fun requestData() {
        val client = OkHttpClient.Builder().build()
        val call = client.newCall(
            Request.Builder()
            .url(this.dataUrl)
            .get()
            .build())
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onResponse(call: Call, response: Response) {
                val data = Gson().fromJson(response.body?.string(), ResponseData::class.java)
                (context as Activity).runOnUiThread {
                    evaluateJavascript("javascript:setOption(${Gson().toJson(data.newslist.map { JsData(it.provinceShortName, it.confirmedCount) })})", ValueCallback {

                    })
                }
            }
        })
    }
}