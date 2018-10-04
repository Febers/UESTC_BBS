package com.febers.uestc_bbs.module.message.model

import android.util.Log.i
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.MsgAtBean
import com.febers.uestc_bbs.entity.MsgPrivateBean
import com.febers.uestc_bbs.entity.MsgReplyBean
import com.febers.uestc_bbs.entity.MsgSystemBean
import com.febers.uestc_bbs.module.message.presenter.MessageContract
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MsgModelImpl(private val messagePresenter: MessageContract.Presenter) : BaseModel(), MessageContract.Model {

    override fun msgService(type: String, page: Int) {
        mType = type
        mPage = page.toString()
        Thread(Runnable { getMessage() }).start()
    }

    private fun getMessage() {
        when(mType) {
            MSG_TYPE_PRIVATE -> getPrivate()
            else -> getReplyAndSystemAndAt()
        }
    }

    private fun getPrivate() {
        getRetrofit()
                .create(MessageInterface::class.java)
                .getPrivate(
                        json = """{"page":$mPage,pageSize:$COMMON_PAGE_SIZE}""")
                .enqueue(object : Callback<MsgPrivateBean> {
                    override fun onFailure(call: Call<MsgPrivateBean>?, t: Throwable?) {
                        i("msgM error", t.toString())
                        messagePresenter.msgResult(BaseEvent(BaseCode.FAILURE, MsgPrivateBean().apply {
                            errcode = t.toString()
                        }))
                    }

                    override fun onResponse(call: Call<MsgPrivateBean>?, response: Response<MsgPrivateBean>?) {
                        val msgPrivateBean = response?.body()
                        if (msgPrivateBean == null) {
                            messagePresenter.msgResult(BaseEvent(BaseCode.FAILURE, MsgReplyBean().apply {
                                errcode = SERVICE_RESPONSE_NULL
                            }))
                            return
                        }
                        if (msgPrivateBean.rs != REQUEST_SUCCESS_RS) {
                            messagePresenter.msgResult(BaseEvent(BaseCode.FAILURE, MsgPrivateBean().apply {
                                errcode = SERVICE_RESPONSE_ERROR
                            }))
                            return
                        }
                        messagePresenter.msgResult(BaseEvent(
                                if (msgPrivateBean.body.hasNext != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                                else BaseCode.SUCCESS,
                                msgPrivateBean))
                    }
                })
    }

    private fun getReplyAndSystemAndAt() {
        getRetrofit()
                .create(MessageInterface::class.java)
                .getReplyAndSystemAndAt(
                        type = mType,
                        page = mPage,
                        pageSize = COMMON_PAGE_SIZE)
                .enqueue(object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                        i("msgM error", t.toString())
                        when(mType) {
                            MSG_TYPE_REPLY -> {
                                messagePresenter.msgResult(BaseEvent(BaseCode.FAILURE, MsgReplyBean().apply {
                                    errcode = t.toString()
                                }))
                            }
                            MSG_TYPE_AT -> {
                                messagePresenter.msgResult(BaseEvent(BaseCode.FAILURE, MsgAtBean().apply {
                                    errcode = t.toString()
                                }))
                            }
                            MSG_TYPE_SYSTEM -> {
                                messagePresenter.msgResult(BaseEvent(BaseCode.FAILURE, MsgSystemBean().apply {
                                    errcode = t.toString()
                                }))
                            }
                        }
                    }

                    override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                        if (response?.body() == null) {
                            messagePresenter.msgResult(BaseEvent(BaseCode.FAILURE, MsgReplyBean().apply {
                                errcode = SERVICE_RESPONSE_NULL
                            }))
                            return
                        }
                        val json = response.body()
                        when(mType) {
                            MSG_TYPE_REPLY -> {
                                val msgReplyBean = Gson().fromJson(json, MsgReplyBean::class.java)
                                if (msgReplyBean.rs != REQUEST_SUCCESS_RS) {
                                    messagePresenter.msgResult(BaseEvent(BaseCode.FAILURE, MsgReplyBean().apply {
                                        errcode = SERVICE_RESPONSE_ERROR
                                    }))
                                    return
                                }
                                messagePresenter.msgResult(BaseEvent(
                                        if (msgReplyBean.has_next != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                                        else BaseCode.SUCCESS,
                                        msgReplyBean))
                            }
                            MSG_TYPE_AT -> {
                                val msgAtBean = Gson().fromJson(json, MsgAtBean::class.java)
                                if (msgAtBean.rs != REQUEST_SUCCESS_RS) {
                                    messagePresenter.msgResult(BaseEvent(BaseCode.FAILURE, MsgAtBean().apply {
                                        errcode = SERVICE_RESPONSE_ERROR
                                    }))
                                    return
                            }
                                messagePresenter.msgResult(BaseEvent(
                                        if (msgAtBean.has_next != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                                        else BaseCode.SUCCESS,
                                        msgAtBean))
                            }
                            MSG_TYPE_SYSTEM -> {
                                val msgSystemBean = Gson().fromJson(json, MsgSystemBean::class.java)
                                if (msgSystemBean.rs != REQUEST_SUCCESS_RS) {
                                    messagePresenter.msgResult(BaseEvent(BaseCode.FAILURE, MsgSystemBean().apply {
                                        errcode = SERVICE_RESPONSE_ERROR
                                    }))
                                    return
                                }
                                messagePresenter.msgResult(BaseEvent(
                                        if (msgSystemBean.has_next != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                                        else BaseCode.SUCCESS,
                                        msgSystemBean))
                            }
                        }
                    }
                })
    }
}