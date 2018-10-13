package com.febers.uestc_bbs.module.message.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.PMDetailBean
import com.febers.uestc_bbs.module.message.model.PMDetailModelImpl

class PMDetailPresenterImpl(val view: MessageContract.PMView): MessageContract.PMPresenter(view) {

    override fun pmRequest(uid: Int, page: Int) {
        val pmModel: MessageContract.PMModel = PMDetailModelImpl(this)
        pmModel.pmService(uid, page)
    }

    override fun pmResult(event: BaseEvent<PMDetailBean>) {
        view.showPMDetail(event)
    }
}