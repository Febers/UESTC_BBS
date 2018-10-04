package com.febers.uestc_bbs.module.message.model

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PMDetailBean
import com.febers.uestc_bbs.module.message.presenter.MessageContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PMDetailModelImpl(val presenter: MessageContract.PMPresenter):MessageContract.PMModel, BaseModel() {

    override fun pmService(uid: String, page: Int) {
        mUid = uid
        Thread(Runnable { getPmDetail() }).start()
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
                        presenter.pmResult(BaseEvent(BaseCode.FAILURE, PMDetailBean()
                                .apply { errcode = SERVICE_RESPONSE_ERROR }))
                    }

                    override fun onResponse(call: Call<PMDetailBean>, response: Response<PMDetailBean>) {
                        val bean = response.body()
                        if (bean == null) {
                            presenter.pmResult(BaseEvent(BaseCode.FAILURE, PMDetailBean()
                                    .apply { errcode = SERVICE_RESPONSE_NULL }))
                            return
                        }
                        if (bean.rs != REQUEST_SUCCESS_RS) {
                            presenter.pmResult(BaseEvent(BaseCode.FAILURE, PMDetailBean()
                                    .apply { errcode = bean.head.errInfo }))
                            return
                        }
                        presenter.pmResult(event = BaseEvent(
                                if (bean.body.pmList[0].hasPrev != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                                else BaseCode.SUCCESS, bean))
                    }
                })
    }
}