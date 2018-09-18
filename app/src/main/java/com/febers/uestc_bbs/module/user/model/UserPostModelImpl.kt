package com.febers.uestc_bbs.module.user.model

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.UserPostBean
import com.febers.uestc_bbs.module.post.model.COMMON_PAGE_SIZE
import com.febers.uestc_bbs.module.user.presenter.UserContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPostModelImpl(private val presenter: UserContract.Presenter): BaseModel(), UserContract.Model {

    private var userPostType = USER_START_POST

    override fun userPostService(uid: String, type: Int, page: String) {
        mUid = uid
        mPage = page
        userPostType = type
        Thread(Runnable { getUserPost() }).start()
    }

    private fun getUserPost() {
        getCall().enqueue(object : Callback<UserPostBean> {
            override fun onFailure(call: Call<UserPostBean>?, t: Throwable?) {
                presenter.userPostResult(BaseEvent(BaseCode.FAILURE, UserPostBean().apply { errcode = SERVICE_RESPONSE_ERROR + t.toString() }))
            }

            override fun onResponse(call: Call<UserPostBean>?, response: Response<UserPostBean>?) {
                val userPostBean = response?.body()
                if (userPostBean == null) {
                    presenter.userPostResult(BaseEvent(BaseCode.FAILURE, UserPostBean().apply { errcode = SERVICE_RESPONSE_NULL }))
                    return
                }
                if (userPostBean.rs != REQUEST_SUCCESS_RS) {
                    presenter.userPostResult(BaseEvent(BaseCode.FAILURE, UserPostBean().apply { errcode = userPostBean.head?.errInfo}))
                    return
                }
                if (userPostBean.has_next != HAVE_NEXT_PAGE) {
                    presenter.userPostResult(BaseEvent(BaseCode.SUCCESS_END, userPostBean))
                } else {
                    presenter.userPostResult(BaseEvent(BaseCode.SUCCESS, userPostBean))
                }
            }
        })
    }

    private fun getCall(): Call<UserPostBean> {
        val userPostRequest = getRetrofit().create(UserInterface::class.java)
        return when(userPostType) {
            USER_START_POST -> {
                userPostRequest.getUserStartPost(accessToken = getUser().token,
                        accessSecret = getUser().secrete,
                        uid = mUid,
                        page = mPage,
                        pageSize = COMMON_PAGE_SIZE)
            }
            USER_REPLY_POST -> {
                userPostRequest.getUserReplyPost(accessToken = getUser().token,
                        accessSecret = getUser().secrete,
                        uid = mUid,
                        page = mPage,
                        pageSize = COMMON_PAGE_SIZE)
            }
            USER_FAV_POST -> {
                userPostRequest.getUserFavPost(accessToken = getUser().token,
                        accessSecret = getUser().secrete,
                        uid = mUid,
                        page = mPage,
                        pageSize = COMMON_PAGE_SIZE)
            }
            else -> {
                userPostRequest.getUserStartPost(accessToken = getUser().token,
                        accessSecret = getUser().secrete,
                        uid = mUid,
                        page = mPage,
                        pageSize = COMMON_PAGE_SIZE)
            }

        }
    }
}