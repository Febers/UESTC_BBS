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
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostModelImpl(val postPresenter: PostContract.Presenter): IPostModel {

    private lateinit var postId: String
    private lateinit var page: String
    private lateinit var authorId: String
    private lateinit var order: String

    override fun postService(_postId: String, _page: Int, _authorId: String, _order: String) {
        postId = _postId
        page = _page.toString()
        authorId = _authorId
        order = _order

        Thread(object : Runnable {
            override fun run() {
                getPost()
            }
        }).start()
    }

    private fun getPost() {
        val user = BaseApplication.getUser()
        val retrofit = Retrofit.Builder()
                .baseUrl(ApiUtils.BBS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val postRequest = retrofit.create(PostInterface::class.java)
        val call = postRequest.getPostDetail(accessToken = user.token!!,
                accessSecret = user.secrete!!,
                topicId = postId,
                authorId = authorId,
                order = order,
                page = page,
                pageSize = COMMON_PAGE_SIZE)

        call?.enqueue(object : Callback<PostResultBean> {
            override fun onFailure(call: Call<PostResultBean>?, t: Throwable?) {
                i("PML", "${t.toString()}")
                postPresenter.postResult(BaseEvent(BaseCode.FAILURE, PostResultBean(rs = SERVICE_RESPONSE_ERROR + t.toString())))
            }

            override fun onResponse(call: Call<PostResultBean>?, response: Response<PostResultBean>?) {
                val postResultBean = response?.body()
                if (postResultBean == null) {
                    postPresenter.postResult(BaseEvent(BaseCode.FAILURE, PostResultBean(rs = SERVICE_RESPONSE_NULL)))
                    return
                }
                if (postResultBean.rs != REQUEST_SECCESS_RS) {
                    postPresenter.postResult(BaseEvent(BaseCode.FAILURE, PostResultBean(rs = postResultBean.head?.errInfo)))
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