package com.febers.uestc_bbs.module.message.view

import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.view.adapter.MsgPagerAdapter
import kotlinx.android.synthetic.main.fragment_msg_pager.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MsgPagerFragment: BaseFragment() {

    override fun setView(): Int {
        return R.layout.fragment_msg_pager
    }

    override fun registerEventBus(): Boolean = true

    override fun initView() {
        val messageViewPageAdapter = MsgPagerAdapter(childFragmentManager)
        view_pager_message.adapter = messageViewPageAdapter
        view_pager_message.offscreenPageLimit = 4
        tab_layout_message.setupWithViewPager(view_pager_message)
    }

//    override fun onSupportVisible() {
//        super.onSupportVisible()
//        if (isSupportVisible && MyApp.msgCount > 0) {
//            MyApp.msgCount = 0
//            EventBus.getDefault().post(MsgFeedbackEvent(BaseCode.SUCCESS, MSG_TYPE_ALL))
//        }
//    }

    /**
     *  当后台Service接收到新消息时，此方法会接受到相应的消息
     *  随后更改当前的pager页面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiveNewMsg(event: MsgEvent) {
        view_pager_message?.setCurrentItem(when(event.type) {
            MSG_TYPE_REPLY -> 0
            MSG_TYPE_PRIVATE -> 1
            MSG_TYPE_AT -> 2
            MSG_TYPE_SYSTEM -> 3
            else -> 0
        }, false)
    }
}