/*
 * Created by Febers at 18-8-17 下午8:46.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-17 下午8:46.
 */

package com.febers.uestc_bbs.module.post.model

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PostDetailBean
import com.febers.uestc_bbs.module.post.presenter.PostContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostModelImpl(val postPresenter: PostContract.Presenter): BaseModel(), PostContract.Model {

    override fun postService(postId: Int, page: Int, authorId: Int, order: Int) {
        mPostId = postId.toString()
        mPage = page.toString()
        mAuthorId = authorId.toString()
        mOrder = order.toString()
        Thread(Runnable { getPost() }).start()
    }

    private fun getPost() {
        val postRequest = getRetrofit().create(PostInterface::class.java)
        val call = postRequest.getPostDetail(
                topicId = mPostId,
                authorId = mAuthorId,
                order = mOrder,
                page = mPage,
                pageSize = COMMON_PAGE_SIZE)
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
                    postPresenter.postResult(BaseEvent(BaseCode.SUCCESS_END, postResultBean))
                    return
                }
                postPresenter.postResult(BaseEvent(BaseCode.SUCCESS, postResultBean))
            }
        })
    }
}