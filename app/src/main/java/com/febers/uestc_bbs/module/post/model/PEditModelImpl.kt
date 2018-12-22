package com.febers.uestc_bbs.module.post.model

import com.febers.uestc_bbs.base.BaseModel
import com.febers.uestc_bbs.base.REQUEST_SUCCESS_RS
import com.febers.uestc_bbs.base.SERVICE_RESPONSE_NULL
import com.febers.uestc_bbs.entity.PostSendResultBean
import com.febers.uestc_bbs.module.post.contract.PEditContract
import com.febers.uestc_bbs.module.post.model.http_interface.PEditInterface
import com.febers.uestc_bbs.utils.log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

class PEditModelImpl(val pEditPresenter: PEditContract.Presenter): BaseModel(), PEditContract.Model {

    override fun newPostService(fid: Int, aid: String, typeId:Int, title: String, anonymous: Int, onlyAuthor: Int, vararg contents: Pair<Int, String>) {
        Thread{ newPost(fid, aid, typeId, title, anonymous, onlyAuthor, *contents) }.start()
    }

    private fun newPost(fid: Int, aid: String, typeId:Int, title: String, anonymous: Int, onlyAuthor: Int, vararg contents: Pair<Int, String>) {
        log("new post and fid is $fid")
        val stContents = StringBuilder()
        contents.forEach {
            stContents.append("""{"type":${it.first},"infor":"${it.second}"},""")
        }
        stContents.deleteCharAt(stContents.lastIndex)
        getRetrofit().create(PEditInterface::class.java)
                .newPost(json = """
                    {"body":
                        {"json":
                            {
                                "fid":$fid,
                                "tid":,
                                "aid":"$aid",
                                "isAnonymous":$anonymous,
                                "isOnlyAuthor":$onlyAuthor,
                                "isQuote":0,
                                "replyId":,
                                "typeId":,
                                "title":"$title",
                                "content":"[$stContents]"
                            }
                        }
                    }
                """)
                .enqueue(object : Callback<PostSendResultBean> {
                    override fun onFailure(call: Call<PostSendResultBean>, t: Throwable) {
                        pEditPresenter.errorResult(t.toString())
                    }

                    override fun onResponse(call: Call<PostSendResultBean>, response: Response<PostSendResultBean>) {
                        val resultBean = response.body()
                        if (resultBean == null) {
                            pEditPresenter.errorResult(SERVICE_RESPONSE_NULL)
                            return
                        }
                        if (resultBean.rs != REQUEST_SUCCESS_RS) {
                            pEditPresenter.errorResult(resultBean.head?.errInfo.toString())
                            return
                        }
                        pEditPresenter.newPostResult(resultBean)
                    }
                })
    }

}