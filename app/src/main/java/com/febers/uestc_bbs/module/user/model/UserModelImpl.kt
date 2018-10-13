package com.febers.uestc_bbs.module.user.model

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.dao.PostStore
import com.febers.uestc_bbs.entity.UserDetailBean
import com.febers.uestc_bbs.entity.UserPostBean
import com.febers.uestc_bbs.module.user.presenter.UserContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserModelImpl(private val presenter: UserContract.Presenter): BaseModel(), UserContract.Model {

    override fun userPostService(uid: Int, type: String, page: Int) {
        mUid = uid.toString()
        mPage = page.toString()
        mType = type
        Thread(Runnable { getUserPost() }).start()
        //Thread(Runnable { getSavedPList() }).start()
    }

    override fun userDetailService(uid: Int) {
        mUid = uid.toString()
        Thread(Runnable { getUserDetail() }).start()
    }

    private fun getUserPost() {
        getUserPListCall().enqueue(object : Callback<UserPostBean> {
            override fun onFailure(call: Call<UserPostBean>?, t: Throwable?) {
                presenter.errorResult(SERVICE_RESPONSE_ERROR)
            }

            override fun onResponse(call: Call<UserPostBean>?, response: Response<UserPostBean>?) {
                val userPostBean = response?.body()
                if (userPostBean == null) {
                    presenter.errorResult(SERVICE_RESPONSE_NULL)
                    return
                }
                if (userPostBean.rs != REQUEST_SUCCESS_RS) {
                    presenter.errorResult(userPostBean.head?.errInfo.toString())
                    return
                }
                presenter.userPostResult(BaseEvent(
                        if (userPostBean.has_next != HAVE_NEXT_PAGE)BaseCode.SUCCESS_END
                        else BaseCode.SUCCESS, userPostBean))
                //if (mPage == FIRST_PAGE) PostStore.saveUserPList(mFid, userPListBean)
            }
        })
    }

    private fun getUserDetail() {
        getRetrofit().create(UserInterface::class.java)
                .getUserDetail(userId = mUid)
                .enqueue(object : Callback<UserDetailBean> {
                    override fun onFailure(call: Call<UserDetailBean>, t: Throwable) {
                        presenter.errorResult(SERVICE_RESPONSE_ERROR)
                    }

                    override fun onResponse(call: Call<UserDetailBean>, response: Response<UserDetailBean>) {
                        val userDetailBean = response?.body()
                        if (userDetailBean == null) {
                            presenter.errorResult(SERVICE_RESPONSE_NULL)
                            return
                        }
                        if (userDetailBean.rs != REQUEST_SUCCESS_RS) {
                            presenter.errorResult(userDetailBean.head?.errInfo.toString())
                            return
                        }
                        presenter.userDetailResult(BaseEvent(BaseCode.SUCCESS, userDetailBean))
                    }
                })
    }


    private fun getUserPListCall(): Call<UserPostBean> {
        val userPostRequest = getRetrofit().create(UserInterface::class.java)
        return when(mType) {
            USER_START_POST -> {
                userPostRequest.getUserStartPList(
                        uid = mUid,
                        page = mPage,
                        pageSize = COMMON_PAGE_SIZE)
            }
            USER_REPLY_POST -> {
                userPostRequest.getUserReplyPList(
                        uid = mUid,
                        page = mPage,
                        pageSize = COMMON_PAGE_SIZE)
            }
            USER_FAV_POST -> {
                userPostRequest.getUserFavPList(
                        uid = mUid,
                        page = mPage,
                        pageSize = COMMON_PAGE_SIZE)
            }
            else -> {
                userPostRequest.getUserStartPList(
                        uid = mUid,
                        page = mPage,
                        pageSize = COMMON_PAGE_SIZE)
            }
        }
    }

    private fun getSavedPList() {
        if (mPage != FIRST_PAGE) return
        PostStore.getUserPList(mFid).apply {
            if (this.list != null) {
                presenter.userPostResult(BaseEvent(BaseCode.LOCAL, this))
            }
        }
    }
}