/*
 * Created by Febers at 18-8-14 上午1:29.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-14 下午4:51.
 */

package com.febers.uestc_bbs.view.custom

import android.content.Context
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView

import com.febers.uestc_bbs.R

class CustomLoginDialog(private val mContext: Context) : AlertDialog(mContext, R.style.Theme_AppCompat_Dialog) {
    private val tieUserId: TextInputEditText
    private val tieUserPw: TextInputEditText
    private val tvTitle: TextView
    private val dialog: AlertDialog
    private val mView: View
    private val btCancel: Button
    private val btEnter: Button
    private var loginListener: OnLoginListener? = null
    var stId = ""
        private set
    var stPw = ""
        private set

    init {
        dialog = AlertDialog.Builder(mContext).create()
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_login, null)
        tvTitle = mView.findViewById(R.id.tv_dialog_title)
        tieUserId = mView.findViewById(R.id.tie_user_id)
        tieUserId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                stId = s.toString()
            }
        })
        tieUserPw = mView.findViewById(R.id.tie_user_pw)
        tieUserPw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                stPw = s.toString()
            }
        })
        btEnter = mView.findViewById(R.id.bt_dialog_login_enter)
        btCancel = mView.findViewById(R.id.bt_dialog_login_cancel)
        btEnter.setOnClickListener { view -> loginListener!!.onClick(view) }
        btCancel.setOnClickListener { view -> loginListener!!.onClick(view) }
        dialog.setView(mView)
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }

    fun setShowTitle(show: Boolean?) {
        if (show!!) {
            tvTitle.visibility = View.VISIBLE
        } else {
            tvTitle.visibility = View.INVISIBLE
        }
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

    //定义点击监听接口
    interface OnLoginListener {
        fun onClick(v: View)
    }

    fun setOnLoginListener(onLoginListener: OnLoginListener) {
        loginListener = onLoginListener
    }
}
