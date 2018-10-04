/*
 * Created by Febers at 18-8-15 下午11:38.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-15 下午11:38.
 */

package com.febers.uestc_bbs.module.post.model

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.dao.PostStore
import com.febers.uestc_bbs.entity.PListResultBean
import com.febers.uestc_bbs.entity.SimplePListBean
import com.febers.uestc_bbs.module.post.presenter.PListContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PListModelImpl(val PListPresenter: PListContract.Presenter) : BaseModel(), PListContract.Model {

    override fun topicService(_fid: String, _page: Int, _refresh: Boolean) {
        mFid = _fid
        mPage = _page.toString()

        Thread(Runnable {
            getSavedPList()
        }).start()
        Thread(Runnable {
            getPList()
        }).start()
    }

    private fun getPList() {
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
                if(body.rs != REQUEST_SUCCESS_RS) {
                    PListPresenter.pListResult(BaseEvent(BaseCode.FAILURE, arrayListOf(SimplePListBean(title = body.head?.errInfo))))
                    return
                }
                if (body.has_next != HAVE_NEXT_PAGE) {
                    PListPresenter.pListResult(BaseEvent(BaseCode.SUCCESS_END, body.list))
                } else {
                    PListPresenter.pListResult(BaseEvent(BaseCode.SUCCESS, body.list))
                }
                //if (mPage == FIRST_PAGE) PostStore.savePostList(mFid, body)
            }
        })
    }

    private fun getCall(): Call<PListResultBean>{
        val pListRequest = getRetrofit().create(PListInterface::class.java)
        //最新发表
        if (mFid == HOME_POSTS_NEW) {
            return pListRequest.newPosts(r = "forum/topiclist",
                    boardId = "0",
                    page = mPage,
                    pageSize = COMMON_PAGE_SIZE,
                    sortby = "new")
        }
        //最新回复
        if (mFid == HOME_POSTS_REPLY) {
            return pListRequest.newPosts(r = "forum/topiclist",
                    boardId = "0",
                    page = mPage,
                    pageSize = COMMON_PAGE_SIZE,
                    sortby = "all")
        }
        //热门帖子
        if (mFid == HOME_POSTS_HOT) {
            return pListRequest.hotPosts(r = "portal/newslist",
                    moduleId = "2",
                    page = mPage,
                    pageSize = COMMON_PAGE_SIZE)
        }
        //版块里的帖子
        return pListRequest.normalPosts(
                boardId = mFid,
                page = mPage,
                pageSize = COMMON_PAGE_SIZE,
                sortby = "new",
                filterType = "sortid",
                isImageList = "fales",
                topOrdere = "0")
    }

    private fun getSavedPList() {
        if (mPage != FIRST_PAGE) return
        PostStore.getPostList(mFid).apply {
            if (this.list != null) {
                PListPresenter.pListResult(BaseEvent(BaseCode.LOCAL, this.list))
            }
        }
    }
}