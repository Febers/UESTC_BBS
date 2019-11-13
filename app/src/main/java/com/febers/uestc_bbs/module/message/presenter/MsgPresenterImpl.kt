package com.febers.uestc_bbs.module.message.presenter

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.*
import com.febers.uestc_bbs.http.MessageInterface
import com.febers.uestc_bbs.module.message.contract.MessageContract
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MsgPresenterImpl(view: MessageContract.View) : MessageContract.Presenter(view) {

    override fun msgRequest(type: String, page: Int) {
        ThreadPoolMgr.execute(Runnable { getMessage(type, page) })
    }

    private fun getMessage(type: String, page: Int) {
        when(type) {
            MSG_TYPE_PRIVATE -> getPrivate(page)
            else -> getReplyAndSystemAndAt(type, page)
        }
    }

    private fun getPrivate(page: Int) {
        getRetrofit()
                .create(MessageInterface::class.java)
                .getPrivate(
                        json = """{"page":$page,pageSize:$COMMON_PAGE_SIZE}""")
                .enqueue(object : Callback<MsgPrivateBean> {
                    override fun onFailure(call: Call<MsgPrivateBean>?, t: Throwable?) {
                        errorResult(t.toString())
                    }

                    override fun onResponse(call: Call<MsgPrivateBean>?, response: Response<MsgPrivateBean>?) {
                        val msgPrivateBean = response?.body()
                        if (msgPrivateBean == null) {
                            errorResult(SERVICE_RESPONSE_NULL)
                            return
                        }
                        if (msgPrivateBean.rs != REQUEST_SUCCESS_RS) {
                            errorResult(msgPrivateBean.head?.errInfo.toString())
                            return
                        }
                        mView?.showMessage(BaseEvent(
                                if (msgPrivateBean.body?.hasNext != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                                else BaseCode.SUCCESS,
                                msgPrivateBean))
                    }
                })
    }

    private fun getReplyAndSystemAndAt(type: String, page: Int) {
        getRetrofit()
                .create(MessageInterface::class.java)
                .getReplyAndSystemAndAt(
                        type = type,
                        page = page.toString(),
                        pageSize = COMMON_PAGE_SIZE.toString())
                .enqueue(object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                        errorResult(t.toString())
                    }

                    override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                        if (response?.body() == null) {
                            errorResult(SERVICE_RESPONSE_NULL)
                            return
                        }
                        val json = response.body()
                        if (json?.get("rs")?.asInt != REQUEST_SUCCESS_RS) {
                            errorResult("获取出错:" + json?.get("head")?.asJsonObject?.get("errInfo")?.asString)
                            return
                        }
                        when(type) {
                            MSG_TYPE_REPLY -> {
                                val msgReplyBean = Gson().fromJson(json, MsgReplyBean::class.java)
                                mView?.showMessage(BaseEvent(
                                        if (msgReplyBean.has_next != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                                        else BaseCode.SUCCESS,
                                        msgReplyBean))
                            }
                            MSG_TYPE_AT -> {
                                val msgAtBean = Gson().fromJson(json, MsgAtBean::class.java)
                                mView?.showMessage(BaseEvent(
                                        if (msgAtBean.has_next != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                                        else BaseCode.SUCCESS,
                                        msgAtBean))
                            }
                            MSG_TYPE_SYSTEM -> {
                                val msgSystemBean = Gson().fromJson(json, MsgSystemBean::class.java)
                                mView?.showMessage(BaseEvent(
                                        if (msgSystemBean.has_next != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                                        else BaseCode.SUCCESS,
                                        msgSystemBean))
                            }
                        }
                    }
                })
    }

}