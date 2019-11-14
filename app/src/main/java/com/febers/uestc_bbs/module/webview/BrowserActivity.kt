package com.febers.uestc_bbs.module.webview

import android.content.Context
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.URL
import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.module.webview.listener.OnReceivedTitleListener
import com.febers.uestc_bbs.utils.WebViewUtils
import kotlinx.android.synthetic.main.activity_browser.*

class BrowserActivity : BaseActivity() {

    private lateinit var url: String

    override fun setView(): Int {
        url = intent.getStringExtra(URL) ?: ""
        return R.layout.activity_browser
    }

    override fun initView() {
        tv_title_browser.text = url
        WebViewConfiguration.Configuration(mContext, web_view_app)
                .setOpenUrlOut(false)
                .setJavaScriptEnabled(true)
                .setAppCacheEnabled(true)
                .setDomEnabled(true)
                .acceptAnyRequest(true)
                .setSupportProgressBar(true, progress_bar_web_app)
                .addOnReceivedTitleListener(object : OnReceivedTitleListener {
                    override fun onReceived(title: String) {
                        tv_title_browser.text = title
                    }
                })
                .configure()
        web_view_app.loadUrl(url)

        iv_back_browser.setOnClickListener { finish() }
    }

    override fun onBackPressed() {
        if (web_view_app.canGoBack()) {
            web_view_app.goBack()
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        WebViewUtils.destroyWebView(web_view_app)
    }
}