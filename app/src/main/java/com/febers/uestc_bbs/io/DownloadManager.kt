package com.febers.uestc_bbs.io

import androidx.annotation.NonNull
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class DownloadManager {

    /**
     * 如果下载的文件为apk，保存在appApkDir
     * 否则保存在appFileDir
     *
     * @param url 下载连接
     * @param listener 下载监听
     */
    fun download(url: String,
                 fileName: String,
                 filePath: String? = null,
                 listener: OnDownloadListener) {

        val request = Request.Builder().url(url).build()
        val okHttpClient = OkHttpClient()

        okHttpClient.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                listener.onDownloadFailed()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                var inputStream: InputStream? = null
                val buf = ByteArray(2048)
                var len = 0
                var outputStream: FileOutputStream? = null
                // 储存下载文件的目录
                var savePath =
                        if (fileName.endsWith("apk", true)) isExistDir(FileHelper.appApkDir)
                        else isExistDir(FileHelper.appFileDir)
                filePath?.apply {
                    savePath = this
                }
                try {
                    inputStream = response.body()!!.byteStream()
                    val total = response.body()!!.contentLength()
                    val file = File(savePath, fileName)
                    if (file.exists()) {
                        file.delete()
                    }
                    outputStream = FileOutputStream(file)
                    var sum: Long = 0

                    do {
                        len = inputStream.read(buf)
                        if (len == -1) break //最后跳出
                        outputStream.write(buf, 0, len)
                        sum += len.toLong()
                        val progress = (sum * 1.0f / total * 100).toInt()
                        listener.onDownloading(progress)
                    } while (len != -1)

                    outputStream.flush()
                    listener.onDownloadSuccess(file)
                } catch (e: Exception) {
                    listener.onDownloadFailed()
                } finally {
                    inputStream?.tryClose()
                    outputStream?.tryClose()
                }
            }
        })
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException
     *
     * 判断下载目录是否存在
     */
    @Throws(IOException::class)
    private fun isExistDir(saveDir: String): String {
        val downloadFile = File(saveDir)
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile()
        }
        return downloadFile.absolutePath
    }

    /**
     * @param url
     * @return
     * 从下载连接中解析出文件名
     */
    @NonNull
    private fun getNameFromUrl(url: String): String {
        return url.substring(url.lastIndexOf("/") + 1)
    }

    interface OnDownloadListener {
        fun onDownloadSuccess(file: File)
        fun onDownloading(progress: Int)
        fun onDownloadFailed()
    }
}
