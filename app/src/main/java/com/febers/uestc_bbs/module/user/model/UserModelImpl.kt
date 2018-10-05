package com.febers.uestc_bbs.module.user.model

import android.util.Log.i
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.dao.PostStore
import com.febers.uestc_bbs.entity.UserDetailBean
import com.febers.uestc_bbs.entity.UserPListBean
import com.febers.uestc_bbs.module.user.presenter.UserContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserModelImpl(private val presenter: UserContract.Presenter): BaseModel(), UserContract.Model {

    override fun userPListService(uid: String, type: String, page: String) {
        mUid = uid
        mPage = page
        mType = type
        Thread(Runnable { getUserPList() }).start()
        //Thread(Runnable { getSavedPList() }).start()
    }

    override fun userDetailService(uid: String) {
        mUid = uid
        Thread(Runnable { getUserDetail() }).start()
    }

    private fun getUserPList() {
        getUserPListCall().enqueue(object : Callback<UserPListBean> {
            override fun onFailure(call: Call<UserPListBean>?, t: Throwable?) {
                presenter.userPListResult(BaseEvent(BaseCode.FAILURE, UserPListBean()
                        .apply { errcode = SERVICE_RESPONSE_ERROR + t.toString() }))
            }

            override fun onResponse(call: Call<UserPListBean>?, response: Response<UserPListBean>?) {
                val userPListBean = response?.body()
                if (userPListBean == null) {
                    presenter.userPListResult(BaseEvent(BaseCode.FAILURE, UserPListBean()
                            .apply { errcode = SERVICE_RESPONSE_NULL }))
                    return
                }
                if (userPListBean.rs != REQUEST_SUCCESS_RS) {
                    presenter.userPListResult(BaseEvent(BaseCode.FAILURE, UserPListBean()
                            .apply { errcode = userPListBean.head?.errInfo}))
                    return
                }
                presenter.userPListResult(BaseEvent(
                        if (userPListBean.has_next != HAVE_NEXT_PAGE)BaseCode.SUCCESS_END
                        else BaseCode.SUCCESS, userPListBean))
                //if (mPage == FIRST_PAGE) PostStore.saveUserPList(mFid, userPListBean)
            }
        })
    }

    private fun getUserDetail() {
        getRetrofit().create(UserInterface::class.java)
                .getUserDetail(userId = mUid)
                .enqueue(object : Callback<UserDetailBean> {
                    override fun onFailure(call: Call<UserDetailBean>, t: Throwable) {
                        presenter.userDetailResult(BaseEvent(BaseCode.FAILURE, UserDetailBean().apply {
                            errcode = SERVICE_RESPONSE_ERROR + t.toString()
                        }))
                    }

                    override fun onResponse(call: Call<UserDetailBean>, response: Response<UserDetailBean>) {
                        val userDetailBean = response?.body()
                        if (userDetailBean == null) {
                            presenter.userDetailResult(BaseEvent(BaseCode.FAILURE, UserDetailBean().apply {
                                errcode = SERVICE_RESPONSE_NULL
                            }))
                            return
                        }
                        presenter.userDetailResult(BaseEvent(BaseCode.SUCCESS, userDetailBean))
                    }
                })
    }


    private fun getUserPListCall(): Call<UserPListBean> {
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
                presenter.userPListResult(BaseEvent(BaseCode.LOCAL, this))
            }
        }
    }
}