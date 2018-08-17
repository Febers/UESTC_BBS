/*
 * Created by Febers at 18-8-15 下午11:38.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-15 下午11:38.
 */

package com.febers.uestc_bbs.module.post.model

import com.febers.uestc_bbs.base.BaseApplication
import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.PostResultBean
import com.febers.uestc_bbs.entity.SimplePostBean
import com.febers.uestc_bbs.module.post.presenter.PostContract
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val COMMON_PAGE_SIZE = "20"

class PostModelImpl(val postPresenter: PostContract.Presenter) : IPostModel {

    private val mContext = BaseApplication.context()
    private lateinit var fid: String
    private lateinit var page: String

    override fun postService(_fid: String, _page: Int, _refresh: Boolean) {
        fid = _fid
        page = _page.toString()

        Thread(object : Runnable {
            override fun run() {
                getPost()
            }
        }).start()
    }

    private fun getPost() {
        val retrofit = Retrofit.Builder()
                .baseUrl(ApiUtils.BBS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val postRequet = retrofit.create(PostInterface::class.java)
        val call = postRequet.newPost(r = "forum/topiclist",
                boardId = "0",
                page = "1",
                pageSize = COMMON_PAGE_SIZE,
                sortby = "new")
        call.enqueue(object : Callback<PostResultBean> {
            override fun onFailure(call: Call<PostResultBean>?, t: Throwable?) {
                postPresenter.postResult(BaseEvent(BaseCode.ERROR, arrayListOf(SimplePostBean(title = "服务器未响应"))))
            }

            override fun onResponse(call: Call<PostResultBean>?, response: Response<PostResultBean>?) {
                val body = response?.body()
                if (body == null) {
                    postPresenter.postResult(BaseEvent(BaseCode.FAILURE, arrayListOf(SimplePostBean(title = "服务器相应为空"))))
                    return
                }
                postPresenter.postResult(BaseEvent(BaseCode.SUCCESS, body.list))
            }
        })
    }
}