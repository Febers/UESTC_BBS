package com.febers.uestc_bbs.module.user.presenter

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.UserDetailBean
import com.febers.uestc_bbs.entity.UserPostBean
import com.febers.uestc_bbs.entity.UserUpdateResultBean
import com.febers.uestc_bbs.http.UserInterface
import com.febers.uestc_bbs.module.user.contract.UserContract
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UserPresenterImpl(view: UserContract.View): UserContract.Presenter(view) {

    override fun userPostRequest(uid: Int, type: Int, page: Int) {
        ThreadPoolMgr.execute(Runnable { getUserPost(uid, type, page) })
    }

    private fun getUserPost(uid: Int, type: Int, page: Int) {
        getUserPListCall(uid, type, page)
                .enqueue(object : Callback<UserPostBean> {
                    override fun onFailure(call: Call<UserPostBean>?, t: Throwable?) {
                        errorResult(SERVICE_RESPONSE_ERROR)
                    }

                    override fun onResponse(call: Call<UserPostBean>?, response: Response<UserPostBean>?) {
                        val userPostBean = response?.body()
                        if (userPostBean == null) {
                            errorResult(SERVICE_RESPONSE_NULL)
                            return
                        }
                        if (userPostBean.rs != REQUEST_SUCCESS_RS) {
                            errorResult(userPostBean.head?.errInfo.toString())
                            return
                        }
                        mView?.showUserPost(BaseEvent(
                                if (userPostBean.has_next != HAVE_NEXT_PAGE) BaseCode.SUCCESS_END
                                else BaseCode.SUCCESS, userPostBean))
                    }
                })
    }

    override fun userDetailRequest(uid: Int) {
        ThreadPoolMgr.execute(Runnable { getUserDetail(uid) })
    }

    private fun getUserDetail(uid: Int) {
        getRetrofit().create(UserInterface::class.java)
                .getUserDetail(userId = uid.toString())
                .enqueue(object : Callback<UserDetailBean> {
                    override fun onFailure(call: Call<UserDetailBean>, t: Throwable) {
                        errorResult(SERVICE_RESPONSE_ERROR)
                    }

                    override fun onResponse(call: Call<UserDetailBean>, response: Response<UserDetailBean>) {
                        val userDetailBean = response.body()
                        if (userDetailBean == null) {
                            errorResult(SERVICE_RESPONSE_NULL)
                            return
                        }
                        if (userDetailBean.rs != REQUEST_SUCCESS_RS) {
                            errorResult(userDetailBean.head?.errInfo.toString())
                            return
                        }
                        mView?.showUserDetail(BaseEvent(BaseCode.SUCCESS, userDetailBean))
                    }
                })
    }

    override fun <T> userUpdateRequest(type: String, newValue: T, oldValue: T?) {
        ThreadPoolMgr.execute(Runnable { userUpdate(type, newValue, oldValue) })
    }

    /**
     * 更新用户资料，目前服务器对更新头像的请求都回复失败
     *
     * @param type 类型
     * @param newValue 新值
     * @param oldValue 旧值，更新密码的时候会用到
     */
    private fun <T> userUpdate(type: String, newValue: T, oldValue: T?) {
        when(type) {
            USER_SIGN -> {
                if (newValue is String) {
                    getRetrofit().create(UserInterface::class.java)
                            .updateUserSign(sign = newValue)
                            .enqueue(object : Callback<UserUpdateResultBean> {
                                override fun onFailure(call: Call<UserUpdateResultBean>, t: Throwable) {
                                    errorResult(t.toString())
                                }

                                override fun onResponse(call: Call<UserUpdateResultBean>, response: Response<UserUpdateResultBean>) {
                                    val resultBean = response.body()
                                    if (resultBean == null) {
                                        errorResult(SERVICE_RESPONSE_NULL)
                                        return
                                    }
                                    if (resultBean.rs != REQUEST_SUCCESS_RS) {
                                        errorResult(resultBean.head?.errInfo.toString())
                                        return
                                    }
                                    mView?.showUserUpdate(BaseEvent(BaseCode.SUCCESS, resultBean))
                                }
                            })
                }
            }
            //TODO
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
                                    errorResult(t.toString())
                                }

                                override fun onResponse(call: Call<UserUpdateResultBean>, response: Response<UserUpdateResultBean>?) {
                                    val resultBean = response?.body()
                                    if (resultBean == null) {
                                        errorResult(SERVICE_RESPONSE_NULL)
                                        return
                                    }
                                    if (resultBean.rs != REQUEST_SUCCESS_RS) {
                                        errorResult(resultBean.head?.errInfo.toString())
                                        return
                                    }
                                    mView?.showUserUpdate(BaseEvent(BaseCode.SUCCESS, resultBean))
                                }
                            })
                }
            }
        }
    }

    private fun getUserPListCall(uid: Int, type: Int, page: Int): Call<UserPostBean> {
        val userPostRequest = getRetrofit().create(UserInterface::class.java)
        return when(type) {
            USER_START_POST -> {
                userPostRequest.getUserStartPList(
                        uid = uid.toString(),
                        page = page.toString(),
                        pageSize = COMMON_PAGE_SIZE.toString())
            }
            USER_REPLY_POST -> {
                userPostRequest.getUserReplyPList(
                        uid = uid.toString(),
                        page = page.toString(),
                        pageSize = COMMON_PAGE_SIZE.toString())
            }
            USER_FAV_POST -> {
                userPostRequest.getUserFavPList(
                        uid = uid.toString(),
                        page = page.toString(),
                        pageSize = COMMON_PAGE_SIZE.toString())
            }
            else -> {
                userPostRequest.getUserStartPList(
                        uid = uid.toString(),
                        page = page.toString(),
                        pageSize = COMMON_PAGE_SIZE.toString())
            }
        }
    }
}