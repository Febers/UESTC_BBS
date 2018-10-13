package com.febers.uestc_bbs.module.message.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.entity.MsgBaseBean
import com.febers.uestc_bbs.entity.PMDetailBean

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
        fun pmService(uid: Int, page: Int)
    }

    interface PMView: BaseView {
        fun showPMDetail(event: BaseEvent<PMDetailBean>)
    }

    abstract class PMPresenter(view: MessageContract.PMView): BasePresenter<MessageContract.PMView>(view) {
        abstract fun pmRequest(uid: Int, page: Int)
        abstract fun pmResult(event: BaseEvent<PMDetailBean>)
    }
}