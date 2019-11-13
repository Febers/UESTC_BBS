package com.febers.uestc_bbs.module.post.presenter

import com.febers.uestc_bbs.base.REQUEST_SUCCESS_RS
import com.febers.uestc_bbs.base.SERVICE_RESPONSE_NULL
import com.febers.uestc_bbs.base.ThreadPoolMgr
import com.febers.uestc_bbs.entity.PostSendResultBean
import com.febers.uestc_bbs.http.PostEditInterface
import com.febers.uestc_bbs.module.post.contract.PEditContract
import com.febers.uestc_bbs.utils.log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

class PEditPresenterImpl(view: PEditContract.View): PEditContract.Presenter(view) {

    override fun newPostRequest(fid: Int, aid: String, typeId:Int, title: String, anonymous: Int, onlyAuthor: Int, vararg contents: Pair<Int, String>) {
        ThreadPoolMgr.execute(Runnable { newPost(fid, aid, typeId, title, anonymous, onlyAuthor, *contents) })
    }

    private fun newPost(fid: Int, aid: String, typeId:Int, title: String, anonymous: Int, onlyAuthor: Int, vararg contents: Pair<Int, String>) {
        log("new post and fid is $fid")
        val stContents = StringBuilder()
        contents.forEach {
            val newContent = it.second.replace("\n", """\\n""") //非常重要，解决服务器不识别换行问题
            stContents.append("""{"type":${it.first},"infor":"$newContent"},""")
        }
        stContents.deleteCharAt(stContents.lastIndex)
        getRetrofit().create(PostEditInterface::class.java)
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
                                "typeId":$typeId,
                                "title":"$title",
                                "content":"[$stContents]"
                            }
                        }
                    }
                """)
                .enqueue(object : Callback<PostSendResultBean> {
                    override fun onFailure(call: Call<PostSendResultBean>, t: Throwable) {
                        errorResult(t.toString())
                    }

                    override fun onResponse(call: Call<PostSendResultBean>, response: Response<PostSendResultBean>) {
                        val resultBean = response.body()
                        if (resultBean == null) {
                            errorResult(SERVICE_RESPONSE_NULL)
                            return
                        }
                        if (resultBean.rs != REQUEST_SUCCESS_RS) {
                            errorResult(resultBean.head?.errInfo.toString())
                            return
                        }
                        mView?.showNewPostResult(resultBean)
                    }
                })
    }
}