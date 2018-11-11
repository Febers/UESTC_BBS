package com.febers.uestc_bbs.io

import android.util.Log
import android.util.Log.i
import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BaseModel
import com.febers.uestc_bbs.base.REQUEST_SUCCESS_RS
import com.febers.uestc_bbs.entity.UploadResultBean
import com.febers.uestc_bbs.http.TokenClient
import com.febers.uestc_bbs.utils.ApiUtils
import com.google.gson.JsonObject
import okhttp3.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * 提供文件上传功能
 */
class FileUploader: BaseModel() {

    /**
     * 将图片上传至河畔服务器
     * 如果上传成功，应该返回包含两个值的json bean
     * 第一个 aid 供发帖时调用
     * 第二个 infor 同样用以发帖
     * @param imageFile 图片文件
     */
    fun uploadPostImageToBBS(imageFile: File, listener: OnFileUploadListener) {
        Thread{
            val imageBody: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)
            val imagePart: MultipartBody.Part = MultipartBody.Part.createFormData("uploadFile[]", imageFile.name, imageBody)

            getRetrofit().create(UploadToBBSInterface::class.java)
                    .uploadImage(params = mapOf("type" to "image", "module" to "forum"),
                            image = imagePart)
                    .enqueue(object : Callback<UploadResultBean> {
                        override fun onFailure(call: Call<UploadResultBean>, t: Throwable) {
                            listener.onUploadFail("Upload Image Fail:" + t.toString())
//                            i("PE", "upload img, fail: " + t.toString())
                        }

                        override fun onResponse(call: Call<UploadResultBean>, response: Response<UploadResultBean>) {
                            val resultBean = response.body()
                            if (resultBean == null || resultBean.rs != REQUEST_SUCCESS_RS || resultBean.body?.attachment?.size!! == 0) {
                                listener.onUploadFail("Upload Image Fail:" + resultBean?.head?.errInfo)
//                                i("PE", "upload img, rs error")
                                return
                            }
                            listener.onUploadSuccess(BaseEvent(BaseCode.SUCCESS, resultBean))
                        }
                    })
        }.start()
    }


    interface OnFileUploadListener {
        fun onUploadFail(msg: String)
        fun onUploadSuccess(event: BaseEvent<UploadResultBean>)
    }
}

interface UploadToBBSInterface {

    @Multipart
    @POST(ApiUtils.BBS_SEND_ATTACHMENTEX_URL)
    fun uploadImage(@QueryMap()params: Map<String, String>, @Part()image: MultipartBody.Part) : Call<UploadResultBean>

}