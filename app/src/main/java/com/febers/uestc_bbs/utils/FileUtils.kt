package com.febers.uestc_bbs.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log.i
import java.text.SimpleDateFormat
import java.util.*
import java.io.*
import java.lang.StringBuilder
import java.nio.charset.Charset


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

    private fun getImageFileName(): String {
        val now: Calendar = GregorianCalendar()
        val simpleDate: SimpleDateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        return simpleDate.format(now.time)
    }

    fun onImageViewDestroy() {
        if (fileForShare) {
            if (imgFile.exists()) {
                imgFile.delete()
            }
        }
    }

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
}