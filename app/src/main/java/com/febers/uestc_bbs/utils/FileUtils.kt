package com.febers.uestc_bbs.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log.i
import java.text.SimpleDateFormat
import java.util.*
import java.io.*
import java.lang.StringBuilder


object FileUtils {

    private val appDir = Environment.getExternalStorageDirectory().absolutePath + "/uestc_bbs/"
    private val appImageDir = appDir + "images/"
    private var fileForShare: Boolean = false
    private lateinit var imgFile: File

    /**
     * 保存image
     * @param bitmap
     * @param forShare 是否作为分享使用，如果是，视图销毁之后会删除
     */
    fun saveImage(bitmap: Bitmap, forShare: Boolean): Uri? {
        fileForShare = forShare
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) { //内存未挂载
            return null
        }
        try {
            val dir: File = File(appImageDir)
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    throw IOException()
                }
            }
            imgFile = File("$appImageDir${getImageFileName()}.png")
            val fos: FileOutputStream = FileOutputStream(imgFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
            return Uri.parse(imgFile.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * 保存gif格式的图片
     */
    fun saveGif(gifByte: ByteArray?, forShare: Boolean): Uri? {
        fileForShare = forShare
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) { //内存未挂载
            return null
        }
        if (gifByte == null) {
            return null
        }
        try {
            val dir: File = File(appImageDir)
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    throw IOException()
                }
            }
            imgFile = File("$appImageDir${getImageFileName()}.gif")
            val fos: FileOutputStream = FileOutputStream(imgFile)
            fos.write(gifByte, 0, gifByte.size)
            fos.flush()
            fos.close()
            return Uri.parse(imgFile.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * 给图片命名，方式简单粗暴，直接用时间字符串
     */
    private fun getImageFileName(): String {
        val now: Calendar = GregorianCalendar()
        val simpleDate: SimpleDateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        return simpleDate.format(now.time)
    }

    /**
     * 在调用saveImage的函数的视图销毁之后，调用此方法
     * 删除用于分享的暂时保存的图片
     */
    fun onImageViewDestroy() {
        if (fileForShare) {
            if (imgFile.exists()) {
                imgFile.delete()
            }
        }
    }

    /**
     * 获取Assets文件夹中文件的String值
     * 原先在其中放置了开源相关的html文件用于展示
     * 后来由于效果不好，就采用了RecyclerView
     * @param context
     * @param fileName 直接使用文件名(包含后缀)
     */
    fun getAssetsString(context: Context, fileName: String): String {
        try {
            val inputReader = InputStreamReader(
                    context.assets.open(fileName))
            val bufReader = BufferedReader(inputReader)
            val result = StringBuilder()
            var line: String? = null
            do {
                line?.let {
                    result.append(it)
                }
                line = bufReader.readLine()
            } while (line != null)
            return result.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "error"
    }

    fun uriToFile(uri: Uri, context: Context): File? {
        var path: String? = null
        if ("file" == uri.scheme) {
            path = uri.encodedPath
            if (path != null) {
                path = Uri.decode(path)
                val cr = context.contentResolver
                val buff = StringBuffer()
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'$path'").append(")")
                val cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        arrayOf(MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA),
                        buff.toString(), null, null)
                var index = 0
                var dataIdx = 0
                cur!!.moveToFirst()
                while (!cur.isAfterLast) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID)
                    index = cur.getInt(index)
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    path = cur.getString(dataIdx)
                    cur.moveToNext()
                }
                cur.close()
                if (index == 0) {

                } else {
                    val u = Uri.parse("content://media/external/images/media/$index")
                    println("temp uri is :$u")
                }
            }
            if (path != null) {
                return File(path)
            }
        } else if ("content" == uri.scheme) {
            // 4.2.2以后
            val projections = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(uri, projections, null, null, null)
            if (cursor!!.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                path = cursor.getString(columnIndex)
            }
            cursor.close()
            return File(path)
        } else {
            //i("Uri", uri.scheme)
        }
        return null
    }

}