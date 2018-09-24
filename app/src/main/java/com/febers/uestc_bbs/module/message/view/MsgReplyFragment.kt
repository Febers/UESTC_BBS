/*
 * Created by Febers at 18-8-14 上午1:46.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-14 上午1:46.
 */

package com.febers.uestc_bbs.module.message.view

import android.os.Bundle
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BaseFragment
import com.febers.uestc_bbs.base.MSG_TYPE
import com.febers.uestc_bbs.base.MSG_TYPE_REPLY
import com.febers.uestc_bbs.entity.MsgBaseBean
import com.febers.uestc_bbs.module.message.presenter.MsgContract
import com.febers.uestc_bbs.module.message.presenter.MsgPresenterImpl
import com.febers.uestc_bbs.view.adapter.MsgReplyAdapter

/**
 * 帖子回复
 */
class MsgReplyFragment : BaseFragment(), MsgContract.View {

    private lateinit var msgPresenter: MsgContract.Presenter
    private lateinit var msgAdapter: MsgReplyAdapter

    override fun setContentView(): Int {
        return R.layout.fragment_sub_message
    }

    override fun initView() {
        msgPresenter = MsgPresenterImpl(this)
    }

    private fun getMsg(page: Int) {
        msgPresenter.msgRequest(type = MSG_TYPE_REPLY, page = page)
    }

    override fun showMessage(event: BaseEvent<MsgBaseBean>) {

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