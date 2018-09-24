package com.febers.uestc_bbs.module.message.model

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.module.message.presenter.MsgContract

class MsgModelImpl(val msgPresenter: MsgContract.Presenter) : BaseModel(), MsgContract.Model {

    override fun msgService(type: String, page: Int) {
        mType = type
    }

    private fun getMessage() {
        when(mType) {
            MSG_TYPE_REPLY -> getReplyMsg()
            MSG_TYPE_PRIVATE -> getPrivateMsg()
            MSG_TYPE_AT -> getAtMsg()
            MSG_TYPE_SYSTEM -> getSystemMsg()
            else -> {}
        }
    }

    private fun getAtMsg() {

    }

    private fun getPrivateMsg() {

    }

    private fun getReplyMsg() {

    }

    private fun getSystemMsg() {

    }
}