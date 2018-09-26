/*
 * Created by Febers at 18-8-14 上午1:45.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-14 上午1:45.
 */

package com.febers.uestc_bbs.module.message.view

import android.os.Bundle
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.MsgBaseBean
import com.febers.uestc_bbs.module.message.presenter.MsgContract
import com.febers.uestc_bbs.module.message.presenter.MsgPresenterImpl
import com.febers.uestc_bbs.view.adapter.MsgReplyAdapter

/**
 * 私信通知
 */
class MsgPrivateFragment : BaseFragment(), MsgContract.View {
    private lateinit var msgPresenter: MsgContract.Presenter
    private lateinit var msgAdapter: MsgReplyAdapter

    override fun setContentView(): Int {
        return R.layout.fragment_sub_message
    }

    override fun initView() {
        msgPresenter = MsgPresenterImpl(this)
    }

    private fun getMsg(page: Int) {
        msgPresenter.msgRequest(type = MSG_TYPE_PRIVATE, page = page)
    }

    override fun <M : MsgBaseBean> showMessage(event: BaseEvent<M>) {

    }

    companion object {
        @JvmStatic
        fun newInstance(type: String) =
                MsgReplyFragment().apply {
                    arguments = Bundle().apply {
                        putString(MSG_TYPE, type)
                    }
                }
    }
}