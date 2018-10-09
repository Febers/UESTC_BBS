package com.febers.uestc_bbs.utils

import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log.i
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object FileUtils {

    private val appDir = Environment.getExternalStorageDirectory().absolutePath + "/uestc_bbs/"
    private val appImageDir = appDir + "images/"
    private var fileForShare: Boolean = false
    private lateinit var imgFile: File

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
            imgFile = File("$appImageDir${getFileName()}.png")
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
            imgFile = File("$appImageDir${getFileName()}.gif")
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

    private fun getFileName(): String {
        val now: Calendar = GregorianCalendar()
        val simpleDate: SimpleDateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        return simpleDate.format(now.time)
    }

    fun viewDestroy() {
        i("File", "onD")
        if (fileForShare) {
            if (imgFile.exists()) {
                imgFile.delete()
            }
        }
    }
}