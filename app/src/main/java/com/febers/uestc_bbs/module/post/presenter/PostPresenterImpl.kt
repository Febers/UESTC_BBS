package com.febers.uestc_bbs.module.post.presenter

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.*
import com.febers.uestc_bbs.http.PostDetailInterface
import com.febers.uestc_bbs.module.post.contract.PostContract
import com.febers.uestc_bbs.utils.log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostPresenterImpl(view: PostContract.View): PostContract.Presenter(view) {

    private var postId = 0

    override fun postDetailRequest(postId: Int, page: Int, authorId: Int, order: Int) {
        this.postId = postId
        ThreadPoolMgr.execute(Runnable { getPostDetail(postId, page, authorId, order) })
    }

    private fun getPostDetail(postId: Int, page: Int, authorId: Int, order: Int) {
        val postRequest = getRetrofit().create(PostDetailInterface::class.java)
        val call = postRequest.getPostDetail(
                topicId = postId.toString(),
                authorId = authorId.toString(),
                order = order.toString(),
                page = page.toString(),
                pageSize = COMMON_PAGE_SIZE.toString())
        call.enqueue(object : Callback<PostDetailBean> {
            override fun onFailure(call: Call<PostDetailBean>?, t: Throwable?) {
                log { "Detail onFail： $t" }
                //没有网络时：java.net.UnknownHostException: Unable to resolve host "bbs.uestc.edu.cn": No address associated with hostname
                errorResult(SERVICE_RESPONSE_ERROR)
            }

            override fun onResponse(call: Call<PostDetailBean>?, response: Response<PostDetailBean>?) {
                val postResultBean = response?.body()
                //一些板块的帖子无法查看，服务器会返回空
                if (postResultBean == null) {
                    log { "Detail null" }
                    errorResult("${SERVICE_RESPONSE_NULL}，请访问 Web 页面，查看该帖子")
                    return
                }
                if (postResultBean.rs != REQUEST_SUCCESS_RS) {
                    errorResult(postResultBean.head?.errInfo.toString())
                    return
                }
                log { "has next: ${postResultBean.has_next}" }
                if (postResultBean.has_next != HAVE_NEXT_PAGE) {
                    mView?.showPostDetail(BaseEvent(BaseCode.SUCCESS_END, postResultBean))
                    return
                }
                mView?.showPostDetail(BaseEvent(BaseCode.SUCCESS, postResultBean))
            }
        })
    }

    override fun postReplyRequest(postId: Int, isQuota: Int, replyId: Int, aid: String, vararg contents: Pair<Int, String>) {
        ThreadPoolMgr.execute(Runnable { postReply(postId, isQuota, replyId, aid, *contents) })
    }

    private fun postReply(postId: Int, isQuota: Int, replyId: Int, aid: String, vararg contents: Pair<Int, String>) {
        val stContents = StringBuilder()
        contents.forEach {
            val newContent = it.second.replace("\n", """\\n""") //非常重要，解决服务器不识别换行问题
            stContents.append("""{"type":${it.first},"infor":"$newContent"},""")
        }
        //清除末尾的逗号
        stContents.deleteCharAt(stContents.lastIndex)
        getRetrofit().create(PostDetailInterface::class.java)
                .postReply(json = """
                    {"body":
                        {"json":
                            {
                                "tid":$postId,
                                "aid":"$aid",
                                "isAnonymous":0,
                                "isOnlyAuthor":0,
                                "isQuote":$isQuota,
                                "replyId":$replyId,
                                "content":"[$stContents]"
                            }
                        }
                    }
                        """)
                .enqueue(object : Callback<PostSendResultBean> {
                    override fun onFailure(call: Call<PostSendResultBean>, t: Throwable?) {
                        errorResult(t.toString())
                    }

                    override fun onResponse(call: Call<PostSendResultBean>, response: Response<PostSendResultBean>?) {
                        val replySendResultBean = response?.body()
                        if (replySendResultBean == null) {
                            errorResult(SERVICE_RESPONSE_NULL)
                            return
                        }
                        if (replySendResultBean.rs != REQUEST_SUCCESS_RS) {
                            errorResult(replySendResultBean.head?.errInfo.toString())
                            return
                        }
                        mView?.showPostReplyResult(BaseEvent(BaseCode.SUCCESS, replySendResultBean))
                    }
                })
    }

    override fun postFavRequest(action: String) {
        ThreadPoolMgr.execute(Runnable { postFav(action) })
    }

    private fun postFav(action: String) {
        getRetrofit().create(PostDetailInterface::class.java)
                .postFavorite(action = action,
                        id = postId.toString())
                .enqueue(object : Callback<PostFavResultBean> {
                    override fun onFailure(call: Call<PostFavResultBean>, t: Throwable) {
                        errorResult(t.toString())
                    }

                    override fun onResponse(call: Call<PostFavResultBean>, response: Response<PostFavResultBean>) {
                        val result = response.body()
                        if (result == null) {
                            mView?.showPostFavResult(BaseEvent(BaseCode.FAILURE, PostFavResultBean().apply {
                                errcode = SERVICE_RESPONSE_NULL
                            }))
                            return
                        }
                        if (result.rs != REQUEST_SUCCESS_RS) {
                            mView?.showPostFavResult(BaseEvent(BaseCode.FAILURE, result))
                            return
                        }
                        mView?.showPostFavResult(BaseEvent(BaseCode.SUCCESS, result))
                    }
                })
    }

    override fun postSupportRequest(postId: Int, tid: Int) {
        ThreadPoolMgr.execute(Runnable { postSupport(postId, tid) })
    }

    private fun postSupport(postId: Int, tid: Int) {
        getRetrofit().create(PostDetailInterface::class.java)
                .postSupport(tid = tid.toString(), pid = postId.toString())
                .enqueue(object : Callback<PostSupportResultBean> {
                    override fun onFailure(call: Call<PostSupportResultBean>, t: Throwable) {
                        mView?.showPostSupportResult(BaseEvent(BaseCode.FAILURE, PostSupportResultBean().apply { errcode = SERVICE_RESPONSE_ERROR }))
                    }

                    override fun onResponse(call: Call<PostSupportResultBean>, response: Response<PostSupportResultBean>) {
                        val result = response.body()
                        if (result == null) {
                            mView?.showPostSupportResult(BaseEvent(BaseCode.LOCAL, PostSupportResultBean().apply { errcode = SERVICE_RESPONSE_NULL }))
                            return
                        }
                        if (result.rs != REQUEST_SUCCESS_RS) {
                            mView?.showPostSupportResult(BaseEvent(BaseCode.LOCAL, result))
                            return
                        }
                        mView?.showPostSupportResult(BaseEvent(BaseCode.SUCCESS, result))
                    }
                })
    }

    override fun postVoteRequest(pollItemId: List<Int>) {
        ThreadPoolMgr.execute(Runnable { postVote(pollItemId) })
    }

    private fun postVote(pollItemId: List<Int>) {
        getRetrofit().create(PostDetailInterface::class.java)
                //将list.toString之后前后的[]删去
                .postVote(tid = postId.toString(), options = pollItemId.toString().replace("[","").replace("]",""))
                .enqueue(object : Callback<PostVoteResultBean> {
                    override fun onFailure(call: Call<PostVoteResultBean>, t: Throwable) {
                        errorResult(SERVICE_RESPONSE_ERROR)
                    }

                    override fun onResponse(call: Call<PostVoteResultBean>, response: Response<PostVoteResultBean>) {
                        val result = response.body()
                        if (result == null) {
                            errorResult(SERVICE_RESPONSE_NULL)
                            return
                        }
                        if (result.rs != REQUEST_SUCCESS_RS) {
                            errorResult(result.head?.errInfo.toString())
                            return
                        }
                        mView?.showVoteResult(BaseEvent(BaseCode.SUCCESS, result))
                    }
                })
    }

    override fun userAtRequest(page: Int) {
        ThreadPoolMgr.execute(Runnable { getUsersAt(page) })
    }

    private fun getUsersAt(page: Int) {
        getRetrofit().create(PostDetailInterface::class.java)
                .userAt(page = page.toString(), pageSize = COMMON_PAGE_SIZE.toString())
                .enqueue(object : Callback<UserCanAtBean> {
                    override fun onFailure(call: Call<UserCanAtBean>, t: Throwable) {
                        log("err on at $t")
                    }

                    override fun onResponse(call: Call<UserCanAtBean>, response: Response<UserCanAtBean>) {
                        val result = response.body()
                        if (result == null) {
                            errorResult(SERVICE_RESPONSE_NULL)
                            return
                        }
                        if (result.rs != REQUEST_SUCCESS_RS) {
                            errorResult(result.head.errInfo)
                            return
                        }
                        mView?.showUserAtResult(BaseEvent(BaseCode.SUCCESS, result))
                    }
                })
    }
}