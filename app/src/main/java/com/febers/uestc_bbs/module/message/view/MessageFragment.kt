/*
 * Created by Febers at 18-8-14 上午1:46.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-14 上午1:46.
 */

package com.febers.uestc_bbs.module.message.view

import android.os.Bundle
import androidx.annotation.UiThread
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.*
import com.febers.uestc_bbs.module.message.contract.MessageContract
import com.febers.uestc_bbs.module.message.presenter.MsgPresenterImpl
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.module.context.ClickContext.clickToPostDetail
import com.febers.uestc_bbs.module.context.ClickContext.clickToUserDetail
import com.febers.uestc_bbs.module.context.ClickContext.clickToPrivateMsg
import com.febers.uestc_bbs.view.adapter.*
import com.febers.uestc_bbs.view.helper.finishFail
import com.febers.uestc_bbs.view.helper.finishSuccess
import com.febers.uestc_bbs.view.helper.initAttrAndBehavior
import com.othershe.baseadapter.ViewHolder
import kotlinx.android.synthetic.main.fragment_sub_message.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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
    private var loadFinish = false

    override fun registerEventBus(): Boolean = true

    override fun setView(): Int {
        return R.layout.fragment_sub_message
    }

    /**
     * 根据mMsgType，确定当前视图应该展示哪一种消息
     */
    override fun initView() {
        messagePresenter = MsgPresenterImpl(this)
        when(mMsgType) {
            MSG_TYPE_REPLY -> msgAdapter = MsgReplyAdapter(context!!, replyList, false).apply {
                recyclerview_sub_message.adapter = this
                setOnItemClickListener { viewHolder, listBean, i ->
                    clickToPostDetail(context, listBean.topic_id) }
                setOnItemChildClickListener(R.id.image_view_msg_reply_author_avatar) {
                    p0: ViewHolder?, p1: MsgReplyBean.ListBean?, p2: Int ->
                    clickToUserDetail(context, p1?.user_id)}
                setOnItemChildClickListener(R.id.image_view_msg_post_reply) {
                    viewHolder, listBean, i ->
                    ClickContext.clickToPostReply(context = activity,
                            toUserId = listBean.user_id,
                            toUserName = listBean.reply_nick_name,
                            postId = listBean.topic_id,
                            replyId = listBean.reply_remind_id,
                            isQuota = true,
                            replySimpleDescription = listBean.reply_content.toString())
                }
                setEmptyView(getEmptyViewForRecyclerView(recyclerview_sub_message))
            }
            MSG_TYPE_PRIVATE -> msgAdapter = MsgPrivateAdapter(context!!, privateList, false).apply {
                recyclerview_sub_message.adapter = this
                setOnItemClickListener { viewHolder, listBean, i ->
                    clickToPrivateMsg(context, listBean.toUserId, listBean.toUserName)
                    listBean.isNew = 0
                    this.notifyDataSetChanged()}
                setOnItemChildClickListener(R.id.image_view_msg_private_author_avatar) {
                    viewHolder, listBean, i -> clickToUserDetail(activity, listBean.toUserId)
                }
                setEmptyView(getEmptyViewForRecyclerView(recyclerview_sub_message))
            }
            MSG_TYPE_AT -> msgAdapter = MsgAtAdapter(context!!, atList, false).apply {
                recyclerview_sub_message.adapter = this
                setOnItemClickListener { viewHolder, listBean, i ->
                    clickToPostDetail(context, listBean.topic_id)}
                setOnItemChildClickListener(R.id.image_view_msg_at_author_avatar) {
                    viewHolder, listBean, i -> clickToUserDetail(activity, listBean.user_id)
                }
                setEmptyView(getEmptyViewForRecyclerView(recyclerview_sub_message))
            }
            MSG_TYPE_SYSTEM -> msgAdapter = MsgSystemAdapter(context!!, systemList, false).apply {
                recyclerview_sub_message.adapter = this
                setEmptyView(getEmptyViewForRecyclerView(recyclerview_sub_message))
            }
        }
        recyclerview_sub_message.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
        refresh_layout_sub_message.apply {
            initAttrAndBehavior()
            setOnRefreshListener {
                page = 1
                getMsg(page)
            }
            setOnLoadMoreListener {
                getMsg(++page)
            }
        }
        ThemeHelper.subscribeOnThemeChange(refresh_layout_sub_message)
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
        loadFinish = true
        refresh_layout_sub_message?.finishSuccess()
        val msgBean = event.data
        when(mMsgType) {
            MSG_TYPE_REPLY -> {
                msgBean as MsgReplyBean
                EventBus.getDefault().post(MsgFeedbackEvent(BaseCode.SUCCESS, MSG_TYPE_REPLY)) //隐藏通知
                if (page == 1) {
                    (msgAdapter as MsgReplyAdapter).setNewData(msgBean.list)
                    return
                }
                (msgAdapter as MsgReplyAdapter).setLoadMoreData(msgBean.list)
            }
            MSG_TYPE_PRIVATE -> {
                msgBean as MsgPrivateBean
                EventBus.getDefault().post(MsgFeedbackEvent(BaseCode.SUCCESS, MSG_TYPE_PRIVATE))
                if (page == 1) {
                    (msgAdapter as MsgPrivateAdapter).setNewData(msgBean.body?.list)
                    return
                }
                (msgAdapter as MsgPrivateAdapter).setLoadMoreData(msgBean.body?.list)
            }
            MSG_TYPE_AT -> {
                msgBean as MsgAtBean
                EventBus.getDefault().post(MsgFeedbackEvent(BaseCode.SUCCESS, MSG_TYPE_AT))
                if (page == 1) {
                    (msgAdapter as MsgAtAdapter).setNewData(msgBean.list)
                    return
                }
                (msgAdapter as MsgAtAdapter).setLoadMoreData(msgBean.list)
            }
            MSG_TYPE_SYSTEM -> {
                msgBean as MsgSystemBean
                EventBus.getDefault().post(MsgFeedbackEvent(BaseCode.SUCCESS, MSG_TYPE_SYSTEM))
                if (page == 1) {
                    (msgAdapter as MsgSystemAdapter).setNewData(msgBean.body?.data)
                    return
                }
                (msgAdapter as MsgSystemAdapter).setLoadMoreData(msgBean.body?.data)
            }
        }
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_sub_message?.finishLoadMoreWithNoMoreData()
        }
    }

    override fun showError(msg: String) {
        showHint(msg)
        refresh_layout_sub_message?.finishFail()
    }

    /**
     * 接收BottomNavigation的二次点击事件
     * 当当前页面正在显示的时候，刷新
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTabReselected(event: TabReselectedEvent) {
        if (isSupportVisible && loadFinish && event.position == 2) {
            scroll_view_sub_message?.scrollTo(0, 0)
            refresh_layout_sub_message?.autoRefresh()
        }
    }

    /**
     *  当后台Service接收到新消息时，此方法会接受到相应的消息
     *  如果正在显示，则刷新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiveNewMsg(event: MsgEvent) {
        if (userVisibleHint && event.type == mMsgType) {
            refresh_layout_sub_message?.autoRefresh()
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