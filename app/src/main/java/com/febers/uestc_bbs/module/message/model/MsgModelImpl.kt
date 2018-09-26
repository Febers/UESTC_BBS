package com.febers.uestc_bbs.module.message.model

import android.util.Log.i
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.MsgAtMBean
import com.febers.uestc_bbs.entity.MsgReplyBean
import com.febers.uestc_bbs.entity.MsgSystemBean
import com.febers.uestc_bbs.module.message.presenter.MsgContract
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MsgModelImpl(private val msgPresenter: MsgContract.Presenter) : BaseModel(), MsgContract.Model {

    override fun msgService(type: String, page: Int) {
        mType = type
        mPage = page.toString()
        Thread(Runnable { getMessage() }).start()
    }

    private fun getMessage() {
        when(mType) {
            MSG_TYPE_PRIVATE -> getPrivateMsg()
            else -> getReplyAndSystemAndAt()
        }
    }

    private fun getPrivateMsg() {

    }

    private fun getReplyAndSystemAndAt() {
        getRetrofit()
                .create(MsgInterface::class.java)
                .getReply(
                        accessToken = getUser().token,
                        accessSecret = getUser().secrete,
                        type = mType,
                        page = mPage,
                        pageSize = COMMON_PAGE_SIZE)
                .enqueue(object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                        i("msgM error", t.toString())
                        when(mType) {
                            MSG_TYPE_REPLY -> {
                                msgPresenter.msgResult(BaseEvent(BaseCode.FAILURE, MsgReplyBean().apply {
                                    errcode = t.toString()
                                }))
                            }
                            MSG_TYPE_AT -> {
                                msgPresenter.msgResult(BaseEvent(BaseCode.FAILURE, MsgAtMBean().apply {
                                    errcode = t.toString()
                                }))
                            }
                            MSG_TYPE_SYSTEM -> {
                                msgPresenter.msgResult(BaseEvent(BaseCode.FAILURE, MsgSystemBean().apply {
                                    errcode = t.toString()
                                }))
                            }
                            else -> {}
                        }
                    }

                    override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                        if (response?.body() == null) {
                            msgPresenter.msgResult(BaseEvent(BaseCode.FAILURE, MsgReplyBean().apply {
                                errcode = SERVICE_RESPONSE_NULL
                            }))
                            return
                        }
                        val json = response.body()
                        when(mType) {
                            MSG_TYPE_REPLY -> {
                                val msgReplyBean = Gson().fromJson(json, MsgReplyBean::class.java)
                                if (msgReplyBean.rs != REQUEST_SUCCESS_RS) {
                                    msgPresenter.msgResult(BaseEvent(BaseCode.FAILURE, MsgReplyBean().apply {
                                        errcode = SERVICE_RESPONSE_ERROR
                                    }))
                                    return
                                }
                                msgPresenter.msgResult(BaseEvent(
                                        if (msgReplyBean.has_next != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                                        else BaseCode.SUCCESS,
                                        msgReplyBean
                                ))
                            }
                            MSG_TYPE_AT -> {
                                val msgAtBean = Gson().fromJson(json, MsgAtMBean::class.java)
                                if (msgAtBean.rs != REQUEST_SUCCESS_RS) {
                                    msgPresenter.msgResult(BaseEvent(BaseCode.FAILURE, MsgAtMBean().apply {
                                        errcode = SERVICE_RESPONSE_ERROR
                                    }))
                                    return
                                }
                                msgPresenter.msgResult(BaseEvent(
                                        if (msgAtBean.has_next != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                                        else BaseCode.SUCCESS,
                                        msgAtBean
                                ))
                            }
                            MSG_TYPE_SYSTEM -> {
                                val msgSystemBean = Gson().fromJson(json, MsgSystemBean::class.java)
                                if (msgSystemBean.rs != REQUEST_SUCCESS_RS) {
                                    msgPresenter.msgResult(BaseEvent(BaseCode.FAILURE, MsgSystemBean().apply {
                                        errcode = SERVICE_RESPONSE_ERROR
                                    }))
                                    return
                                }
                                msgPresenter.msgResult(BaseEvent(
                                        if (msgSystemBean.has_next != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                                        else BaseCode.SUCCESS,
                                        msgSystemBean
                                ))
                            }
                            else -> {}
                        }
                    }
                })
    }
}