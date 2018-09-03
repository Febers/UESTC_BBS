/*
 * Created by Febers at 18-8-15 下午11:38.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-15 下午11:38.
 */

package com.febers.uestc_bbs.module.post.model

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PListResultBean
import com.febers.uestc_bbs.entity.SimplePListBean
import com.febers.uestc_bbs.module.post.presenter.PListContract
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val COMMON_PAGE_SIZE = "20"

class TopicModelImpl(val PListPresenter: PListContract.Presenter) : IPListModel {

    private val mContext = BaseApplication.context()
    private lateinit var fid: String
    private lateinit var page: String

    override fun topicService(_fid: String, _page: Int, _refresh: Boolean) {
        fid = _fid
        page = _page.toString()

        Thread(Runnable { getTopic() }).start()
    }

    private fun getTopic() {
        getCall().enqueue(object : Callback<PListResultBean> {
            override fun onFailure(call: Call<PListResultBean>?, t: Throwable?) {
                PListPresenter.pListResult(BaseEvent(BaseCode.FAILURE, arrayListOf(SimplePListBean(title = SERVICE_RESPONSE_ERROR))))
            }

            override fun onResponse(call: Call<PListResultBean>?, response: Response<PListResultBean>?) {
                val body = response?.body()
                if (body == null) {
                    PListPresenter.pListResult(BaseEvent(BaseCode.FAILURE, arrayListOf(SimplePListBean(title = SERVICE_RESPONSE_NULL))))
                    return
                }
                if(body.rs != REQUEST_SECCESS_RS) {
                    PListPresenter.pListResult(BaseEvent(BaseCode.FAILURE, arrayListOf(SimplePListBean(title = body.head?.errInfo))))
                    return
                }
                if (body.has_next == 0) {
                    PListPresenter.pListResult(BaseEvent(BaseCode.SUCCESS_END, body.list))
                } else {
                    PListPresenter.pListResult(BaseEvent(BaseCode.SUCCESS, body.list))
                }
            }
        })
    }

    private fun getCall(): Call<PListResultBean>{
        val user = BaseApplication.getUser()
        val retrofit = Retrofit.Builder()
                .baseUrl(ApiUtils.BBS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val pListRequest = retrofit.create(PListInterface::class.java)
        //最新发表
        if (fid == HOME_POSTS_NEW) {
            return pListRequest.newPosts(r = "forum/topiclist",
                    boardId = "0",
                    page = page,
                    pageSize = COMMON_PAGE_SIZE,
                    sortby = "new")
        }
        //最新回复
        if (fid == HOME_POSTS_REPLY) {
            return pListRequest.newPosts(r = "forum/topiclist",
                    boardId = "0",
                    page = page,
                    pageSize = COMMON_PAGE_SIZE,
                    sortby = "all")
        }
        //热门帖子
        if (fid == HOME_POSTS_HOT) {
            return pListRequest.hotPosts(r = "portal/newslist",
                    moduleId = "2",
                    page = page,
                    pageSize = COMMON_PAGE_SIZE)
        }
        //版块里的帖子
        return pListRequest.normalPosts(accessToken = user.token,
                accessSecret = user.secrete,
                boardId = fid,
                page = page,
                pageSize = COMMON_PAGE_SIZE,
                sortby = "new",
                filterType = "sortid",
                isImageList = "fales",
                topOrdere = "0")
    }
}