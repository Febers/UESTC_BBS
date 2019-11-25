package com.febers.uestc_bbs.module.message.contract

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.mvp.BasePresenter
import com.febers.uestc_bbs.base.mvp.BaseView
import com.febers.uestc_bbs.entity.MsgBaseBean
import com.febers.uestc_bbs.entity.PMDetailBean
import com.febers.uestc_bbs.entity.PMSendResultBean

interface MessageContract {

    interface View: BaseView {
        fun <M: MsgBaseBean> showMessage(event: BaseEvent<M>)
    }

    abstract class Presenter(view: View): BasePresenter<View>(view) {
        abstract fun msgRequest(type: String, page: Int)
    }

    interface PMView: BaseView {

        fun showPMSession(event: BaseEvent<PMDetailBean>)

        fun showPMSendResult(event: BaseEvent<PMSendResultBean>)
    }

    abstract class PMPresenter(view: PMView): BasePresenter<PMView>(view) {

        abstract fun pmSessionRequest(uid: Int, page: Int, beginTime: Long = 0)

        abstract fun pmSendRequest(content: Any, type: String)

    }
}