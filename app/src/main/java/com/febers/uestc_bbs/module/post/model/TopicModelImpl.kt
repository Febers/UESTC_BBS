/*
 * Created by Febers at 18-8-15 下午11:38.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-15 下午11:38.
 */

package com.febers.uestc_bbs.module.post.model

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.TopicResultBean
import com.febers.uestc_bbs.entity.SimpleTopicBean
import com.febers.uestc_bbs.module.post.presenter.TopicContract
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val COMMON_PAGE_SIZE = "20"

class TopicModelImpl(val topicPresenter: TopicContract.Presenter) : ITopicModel {

    private val mContext = BaseApplication.context()
    private lateinit var fid: String
    private lateinit var page: String

    override fun topicService(_fid: String, _page: Int, _refresh: Boolean) {
        fid = _fid
        page = _page.toString()

        Thread(object : Runnable {
            override fun run() {
                getTopic()
            }
        }).start()
    }

    private fun getTopic() {
        getCall().enqueue(object : Callback<TopicResultBean> {
            override fun onFailure(call: Call<TopicResultBean>?, t: Throwable?) {
                topicPresenter.topicResult(BaseEvent(BaseCode.FAILURE, arrayListOf(SimpleTopicBean(title = SERVICE_RESPONSE_ERROR))))
            }

            override fun onResponse(call: Call<TopicResultBean>?, response: Response<TopicResultBean>?) {
                val body = response?.body()
                if (body == null) {
                    topicPresenter.topicResult(BaseEvent(BaseCode.FAILURE, arrayListOf(SimpleTopicBean(title = SERVICE_RESPONSE_NULL))))
                    return
                }
                if (body.has_next == 0) {
                    topicPresenter.topicResult(BaseEvent(BaseCode.SUCCESS_END, body.list))
                } else {
                    topicPresenter.topicResult(BaseEvent(BaseCode.SUCCESS, body.list))
                }
            }
        })
    }

    private fun getCall(): Call<TopicResultBean>{
        val retrofit = Retrofit.Builder()
                .baseUrl(ApiUtils.BBS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val topicRequet = retrofit.create(TopicInterface::class.java)
        //最新发表
        if (fid == "0") {
            return topicRequet.newPosts(r = "forum/topiclist",
                    boardId = "0",
                    page = page,
                    pageSize = COMMON_PAGE_SIZE,
                    sortby = "new")
        }
        //最新回复
        if (fid == "1") {
            return topicRequet.newPosts(r = "forum/topiclist",
                    boardId = "0",
                    page = page,
                    pageSize = COMMON_PAGE_SIZE,
                    sortby = "all")
        }
        //热门帖子
        if (fid == "2") {
            return topicRequet.hotPosts(r = "portal/newslist",
                    moduleId = "2",
                    page = page,
                    pageSize = COMMON_PAGE_SIZE)
        }
        //版块里的帖子
        return topicRequet.normalPosts()
    }
}