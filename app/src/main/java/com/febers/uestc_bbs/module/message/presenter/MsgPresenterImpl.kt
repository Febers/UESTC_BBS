package com.febers.uestc_bbs.module.message.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.MsgBaseBean

class MsgPresenterImpl(val view: MsgContract.View) : MsgContract.Presenter(view) {

    override fun msgRequest(type: String, page: Int) {

    }

    override fun msgResult(event: BaseEvent<MsgBaseBean>) {

    }
}