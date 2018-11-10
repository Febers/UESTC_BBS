package com.febers.uestc_bbs.io

import android.util.Log
import android.util.Log.i
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

    private var fileUploadListener: OnFileUploadListener? = null

    fun uploadToBBSService(type: String, imageFile: File) {
        Thread{
            uploadFile(file = imageFile)

//            val imageBody: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)
//            val imagePart: MultipartBody.Part = MultipartBody.Part.createFormData("uploadFile[]", imageFile.name, imageBody)
//            getRetrofit().create(UploadToBBSInterface::class.java)
//                    .uploadImage(params = mapOf("type" to "image", "module" to "forum"),
//                            image = imagePart)
//                    .enqueue(object : Callback<UploadResultBean> {
//                        override fun onFailure(call: Call<UploadResultBean>, t: Throwable) {
//                            i("PE", "upload img, fail: " + t.toString())
//                        }
//
//                        override fun onResponse(call: Call<UploadResultBean>, response: Response<UploadResultBean>) {
//                            i("PE header:", response.headers().toString())
//                            i("PE mes ", response.message())
//                            i("PE raw ", response.raw().toString())
//                            val resultBean = response.body()
//                            i("PE", resultBean.toString())
//                            if (resultBean?.rs != REQUEST_SUCCESS_RS) {
//                                i("PE", "upload img, rs error")
//                                return
//                            }
//                            i("PE", "upload img, raw: " + resultBean.head?.errInfo)
//                        }
//                    })
        }.start()
    }

    /**
     * 上传文件
     * @param actionUrl 接口地址
     * @param paramsMap 参数
    </T> */
    private fun uploadFile(file: File) {
        try {
            //请求地址
            val requestUrl = ApiUtils.BBS_BASE_URL + ApiUtils.BBS_SEND_ATTACHMENTEX_URL + "&type=image&module=forum"
            val body = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("uploadFile[]", file.name, RequestBody.create(MediaType.parse("image/jpg"), file))
                    .build()
            val request = Request.Builder().url(requestUrl).post(body).build()
            TokenClient.get().newBuilder()
                    .writeTimeout(50, TimeUnit.SECONDS)
                    .build()
                    .newCall(request).enqueue(object : okhttp3.Callback {
                        override fun onFailure(call: okhttp3.Call, e: IOException) {
                            i("PE ok ", "fail")
                        }

                        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                            i("PE ok head ", ":" + response.headers())

                            i("PE ok body ", response.body()?.string())
                        }
                    })


        } catch (e: Exception) {
            Log.e("PE fun", e.toString())
        }

    }

    fun setFileUpdateListener(uploadListener: OnFileUploadListener) {
        this.fileUploadListener = uploadListener
    }

    interface OnFileUploadListener {
        fun onFileUploading(progress: Int)
    }
}

interface UploadToBBSInterface {

    @Multipart
    @POST(ApiUtils.BBS_SEND_ATTACHMENTEX_URL)
    fun uploadImage(@QueryMap()params: Map<String, String>, @Part()image: MultipartBody.Part) : Call<UploadResultBean>
}