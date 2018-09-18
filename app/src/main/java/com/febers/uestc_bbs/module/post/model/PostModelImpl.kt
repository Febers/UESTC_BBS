/*
 * Created by Febers at 18-8-17 下午8:46.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-17 下午8:46.
 */

package com.febers.uestc_bbs.module.post.model

import android.util.Log.i
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PostResultBean
import com.febers.uestc_bbs.module.post.presenter.PostContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostModelImpl(val postPresenter: PostContract.Presenter): BaseModel(), PostContract.Model {

    override fun postService(_postId: String, _page: Int, _authorId: String, _order: String) {
        mPostId = _postId
        mPage = _page.toString()
        mAuthorId = _authorId
        mOrder = _order
        Thread(Runnable { getPost() }).start()
    }

    private fun getPost() {
        val postRequest = getRetrofit().create(PostInterface::class.java)
        val call = postRequest.getPostDetail(accessToken = getUser().token,
                accessSecret = getUser().secrete,
                topicId = mPostId,
                authorId = mAuthorId,
                order = mOrder,
                page = mPage,
                pageSize = COMMON_PAGE_SIZE)
        call.enqueue(object : Callback<PostResultBean> {
            override fun onFailure(call: Call<PostResultBean>?, t: Throwable?) {
                i("PML", t.toString())
                postPresenter.postResult(BaseEvent(BaseCode.FAILURE, PostResultBean(errcode = SERVICE_RESPONSE_ERROR + t.toString())))
            }

            override fun onResponse(call: Call<PostResultBean>?, response: Response<PostResultBean>?) {
                val postResultBean = response?.body()
                if (postResultBean == null) {
                    postPresenter.postResult(BaseEvent(BaseCode.FAILURE, PostResultBean(errcode = SERVICE_RESPONSE_NULL)))
                    return
                }
                if (postResultBean.rs != REQUEST_SUCCESS_RS) {
                    postPresenter.postResult(BaseEvent(BaseCode.FAILURE, PostResultBean(errcode = postResultBean.head?.errInfo)))
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