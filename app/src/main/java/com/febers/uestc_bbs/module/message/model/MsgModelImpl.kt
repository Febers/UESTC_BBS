package com.febers.uestc_bbs.module.message.model

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.MsgAtBean
import com.febers.uestc_bbs.entity.MsgPrivateBean
import com.febers.uestc_bbs.entity.MsgReplyBean
import com.febers.uestc_bbs.entity.MsgSystemBean
import com.febers.uestc_bbs.module.message.contract.MessageContract
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
                        messagePresenter.errorResult(t.toString())
                    }

                    override fun onResponse(call: Call<MsgPrivateBean>?, response: Response<MsgPrivateBean>?) {
                        val msgPrivateBean = response?.body()
                        if (msgPrivateBean == null) {
                            messagePresenter.errorResult(SERVICE_RESPONSE_NULL)
                            return
                        }
                        if (msgPrivateBean.rs != REQUEST_SUCCESS_RS) {
                            messagePresenter.errorResult(msgPrivateBean.head?.errInfo.toString())
                            return
                        }
                        messagePresenter.msgResult(BaseEvent(
                                if (msgPrivateBean.body?.hasNext != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
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
                        pageSize = COMMON_PAGE_SIZE.toString())
                .enqueue(object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                        messagePresenter.errorResult(t.toString())
                    }

                    override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                        if (response?.body() == null) {
                            messagePresenter.errorResult(SERVICE_RESPONSE_NULL)
                            return
                        }
                        val json = response.body()
                        if (json?.get("rs")?.asInt != REQUEST_SUCCESS_RS) {
                            messagePresenter.errorResult("获取出错:" + json?.get("head")?.asJsonObject?.get("errInfo")?.asString)
                            return
                        }
                        when(mType) {
                            MSG_TYPE_REPLY -> {
                                val msgReplyBean = Gson().fromJson(json, MsgReplyBean::class.java)
                                messagePresenter.msgResult(BaseEvent(
                                        if (msgReplyBean.has_next != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                                        else BaseCode.SUCCESS,
                                        msgReplyBean))
                            }
                            MSG_TYPE_AT -> {
                                val msgAtBean = Gson().fromJson(json, MsgAtBean::class.java)
                                messagePresenter.msgResult(BaseEvent(
                                        if (msgAtBean.has_next != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                                        else BaseCode.SUCCESS,
                                        msgAtBean))
                            }
                            MSG_TYPE_SYSTEM -> {
                                val msgSystemBean = Gson().fromJson(json, MsgSystemBean::class.java)
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