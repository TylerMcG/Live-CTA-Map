package com.McGregor.chicagotraintracker.UI


import android.annotation.SuppressLint
import android.util.Log
import android.os.Bundle
import com.McGregor.chicagotraintracker.R
import android.view.LayoutInflater
import android.view.ViewGroup
import com.McGregor.chicagotraintracker.MainActivity
import android.webkit.WebViewClient
import android.webkit.WebView

import android.view.View
import androidx.fragment.app.Fragment
import java.util.*

class AlertFragment : Fragment() {
    private var webViewState: Bundle? = null
    private var webViewClient: WebViewClient? = null
    private var webView: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webViewClient = WebViewClient()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mainActivity = activity as MainActivity?
        Objects.requireNonNull(mainActivity)!!.bottomNavigationView!!.findViewById<View>(R.id.trainsFragment).isEnabled = false
        mainActivity!!.bottomNavigationView!!.findViewById<View>(R.id.stations).isEnabled = false
        val view = inflater.inflate(R.layout.fragment_alert, container, false)
        webView = view.findViewById(R.id.webview)
        val webSettings = webView?.settings
        webSettings?.javaScriptEnabled = true
        webView?.webViewClient = webViewClient!!
        if (webViewState == null) {
            webView?.loadUrl("https://www.transitchicago.com/travel-information/railstatus/")
        } else {
            webView?.restoreState(webViewState!!)
        }
        return view
    }

    override fun onPause() {
        webViewState = Bundle()
        Log.d(TAG, "Alert Pause")
        webView!!.saveState(webViewState!!)
        super.onPause()
    }

    fun handleGoBack() {
        if (webView != null && webView!!.canGoBack()) {
            webView!!.goBack()
        }
    }

    companion object {
        private const val TAG = "ALERT_FRAG"
    }
}