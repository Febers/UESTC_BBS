package com.febers.uestc_bbs.module.webview

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.URL
import com.febers.uestc_bbs.module.webview.listener.OnReceivedTitleListener
import com.febers.uestc_bbs.utils.WebViewUtils
import kotlinx.android.synthetic.main.activity_browser.*
import me.yokeyword.fragmentation.SwipeBackLayout
import org.jetbrains.anko.browse
import org.jetbrains.anko.toast

class BrowserActivity : BaseActivity() {

    private lateinit var url: String

    override fun setView(): Int {
        url = intent.getStringExtra(URL) ?: ""
        return R.layout.activity_browser
    }

    override fun setToolbar(): Toolbar? = toolbar_browser

    override fun setMenu(): Int? = R.menu.menu_browser

    override fun initView() {
        setEdgeLevel(SwipeBackLayout.EdgeLevel.MIN)
        tv_title_browser.text = url
        WebViewConfiguration.Configuration(ctx, web_view_app)
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
        web_view_app.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            browse(url)
            if (this.url == url) {  //如果只是打开了一个下载界面
                finish()
            }
        }
    }

    override fun onBackPressed() {
        if (web_view_app.canGoBack()) {
            web_view_app.goBack()
        } else {
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_item_open_out -> browse(url)
            R.id.menu_item_copy_url -> {
                val clipboardManager: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("url", url)
                clipboardManager.setPrimaryClip(clipData)
                toast(getString(R.string.copy_url_successfully))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        WebViewUtils.destroyWebView(web_view_app)
    }
}