package com.febers.uestc_bbs.module.message.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.entity.MsgBaseBean

interface MsgContract {

    interface Model {
        fun msgService(type: String, page: Int)
    }

    interface View: BaseView {
        fun <M: MsgBaseBean> showMessage(event: BaseEvent<M>)
    }

    abstract class Presenter(view: MsgContract.View): BasePresenter<MsgContract.View>(view) {
        abstract fun msgRequest(type: String, page: Int)
        abstract fun <M :MsgBaseBean> msgResult(event: BaseEvent<M>)
    }
}