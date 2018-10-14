package com.febers.uestc_bbs.module.message.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.entity.MsgBaseBean
import com.febers.uestc_bbs.entity.PMDetailBean
import com.febers.uestc_bbs.entity.PMSendResultBean

interface MessageContract {

    interface Model {
        fun msgService(type: String, page: Int)
    }

    interface View: BaseView {
        fun <M: MsgBaseBean> showMessage(event: BaseEvent<M>)
    }

    abstract class Presenter(view: MessageContract.View): BasePresenter<MessageContract.View>(view) {
        abstract fun msgRequest(type: String, page: Int)
        abstract fun <M :MsgBaseBean> msgResult(event: BaseEvent<M>)
    }

    interface PMModel {
        fun pmSessionService(uid: Int, page: Int)
        fun pmSendService(content: Any, type: String)
    }

    interface PMView: BaseView {
        fun showPMSession(event: BaseEvent<PMDetailBean>)
        fun showPMSendResult(event: BaseEvent<PMSendResultBean>)
    }

    abstract class PMPresenter(view: MessageContract.PMView): BasePresenter<MessageContract.PMView>(view) {
        abstract fun pmSessionRequest(uid: Int, page: Int)
        abstract fun pmSessionResult(event: BaseEvent<PMDetailBean>)
        abstract fun pmSendRequest(content: Any, type: String)
        abstract fun pmSendResult(event: BaseEvent<PMSendResultBean>)

    }
}