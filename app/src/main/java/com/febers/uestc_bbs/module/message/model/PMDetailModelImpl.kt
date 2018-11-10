package com.febers.uestc_bbs.module.message.model

import android.util.Log.i
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PMDetailBean
import com.febers.uestc_bbs.entity.PMSendResultBean
import com.febers.uestc_bbs.module.message.presenter.MessageContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PMDetailModelImpl(val presenter: MessageContract.PMPresenter):MessageContract.PMModel, BaseModel() {


    override fun pmSessionService(uid: Int, page: Int, beginTime: Long) {
        mUid = uid.toString()
        Thread(Runnable { getPmDetail() }).start()
    }

    override fun pmSendService(content: Any, type: String) {
        val stContent = content.toString()
        Thread(Runnable {
            getRetrofit().create(MessageInterface::class.java)
                    .pmSendResult(json = """
                        {"action":"send","toUid":$mUid,msg:{"type":"$type","content":"$stContent"}}
                    """)
                    .enqueue(object : Callback<PMSendResultBean> {
                        override fun onFailure(call: Call<PMSendResultBean>, t: Throwable) {
                            presenter.errorResult(t.toString())
                        }

                        override fun onResponse(call: Call<PMSendResultBean>, response: Response<PMSendResultBean>) {
                            val result = response.body()
                            if (result == null) {
                                presenter.errorResult(SERVICE_RESPONSE_NULL)
                                return
                            }
                            if (result.rs != REQUEST_SUCCESS_RS) {
                                presenter.errorResult(result.head?.errInfo.toString())
                                return
                            }
                            presenter.pmSendResult(BaseEvent(BaseCode.SUCCESS, result))
                        }
                    })
        }).start()
    }

    private fun getPmDetail() {
        getRetrofit().create(MessageInterface::class.java)
                .getPMDetail(pmlist = """
                    {"body":
                        {"pmInfos":[{
                            "cacheCount":0,
                            "fromUid":$mUid,
                            "plid":0,
                            "pmLimit":25,
                            "pmid":0,
                            "startTime":0,
                            "stopTime":0
                            }]
                        }
                    }
                """.trimIndent())
                .enqueue(object : Callback<PMDetailBean> {
                    override fun onFailure(call: Call<PMDetailBean>, t: Throwable) {
                        presenter.errorResult(t.toString())
                    }

                    override fun onResponse(call: Call<PMDetailBean>, response: Response<PMDetailBean>) {
                        val bean = response.body()
                        if (bean == null) {
                            presenter.errorResult(SERVICE_RESPONSE_NULL)
                            return
                        }
                        if (bean.rs != REQUEST_SUCCESS_RS) {
                            presenter.errorResult(bean.head?.errInfo.toString())
                            return
                        }
                        presenter.pmSessionResult(event = BaseEvent(
                                if (bean.body?.pmList!![0].hasPrev != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                                else BaseCode.SUCCESS, bean))
                    }
                })
    }
}