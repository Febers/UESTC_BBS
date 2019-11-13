package com.febers.uestc_bbs.module.post.presenter

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.BoardListBean_
import com.febers.uestc_bbs.entity.PostListBean
import com.febers.uestc_bbs.http.PostListInterface
import com.febers.uestc_bbs.io.PostManager
import com.febers.uestc_bbs.module.post.contract.PListContract
import com.febers.uestc_bbs.utils.log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PListPresenterImpl(view: PListContract.View) : PListContract.Presenter(view) {

    private var getSavedListFlag = false
    private var filterType = PLIST_SORT_BY_TYPE //默认帖子分类
    private var filterId = 0
    private var topOrder: String = "1"   //默认返回本版置顶帖

    override fun pListRequest(fid: Int, page: Int, pageSize:Int, filterType: String, filterId: Int) {
        ThreadPoolMgr.execute(Runnable { getSavedPList(fid, page) })
        ThreadPoolMgr.execute(Runnable { getPList(fid, page) })
    }

    private fun getPList(fid: Int, page: Int) {
        getCall(fid, page).enqueue(object : Callback<PostListBean> {
            override fun onFailure(call: Call<PostListBean>?, t: Throwable?) {
                errorResult(SERVICE_RESPONSE_ERROR)
            }

            override fun onResponse(call: Call<PostListBean>?, response: Response<PostListBean>?) {
                val body = response?.body()
                if (body == null) {
                    //log("服务器无响应：body == null and fid is $mFid")
                    errorResult("该版块$SERVICE_RESPONSE_NULL，请访问 Web 页面，查看该版块")
                    return
                }
                if(body.rs != REQUEST_SUCCESS_RS) {
                    errorResult(body.head?.errInfo.toString())
                    return
                }
                log { "has next: ${body.has_next}" }
                if (body.has_next != HAVE_NEXT_PAGE) {
                    mView?.showPList(BaseEvent(BaseCode.SUCCESS_END, body))
                } else {
                    mView?.showPList(BaseEvent(BaseCode.SUCCESS, body))
                }
                //保存首页的第一页帖子列表
                if (page == FIRST_PAGE && fid < 0)
                    PostManager.savePostListToFile(fid, body)
            }
        })
    }

    private fun getSavedPList(fid: Int, page: Int) {
        if (page != FIRST_PAGE || fid >= 0 || getSavedListFlag) {
            return
        }
        PostManager.getPostListByFile(fid).apply {
            if (this.list != null) {
                mView?.showPList(BaseEvent(BaseCode.LOCAL, this))
            }
        }
        getSavedListFlag = true
    }

    private fun getCall(fid: Int, page: Int): Call<PostListBean>{
        val pListRequest = getRetrofit().create(PostListInterface::class.java)
        //最新发表
        if (fid == HOME_POSTS_NEW) {
            return pListRequest.newPosts(r = "forum/topiclist",
                    boardId = "0",
                    page = page.toString(),
                    pageSize = COMMON_PAGE_SIZE.toString(),
                    sortby = "new")
        }
        //最新回复
        if (fid == HOME_POSTS_REPLY) {
            return pListRequest.newPosts(r = "forum/topiclist",
                    boardId = "0",
                    page = page.toString(),
                    pageSize = COMMON_PAGE_SIZE.toString(),
                    sortby = "all")
        }
        //热门帖子
        if (fid == HOME_POSTS_HOT) {
            return pListRequest.hotPosts(r = "portal/newslist",
                    moduleId = "2",
                    page = page.toString(),
                    pageSize = COMMON_PAGE_SIZE.toString())
        }
        //版块里的帖子
        return pListRequest.normalPosts(
                boardId = fid.toString(),
                page = page.toString(),
                pageSize = COMMON_PAGE_SIZE.toString(),
                sortby = "new",
                filterType = filterType,
                filterId = filterId.toString(),
                isImageList = "false",
                topOrder = topOrder)
    }

    override fun boardListRequest(fid: Int) {
        ThreadPoolMgr.execute(Runnable { getBoardList(fid) })
    }

    private fun getBoardList(fid: Int) {
        getRetrofit().create(PostListInterface::class.java)
                .boardList(fid = fid.toString())
                .enqueue(object : Callback<BoardListBean_> {
                    override fun onFailure(call: Call<BoardListBean_>, t: Throwable) {
                        errorResult("获取子版块失败")
                    }

                    override fun onResponse(call: Call<BoardListBean_>?, response: Response<BoardListBean_>?) {
                        val body = response?.body()
                        if (body == null) {
                            errorResult("获取子版块失败")
                            return
                        }
                        if (body.rs != REQUEST_SUCCESS_RS) {
                            errorResult(body.head?.errInfo.toString())
                            return
                        }
                        mView?.showBoardList(BaseEvent(BaseCode.SUCCESS, body))
                    }
                })
    }

}
