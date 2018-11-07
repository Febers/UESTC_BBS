package com.febers.uestc_bbs.module.user.model

import android.util.Log.i
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.UserDetailBean
import com.febers.uestc_bbs.entity.UserPostBean
import com.febers.uestc_bbs.entity.UserUpdateResultBean
import com.febers.uestc_bbs.module.user.presenter.UserContract
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UserModelImpl(private val presenter: UserContract.Presenter): BaseModel(), UserContract.Model {

    override fun userPostService(uid: Int, type: String, page: Int) {
        mUid = uid.toString()
        mPage = page.toString()
        mType = type
        Thread(Runnable { getUserPost() }).start()
    }

    override fun userDetailService(uid: Int) {
        mUid = uid.toString()
        Thread(Runnable { getUserDetail() }).start()
    }

    override fun <T> userUpdateService(type: String, newValue: T, oldValue: T?) {
        Thread{
            userUpdate(type, newValue, oldValue)
        }.start()
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

    private fun <T> userUpdate(type: String, newValue: T, oldValue: T?) {
        when(type) {
            USER_SIGN -> {
                if (newValue is String) {
                    getRetrofit().create(UserInterface::class.java)
                            .updateUserSign(sign = newValue)
                            .enqueue(object : Callback<UserUpdateResultBean> {
                                override fun onFailure(call: Call<UserUpdateResultBean>, t: Throwable) {
                                    presenter.errorResult(t.toString())
                                }

                                override fun onResponse(call: Call<UserUpdateResultBean>, response: Response<UserUpdateResultBean>) {
                                    val resultBean = response?.body()
                                    if (resultBean == null) {
                                        presenter.errorResult(SERVICE_RESPONSE_NULL)
                                        return
                                    }
                                    if (resultBean.rs != REQUEST_SUCCESS_RS) {
                                        presenter.errorResult(resultBean.head?.errInfo.toString())
                                        return
                                    }
                                    presenter.userUpdateResult(BaseEvent(BaseCode.SUCCESS, resultBean))
                                }
                            })
                }
            }
            USER_GENDER -> {
                if (newValue is String)
                getRetrofit().create(UserInterface::class.java)
                        .updateUserGender(gender = newValue)
                        .enqueue(object : Callback<UserUpdateResultBean> {
                            override fun onFailure(call: Call<UserUpdateResultBean>, t: Throwable) {

                            }

                            override fun onResponse(call: Call<UserUpdateResultBean>, response: Response<UserUpdateResultBean>) {

                            }
                        })
            }
            USER_AVATAR -> {
                if (newValue is File) {
                    val avatarBody: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), newValue)
                    val avatarPart: MultipartBody.Part = MultipartBody.Part.createFormData("userAvatar", newValue.name, avatarBody)

                    getRetrofit().create(UserInterface::class.java)
                            .updateUserAvatar(avatar = avatarPart)
                            .enqueue(object : Callback<UserUpdateResultBean> {
                                override fun onFailure(call: Call<UserUpdateResultBean>, t: Throwable) {
                                    i("Uri fail: ", t.toString())
                                }

                                override fun onResponse(call: Call<UserUpdateResultBean>, response: Response<UserUpdateResultBean>?) {
                                    if (response != null)
                                        i("Uri response: ", response.body()?.head?.errInfo)
                                    else
                                        i("Uri response: ", "res is null")
                                }
                            })
                }
            }
        }
    }

    private fun getUserPListCall(): Call<UserPostBean> {
        val userPostRequest = getRetrofit().create(UserInterface::class.java)
        return when(mType) {
            USER_START_POST -> {
                userPostRequest.getUserStartPList(
                        uid = mUid,
                        page = mPage,
                        pageSize = COMMON_PAGE_SIZE.toString())
            }
            USER_REPLY_POST -> {
                userPostRequest.getUserReplyPList(
                        uid = mUid,
                        page = mPage,
                        pageSize = COMMON_PAGE_SIZE.toString())
            }
            USER_FAV_POST -> {
                userPostRequest.getUserFavPList(
                        uid = mUid,
                        page = mPage,
                        pageSize = COMMON_PAGE_SIZE.toString())
            }
            else -> {
                userPostRequest.getUserStartPList(
                        uid = mUid,
                        page = mPage,
                        pageSize = COMMON_PAGE_SIZE.toString())
            }
        }
    }
}