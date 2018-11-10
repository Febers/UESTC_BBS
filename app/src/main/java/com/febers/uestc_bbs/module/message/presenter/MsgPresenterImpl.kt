package com.febers.uestc_bbs.module.message.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.MsgBaseBean
import com.febers.uestc_bbs.module.message.contract.MessageContract
import com.febers.uestc_bbs.module.message.model.MsgModelImpl

class MsgPresenterImpl(val view: MessageContract.View) : MessageContract.Presenter(view) {

    override fun msgRequest(type: String, page: Int) {
        MsgModelImpl(this).msgService(type, page)
    }

    override fun <M : MsgBaseBean> msgResult(event: BaseEvent<M>) {
        view.showMessage(event)
    }
}