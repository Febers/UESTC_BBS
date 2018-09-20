/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.view.custom

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView

import com.febers.uestc_bbs.R


class CustomProgressDialog @JvmOverloads constructor(mContext: Context,
                                                     title: String = "请稍侯") :
        AlertDialog(mContext, R.style.Theme_AppCompat_Dialog) {

    private val view: View = LayoutInflater.from(mContext).inflate(R.layout.dialog_progress, null)
    private val dialog: AlertDialog = AlertDialog.Builder(mContext).create()
    private val progressBar: ProgressBar
    private val titleText: TextView

    init {
        progressBar = view.findViewById(R.id.pb_progress)
        titleText = view.findViewById(R.id.progress_dialog_title)
        titleText.text = title
        dialog.setCanceledOnTouchOutside(false)
        dialog.setView(view)
    }

    override fun show() {
        dialog.show()
    }

    override fun hide() {
        dialog.hide()
    }

    override fun dismiss() {
        dialog.dismiss()
    }

    override fun isShowing(): Boolean {
        return dialog.isShowing
    }
}
