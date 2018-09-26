/*
 * Created by Febers at 18-8-14 上午1:46.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-14 上午1:46.
 */

package com.febers.uestc_bbs.module.message.view

import android.os.Bundle
import android.support.annotation.UiThread
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log.i
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.MsgBaseBean
import com.febers.uestc_bbs.entity.MsgReplyBean
import com.febers.uestc_bbs.module.message.presenter.MsgContract
import com.febers.uestc_bbs.module.message.presenter.MsgPresenterImpl
import com.febers.uestc_bbs.view.adapter.MsgReplyAdapter
import kotlinx.android.synthetic.main.fragment_sub_message.*

/**
 * 帖子回复
 */
class MsgReplyFragment : BaseFragment(), MsgContract.View {

    private val repliesList: MutableList<MsgReplyBean.ListBean> = ArrayList()
    private lateinit var msgPresenter: MsgContract.Presenter
    private lateinit var msgAdapter: MsgReplyAdapter
    private var page = 1

    override fun setContentView(): Int {
        return R.layout.fragment_sub_message
    }

    override fun initView() {
        msgPresenter = MsgPresenterImpl(this)
        msgAdapter = MsgReplyAdapter(context!!, repliesList, false)
        recyclerview_sub_message.apply {
            adapter = msgAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
        refresh_layout_sub_message.apply {
            setEnableLoadMore(false)
            setOnRefreshListener {
                page = 1
                getMsg(page)
            }
            setOnLoadMoreListener {
                getMsg(++page)
            }
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        i("MsgF", "lazy")
        refresh_layout_sub_message.autoRefresh()
    }

    private fun getMsg(page: Int) {
        msgPresenter.msgRequest(type = MSG_TYPE_REPLY, page = page)
    }

    @UiThread
    override fun <M : MsgBaseBean> showMessage(event: BaseEvent<M>) {
        if (event.code == BaseCode.FAILURE) {
            onError(""+(event.data as MsgReplyBean).errcode)
            refresh_layout_sub_message.apply {
                finishRefresh(false)
                finishLoadMore(false)
                return
            }
        }
        val replyBean = event.data as MsgReplyBean
        refresh_layout_sub_message.apply {
            finishRefresh(true)
            finishLoadMore(true)
            setEnableLoadMore(true)
        }
        if (page == 1) {
            msgAdapter.setNewData(replyBean.list)
            return
        }
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_sub_message.finishLoadMoreWithNoMoreData()
        }
        msgAdapter.setLoadMoreData(replyBean.list)
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