package com.febers.uestc_bbs.module.post.view.bottom_sheet

import android.content.Context
import android.net.http.SslError
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentManager
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.utils.log
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_bottom_sheet_post_web.*
import org.jetbrains.anko.browse


class PostWebViewBottomSheet(context: Context, style: Int, private val url: String): BottomSheetDialogFragment() {

    private lateinit var webViewClient: WebViewClient
    private lateinit var webChromeClient: WebChromeClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.dialog_bottom_sheet_post_web, container)
        return view
    }

    override fun onStart() {
        super.onStart()

        try {
            if (dialog != null) {
                val bottomSheet = dialog!!.findViewById<View>(R.id.design_bottom_sheet)
                bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            val view = view
            view?.post {
                val parent = view.parent as View
                val params = (parent).layoutParams as CoordinatorLayout.LayoutParams
                val behavior = params.behavior as CoordinatorLayout.Behavior
                val bottomSheetBehavior = behavior as BottomSheetBehavior
                bottomSheetBehavior.peekHeight = view.measuredHeight
            }
        } catch (e: Exception) {
            log(e.toString())
        }
        btn_open_in_browser.setOnClickListener { context?.browse(url) }
        btn_close_web_bottom_sheet.setOnClickListener { dismiss() }

        webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }
        }
        webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100) {
                    progress_bar_post_detail_web_view.visibility = View.GONE
                } else {
                    progress_bar_post_detail_web_view.progress = newProgress
                    progress_bar_post_detail_web_view.visibility = View.VISIBLE
                }
            }
        }
        val webSettings = web_view_post_detail.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true

        CookieSyncManager.createInstance(web_view_post_detail.context)
        CookieSyncManager.getInstance().sync()

        web_view_post_detail.webViewClient = webViewClient
        web_view_post_detail.webChromeClient = webChromeClient
        web_view_post_detail.setDownloadListener { p0, p1, p2, p3, p4 ->
            //注意典型的下载链接后面跟着一个无效参数，删去之后才能下载
            //http://bbs.uestc.edu.cn/forum.php?mod=attachment&aid=MTk0NTcyNnxiYzNiYjMyOXwxNTYyMTQzNzg0fDE5NjQ4NnwxNzY4Njc2&mobile=2
            context?.browse(getDownloadUrl(p0+""))
        }
        web_view_post_detail.loadUrl(url)
        web_view_post_detail.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
                if (p2?.action == KeyEvent.ACTION_DOWN) {
                    if (p1 == KeyEvent.KEYCODE_BACK && web_view_post_detail.canGoBack()) {
                        web_view_post_detail.goBack()
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun getDownloadUrl(url: String): String =
        try {
            val index = url.lastIndexOf("&mobile")
            url.substring(0, index)
        } catch (e: Exception) {
            ""
        }

}
