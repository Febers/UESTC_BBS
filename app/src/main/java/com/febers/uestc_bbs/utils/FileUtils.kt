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

    fun saveImage(bitmap: Bitmap): Uri? {
        val now: Calendar = GregorianCalendar()
        val simpleDate: SimpleDateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val fileName: String = simpleDate.format(now.time)
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) { //内存未挂载
            return null
        }
        try {
            val file: File = File(appImageDir)
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    i("Image", "no e")
                    throw IOException()
                }
            }
            val imgFile = File("$appImageDir$fileName.png")
            val fos: FileOutputStream = FileOutputStream(imgFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
            val uri = Uri.parse(imgFile.absolutePath)
            return uri
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
}