/*
 * Created by Febers at 18-8-17 下午8:46.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-17 下午8:46.
 */

package com.febers.uestc_bbs.module.post.model

import android.util.Log.i
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PostDetailBean
import com.febers.uestc_bbs.entity.ReplySendResultBean
import com.febers.uestc_bbs.module.post.presenter.PostContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostModelImpl(val postPresenter: PostContract.Presenter): BaseModel(), PostContract.Model {

    override fun postDetailService(postId: Int, page: Int, authorId: Int, order: Int) {
        mPostId = postId.toString()
        mPage = page.toString()
        mAuthorId = authorId.toString()
        mOrder = order.toString()
        Thread(Runnable { getPost() }).start()
    }

    override fun postReplyService(tid: Int, isQuote: Int, replyId: Int, vararg contents: Pair<Int, String>) {
        Thread(Runnable {
            reply(isQuote, replyId, *contents)
        }).start()
    }

    private fun reply(isQuote: Int, replyId: Int, vararg contents: Pair<Int, String>) {
        val stContents = StringBuilder()
        contents.forEach {
            stContents.append("""{"type":${it.first},"infor":"${it.second}"},""")
        }
        stContents.deleteCharAt(stContents.lastIndex)
        i("POST", stContents.toString())
        getRetrofit().create(PostInterface::class.java)
                .postReply(json = """
                    {"body":
                        {"json":
                            {
                                "tid":$mPostId,
                                "isAnonymous":0,
                                "isOnlyAuthor":0,
                                "isQuote":$isQuote,
                                "replyId":$replyId,
                                "content":"[$stContents]"
                            }
                        }
                    }
                        """.trimIndent())
                .enqueue(object : Callback<ReplySendResultBean> {
                    override fun onFailure(call: Call<ReplySendResultBean>, t: Throwable?) {
                        postPresenter.errorResult(t.toString())
                    }

                    override fun onResponse(call: Call<ReplySendResultBean>, response: Response<ReplySendResultBean>?) {
                        val replySendResultBean = response?.body()
                        if (replySendResultBean == null) {
                            postPresenter.errorResult(SERVICE_RESPONSE_NULL)
                            return
                        }
                        if (replySendResultBean.rs != REQUEST_SUCCESS_RS) {
                            postPresenter.errorResult(replySendResultBean.head?.errInfo.toString())
                            return
                        }
                        postPresenter.postReplyResult(BaseEvent(BaseCode.SUCCESS, replySendResultBean))
                    }
                })
    }

    private fun getPost() {
        val postRequest = getRetrofit().create(PostInterface::class.java)
        val call = postRequest.getPostDetail(
                topicId = mPostId,
                authorId = mAuthorId,
                order = mOrder,
                page = mPage,
                pageSize = COMMON_PAGE_SIZE.toString())
        call.enqueue(object : Callback<PostDetailBean> {
            override fun onFailure(call: Call<PostDetailBean>?, t: Throwable?) {
                postPresenter.errorResult(SERVICE_RESPONSE_ERROR)
            }

            override fun onResponse(call: Call<PostDetailBean>?, response: Response<PostDetailBean>?) {
                val postResultBean = response?.body()
                if (postResultBean == null) {
                    postPresenter.errorResult(SERVICE_RESPONSE_NULL)
                    return
                }
                if (postResultBean.rs != REQUEST_SUCCESS_RS) {
                    postPresenter.errorResult(postResultBean.head?.errInfo.toString())
                    return
                }
                if (postResultBean.has_next != HAVE_NEXT_PAGE) {
                    postPresenter.postDetailResult(BaseEvent(BaseCode.SUCCESS_END, postResultBean))
                    return
                }
                postPresenter.postDetailResult(BaseEvent(BaseCode.SUCCESS, postResultBean))
            }
        })
    }
}