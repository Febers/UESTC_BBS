package com.febers.uestc_bbs.view.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.febers.uestc_bbs.R

class UpdateDialog(context: Context, title: String, time: String = "", size: String = "", body: String) : AlertDialog(context, R.style.Theme_AppCompat_Dialog) {

    private lateinit var dialog: AlertDialog
    private lateinit var tvTitle: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvSize: TextView
    private lateinit var tvBody: TextView
    private lateinit var pbDownload: ProgressBar
    private lateinit var btnLeft: Button
    private lateinit var btnCenter: Button
    private lateinit var btnRight: Button

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_update, null)
        tvTitle = view.findViewById(R.id.tv_update_dialog_title)
        tvTime = view.findViewById(R.id.tv_update_dialog_time)
        tvSize = view.findViewById(R.id.tv_update_dialog_size)
        tvBody = view.findViewById(R.id.tv_update_dialog_body)
        pbDownload = view.findViewById(R.id.progress_bar_update_dialog)
        tvTitle.text = title
        tvTime.text = time
        tvSize.text = size
        tvBody.text = body
        btnLeft = view.findViewById(R.id.btn_update_dialog_left)
        btnCenter = view.findViewById(R.id.btn_update_dialog_center)
        btnRight = view.findViewById(R.id.btn_update_dialog_right)
        dialog = AlertDialog.Builder(context).create()
        dialog.setView(view)
    }

    override fun show() {
        dialog.show()
    }

    override fun dismiss() {
        dialog.dismiss()
    }

    override fun hide() {
        dialog.dismiss()
    }

    fun setOnClickLeftButtonListener(title: String, listener: ButtonClickListener) {
        btnLeft.apply {
            visibility = View.VISIBLE
            text = title
            setOnClickListener { listener.onClick() }
        }
    }

    fun setOnClickCenterButtonListener(title: String, listener: ButtonClickListener) {
        btnCenter.apply {
            visibility = View.VISIBLE
            text = title
            setOnClickListener { listener.onClick() }
        }
    }

    fun setOnClickRightButtonListener(title: String, listener: ButtonClickListener) {
        btnRight.apply {
            visibility = View.VISIBLE
            text = title
            setOnClickListener { listener.onClick() }
        }
    }

    fun setLeftButtonText(title: String) {
        btnLeft.text = title
    }

    fun setCenterButtonText(title: String) {
        btnCenter.text = title
    }

    fun setRightButtonText(title: String) {
        btnRight.text = title
    }

    fun setDownloadProgress(progress: Int) {
        pbDownload.visibility = View.VISIBLE
        pbDownload.progress = progress
    }

    @FunctionalInterface
    interface ButtonClickListener {
        fun onClick()
    }
}