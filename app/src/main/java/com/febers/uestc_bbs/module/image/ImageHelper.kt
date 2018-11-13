package com.febers.uestc_bbs.module.image

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.febers.uestc_bbs.io.FileHelper.appImageDir
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object ImageHelper {

    private var fileForShare: Boolean = false
    private var imgFile: File? = null

    /**
     * 保存image
     *
     * @param bitmap
     */
    fun saveImage(context: Context, bitmap: Bitmap, forShare: Boolean): Uri? {
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

            val uri = Uri.parse(imgFile!!.absolutePath)
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
            return uri
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * 保存gif格式的图片
     */
    fun saveGif(context: Context, gifByte: ByteArray?, forShare: Boolean): Uri? {
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

            val uri = Uri.parse(imgFile!!.absolutePath)
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
            return uri
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
     * 保存图片之后插入到系统相册并且通知图库更新
     *
     */
    private fun insertImageToGallery(context: Context, file: File) {
        try {
            MediaStore.Images.Media.insertImage(context.contentResolver,
                    file.absolutePath, file.name, null)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.absolutePath)))
    }

    /**
     * 在调用saveImage的函数的视图销毁之后，调用此方法
     * 删除图片文件
     */
    fun onImageViewDestroy() {
        imgFile ?: return
        if (fileForShare) {
            if (imgFile!!.exists()) {
                imgFile!!.delete()
            }
        }
    }
}