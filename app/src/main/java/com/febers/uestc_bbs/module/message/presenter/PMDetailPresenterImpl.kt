package com.febers.uestc_bbs.module.message.presenter

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PMDetailBean
import com.febers.uestc_bbs.entity.PMSendResultBean
import com.febers.uestc_bbs.http.MessageInterface
import com.febers.uestc_bbs.module.message.contract.MessageContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PMDetailPresenterImpl(val view: MessageContract.PMView): MessageContract.PMPresenter(view) {

    private var uid  = 0

    override fun pmSessionRequest(uid: Int, page: Int, beginTime: Long) {
        this.uid = uid
        ThreadMgr.network { getPmDetail(uid, page, beginTime) }
    }

    override fun pmSendRequest(content: Any, type: String) {
        ThreadMgr.network { pmSendService(content, type) }
    }

    private fun getPmDetail(uid: Int, page: Int, beginTime: Long) {
        getRetrofit().create(MessageInterface::class.java)
                .getPMDetail(pmlist = """
                    {"body":
                        {"pmInfos":[{
                            "cacheCount":0,
                            "fromUid":$uid,
                            "plid":0,
                            "pmLimit":25,
                            "pmid":0,
                            "startTime":$beginTime,
                            "stopTime":0
                            }]
                        }
                    }
                """.trimIndent())
                .enqueue(object : Callback<PMDetailBean> {
                    override fun onFailure(call: Call<PMDetailBean>, t: Throwable) {
                        errorResult(t.toString())
                    }

                    override fun onResponse(call: Call<PMDetailBean>, response: Response<PMDetailBean>) {
                        val bean = response.body()
                        if (bean == null) {
                            errorResult(SERVICE_RESPONSE_NULL)
                            return
                        }
                        if (bean.rs != REQUEST_SUCCESS_RS) {
                            errorResult(bean.head?.errInfo.toString())
                            return
                        }
                        mView?.showPMSession(event = BaseEvent(
                                if (bean.body?.pmList!![0].hasPrev != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                                else BaseCode.SUCCESS, bean))
                    }
                })
    }

    private fun pmSendService(content: Any, type: String) {
        val stContent = content.toString()
        ThreadMgr.network {
            getRetrofit().create(MessageInterface::class.java)
                    .pmSendResult(json = """
                        {"action":"send","toUid":$uid,msg:{"type":"$type","content":"$stContent"}}
                    """)
                    .enqueue(object : Callback<PMSendResultBean> {
                        override fun onFailure(call: Call<PMSendResultBean>, t: Throwable) {
                            errorResult(t.toString())
                        }

                        override fun onResponse(call: Call<PMSendResultBean>, response: Response<PMSendResultBean>) {
                            val result = response.body()
                            if (result == null) {
                                errorResult(SERVICE_RESPONSE_NULL)
                                return
                            }
                            if (result.rs != REQUEST_SUCCESS_RS) {
                                errorResult(result.head?.errInfo.toString())
                                return
                            }
                            mView?.showPMSendResult(BaseEvent(BaseCode.SUCCESS, result))
                        }
                    })
        }
    }
}