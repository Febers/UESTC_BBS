package com.febers.uestc_bbs.module.webview

import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.URL
import com.febers.uestc_bbs.module.webview.WebViewConfiguration
import kotlinx.android.synthetic.main.activity_browser.*
import kotlinx.android.synthetic.main.layout_toolbar_common.*

class BrowserActivity : BaseActivity() {

    private lateinit var url: String

    override fun setView(): Int {
        url = intent.getStringExtra(URL) ?: ""
        return R.layout.activity_browser
    }

    override fun setToolbar(): Toolbar? = toolbar_common

    override fun initView() {
        toolbar_common.title = url
        WebViewConfiguration.Configuration(mContext, web_view_app)
                .setOpenUrlOut(false)
                .setJavaScriptEnabled(true)
                .setAppCacheEnabled(true)
                .acceptAnyRequest(true)
                .setSupportProgressBar(true, progress_bar_web_app)
                .configure()
        web_view_app.loadUrl(url)
    }
}
