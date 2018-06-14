/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.view

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar

import com.febers.uestc_bbs.R


class CustomProgressDialog @JvmOverloads constructor(private val mContext: Context,
                                                     title: String = "请稍侯") :
        AlertDialog(mContext, R.style.Theme_AppCompat_Dialog) {

    private val view: View
    private val dialog: AlertDialog
    private val progressBar: ProgressBar

    init {
        dialog = AlertDialog.Builder(mContext).create()
        view = LayoutInflater.from(mContext).inflate(R.layout.dialog_progress, null)
        progressBar = view.findViewById(R.id.pb_progress)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setTitle(title)
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
