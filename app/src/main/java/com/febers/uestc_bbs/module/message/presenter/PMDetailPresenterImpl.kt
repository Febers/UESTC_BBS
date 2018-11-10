package com.febers.uestc_bbs.module.message.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.PMDetailBean
import com.febers.uestc_bbs.entity.PMSendResultBean
import com.febers.uestc_bbs.module.message.model.PMDetailModelImpl

class PMDetailPresenterImpl(val view: MessageContract.PMView): MessageContract.PMPresenter(view) {

    private val pmModel: MessageContract.PMModel = PMDetailModelImpl(this)

    override fun pmSessionRequest(uid: Int, page: Int, beginTime: Long) {
        pmModel.pmSessionService(uid, page, beginTime)
    }

    override fun pmSendRequest(content: Any, type: String) {
        pmModel.pmSendService(content, type)
    }

    override fun pmSessionResult(event: BaseEvent<PMDetailBean>) {
        view.showPMSession(event)
    }

    override fun pmSendResult(event: BaseEvent<PMSendResultBean>) {
        view.showPMSendResult(event)
    }
}