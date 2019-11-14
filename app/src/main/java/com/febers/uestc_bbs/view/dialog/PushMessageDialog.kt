package com.febers.uestc_bbs.view.dialog

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.PushMessageBean
import com.febers.uestc_bbs.module.setting.push.PushManager
import com.febers.uestc_bbs.module.setting.push.PushMessageListener
import com.febers.uestc_bbs.utils.colorAccent
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.runOnUiThread
import java.lang.StringBuilder

class PushMessageDialog(var activity: Activity): AlertDialog(activity, R.style.Theme_AppCompat_DayNight_Dialog) {

    private var dialog: AlertDialog
    private var progressBar: ProgressBar
    private var tvPushMessage: TextView
    private var btnEnter: Button
    private var btnRetry: Button

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_push_msg, null)
        progressBar = view.findViewById(R.id.progress_bar_push_dialog)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.progressTintList = ColorStateList.valueOf(colorAccent())
        }
        tvPushMessage = view.findViewById(R.id.tv_push_dialog)
        btnEnter = view.findViewById(R.id.btn_enter_push_dialog)
        btnEnter.setOnClickListener { dismiss() }
        btnEnter.setTextColor(colorAccent())
        btnRetry = view.findViewById(R.id.btn_retry_push_dialog)
        btnRetry.setOnClickListener { getPushMessage() }
        btnRetry.setTextColor(colorAccent())
        dialog = Builder(context).setCancelable(false).create()
        dialog.setView(view)
    }

    /**
     * @deprecated 应该使用带参数的show方法
     */
    override fun show() {
        dialog.show()
        getPushMessage()
    }

    fun show(msg: String? = null) {
        if (activity.isDestroyed) return
        dialog.show()
        if (msg == null) {
            getPushMessage()
        } else {
            progressBar.visibility = View.GONE
            tvPushMessage.text = Html.fromHtml(msg.replace("/n", "<br>"))
        }
    }

    private fun getPushMessage() {
        progressBar.visibility = View.VISIBLE
        PushManager.getHttpMessages(object : PushMessageListener {
            override fun success(message: PushMessageBean) {
                context.runOnUiThread {
                    progressBar.visibility = View.GONE
                    if (message.msg.isNullOrEmpty()) {
                        tvPushMessage.text = "推送消息为空"
                    } else {
                        val sb = StringBuilder()
                        message.msg!!.forEachWithIndex { index, m ->
                            sb.append("<h4>#${index+1}</h4><p>").append(m.text).append("</p>").append("<br><br>")
                        }
                        tvPushMessage.text = Html.fromHtml(sb.toString().replace("/n", "<br>"))
                    }
                }
            }

            override fun fail(message: String) {
                context.runOnUiThread {
                    progressBar.visibility = View.GONE
                    tvPushMessage.text = message
                    if (btnRetry.visibility == View.GONE) {
                        btnRetry.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    override fun dismiss() {
        dialog.dismiss()
    }

    override fun hide() {
        dialog.dismiss()
    }
}