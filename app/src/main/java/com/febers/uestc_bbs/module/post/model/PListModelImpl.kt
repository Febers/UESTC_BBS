package com.febers.uestc_bbs.module.post.model

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.BoardListBean_
import com.febers.uestc_bbs.io.PostHelper
import com.febers.uestc_bbs.entity.PostListBean
import com.febers.uestc_bbs.module.post.contract.PListContract
import com.febers.uestc_bbs.module.post.model.http_interface.PListInterface
import com.febers.uestc_bbs.utils.log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PListModelImpl(val pListPresenter: PListContract.Presenter) : BaseModel(), PListContract.Model {

    private var savedPListGot = false

    override fun pListService(fid: Int, page: Int) {
        mFid = fid.toString()
        mPage = page.toString()

        Thread(Runnable {
            getSavedPList()
        }).start()
        Thread(Runnable {
            getPList()
        }).start()
    }

    override fun boardListService(fid: Int) {
        mFid = fid.toString()
        Thread { getBoardList() }.start()
    }

    private fun getPList() {
        getCall().enqueue(object : Callback<PostListBean> {
            override fun onFailure(call: Call<PostListBean>?, t: Throwable?) {
                pListPresenter.errorResult(SERVICE_RESPONSE_ERROR)
            }

            override fun onResponse(call: Call<PostListBean>?, response: Response<PostListBean>?) {
                val body = response?.body()
                if (body == null) {
                    //log("服务器无响应：body == null and fid is $mFid")
                    pListPresenter.errorResult("该版块$SERVICE_RESPONSE_NULL,请点击右上角 菜单->访问Web页面 查看")
                    return
                }
                if(body.rs != REQUEST_SUCCESS_RS) {
                    pListPresenter.errorResult(body.head?.errInfo.toString())
                    return
                }
                if (body.has_next != HAVE_NEXT_PAGE) {
                    pListPresenter.pListResult(BaseEvent(BaseCode.SUCCESS_END, body))
                } else {
                    pListPresenter.pListResult(BaseEvent(BaseCode.SUCCESS, body))
                }
                //保存首页的第一页帖子列表
                if (mPage == FIRST_PAGE.toString() && mFid.toInt() < 0)
                    //PostHelper.savePostListToSp(mFid, body)
                    PostHelper.savePostListToFile(mFid, body)
            }
        })
    }

    private fun getBoardList() {
        getRetrofit().create(PListInterface::class.java)
                .boardList(fid = mFid)
                .enqueue(object : Callback<BoardListBean_> {
                    override fun onFailure(call: Call<BoardListBean_>, t: Throwable) {
                        pListPresenter.errorResult("获取子版块失败")
                    }

                    override fun onResponse(call: Call<BoardListBean_>?, response: Response<BoardListBean_>?) {
                        val body = response?.body()
                        if (body == null) {
                            pListPresenter.errorResult("获取子版块失败")
                            return
                        }
                        if (body.rs != REQUEST_SUCCESS_RS) {
                            pListPresenter.errorResult(body.head?.errInfo.toString())
                            return
                        }
                        pListPresenter.boardListResult(BaseEvent(BaseCode.SUCCESS, body))
                    }
                })
    }

    private fun getCall(): Call<PostListBean>{
        val pListRequest = getRetrofit().create(PListInterface::class.java)
        //最新发表
        if (mFid == HOME_POSTS_NEW.toString()) {
            return pListRequest.newPosts(r = "forum/topiclist",
                    boardId = "0",
                    page = mPage,
                    pageSize = COMMON_PAGE_SIZE.toString(),
                    sortby = "new")
        }
        //最新回复
        if (mFid == HOME_POSTS_REPLY.toString()) {
            return pListRequest.newPosts(r = "forum/topiclist",
                    boardId = "0",
                    page = mPage,
                    pageSize = COMMON_PAGE_SIZE.toString(),
                    sortby = "all")
        }
        //热门帖子
        if (mFid == HOME_POSTS_HOT.toString()) {
            return pListRequest.hotPosts(r = "portal/newslist",
                    moduleId = "2",
                    page = mPage,
                    pageSize = COMMON_PAGE_SIZE.toString())
        }
        //版块里的帖子
        return pListRequest.normalPosts(
                boardId = mFid,
                page = mPage,
                pageSize = COMMON_PAGE_SIZE.toString(),
                sortby = "new",
                filterType = "sortid",
                isImageList = "false",
                topOrder = mTopOrder)
    }

    private fun getSavedPList() {
        if (mPage != FIRST_PAGE.toString() || mFid.toInt() >= 0 || savedPListGot) return
        PostHelper.getPostListByFile(mFid).apply {
            log("PLIST"+this.list?.size.toString())
            if (this.list != null) {
                pListPresenter.pListResult(BaseEvent(BaseCode.LOCAL, this))
            }
        }
        savedPListGot = true
    }
}