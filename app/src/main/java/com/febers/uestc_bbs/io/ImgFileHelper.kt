package com.febers.uestc_bbs.io

import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object ImgFileHelper {
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
}