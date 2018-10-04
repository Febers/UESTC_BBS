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
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.*
import com.febers.uestc_bbs.module.message.presenter.MessageContract
import com.febers.uestc_bbs.module.message.presenter.MsgPresenterImpl
import com.febers.uestc_bbs.utils.ViewClickUtils.clickToPostDetail
import com.febers.uestc_bbs.utils.ViewClickUtils.clickToUserDetail
import com.febers.uestc_bbs.utils.ViewClickUtils.clickToPM
import com.febers.uestc_bbs.view.adapter.*
import com.othershe.baseadapter.ViewHolder
import kotlinx.android.synthetic.main.fragment_sub_message.*

/**
 * 消息列表的Fragment，分为四种类型
 * 依次为帖子回复、私信、At和系统消息
 */
class MessageFragment : BaseFragment(), MessageContract.View {

    private val replyList: MutableList<MsgReplyBean.ListBean> = ArrayList()
    private val privateList: MutableList<MsgPrivateBean.BodyBean.ListBean> = ArrayList()
    private val atList: MutableList<MsgAtBean.ListBean> = ArrayList()
    private val systemList: MutableList<MsgSystemBean.BodyBean.DataBean> = ArrayList()
    private lateinit var messagePresenter: MessageContract.Presenter
    private lateinit var msgAdapter: MsgBaseAdapter
    private var page = 1

    override fun setContentView(): Int {
        return R.layout.fragment_sub_message
    }

    override fun initView() {
        messagePresenter = MsgPresenterImpl(this)
        when(mMsgType) {
            MSG_TYPE_REPLY -> msgAdapter = MsgReplyAdapter(context!!, replyList, false).apply {
                recyclerview_sub_message.adapter = this
                setOnItemClickListener { viewHolder, listBean, i ->
                    clickToPostDetail(context, activity, listBean.topic_id.toString()) }
                setOnItemChildClickListener(R.id.image_view_msg_reply_author_avatar) {
                    p0: ViewHolder?, p1: MsgReplyBean.ListBean?, p2: Int ->
                    clickToUserDetail(context, activity, p1?.user_id.toString())}
            }
            MSG_TYPE_PRIVATE -> msgAdapter = MsgPrivateAdapter(context!!, privateList, false).apply {
                recyclerview_sub_message.adapter = this
                setOnItemClickListener { viewHolder, listBean, i -> clickToPM(context, activity, listBean.toUserId.toString()) }
            }
            MSG_TYPE_AT -> msgAdapter = MsgAtAdapter(context!!, atList, false).apply {
                recyclerview_sub_message.adapter = this
                setOnItemClickListener { viewHolder, listBean, i ->
                    clickToPostDetail(context, activity, listBean.topic_id.toString())}
            }
            MSG_TYPE_SYSTEM -> msgAdapter = MsgSystemAdapter(context!!, systemList, false).apply {
                recyclerview_sub_message.adapter = this
            }
        }

        recyclerview_sub_message.apply {
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
        refresh_layout_sub_message.autoRefresh()
    }

    private fun getMsg(page: Int) {
        messagePresenter.msgRequest(type = mMsgType?: MSG_TYPE_PRIVATE, page = page)
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
        refresh_layout_sub_message.apply {
            finishRefresh(true)
            finishLoadMore(true)
            setEnableLoadMore(true)
        }
        val msgBean = event.data
        when(mMsgType) {
            MSG_TYPE_REPLY -> {
                msgBean as MsgReplyBean
                if (page == 1) {
                    (msgAdapter as MsgReplyAdapter).setNewData(msgBean.list)
                    return
                }
                if (event.code == BaseCode.SUCCESS_END) {
                    refresh_layout_sub_message.finishLoadMoreWithNoMoreData()
                }
                (msgAdapter as MsgReplyAdapter).setLoadMoreData(msgBean.list)
            }
            MSG_TYPE_PRIVATE -> {
                msgBean as MsgPrivateBean
                if (page == 1) {
                    (msgAdapter as MsgPrivateAdapter).setNewData(msgBean.body.list)
                    return
                }
                if (event.code == BaseCode.SUCCESS_END) {
                    refresh_layout_sub_message.finishLoadMoreWithNoMoreData()
                }
                (msgAdapter as MsgPrivateAdapter).setLoadMoreData(msgBean.body.list)
            }
            MSG_TYPE_AT -> {
                msgBean as MsgAtBean
                if (page == 1) {
                    (msgAdapter as MsgAtAdapter).setNewData(msgBean.list)
                    return
                }
                if (event.code == BaseCode.SUCCESS_END) {
                    refresh_layout_sub_message.finishLoadMoreWithNoMoreData()
                }
                (msgAdapter as MsgAtAdapter).setLoadMoreData(msgBean.list)
            }
            MSG_TYPE_SYSTEM -> {
                msgBean as MsgSystemBean
                if (page == 1) {
                    (msgAdapter as MsgSystemAdapter).setNewData(msgBean.body.data)
                    return
                }
                if (event.code == BaseCode.SUCCESS_END) {
                    refresh_layout_sub_message.finishLoadMoreWithNoMoreData()
                }
                (msgAdapter as MsgSystemAdapter).setLoadMoreData(msgBean.body.data)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(type: String) =
                MessageFragment().apply {
                    arguments = Bundle().apply {
                        putString(MSG_TYPE, type)
                    }
                }
    }
}