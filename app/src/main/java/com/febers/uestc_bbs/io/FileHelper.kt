package com.febers.uestc_bbs.io

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log.i
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.febers.uestc_bbs.MyApp
import java.io.*
import java.lang.StringBuilder
import java.math.BigDecimal
import android.content.Intent
import androidx.core.content.FileProvider
import android.os.Build
import java.nio.ByteBuffer

object FileHelper {

    /*
        目录为/storage/emulated/0/UESTC_BBS，主要用于用户保存的图片
     */
    val appImageDir: String = Environment.getExternalStorageDirectory().absolutePath + "/UESTC_BBS/"

    /*
        不再单独使用一个外部文件夹保存图片，直接保存图片到/storage/emulated/0/Android/data/com.febers.uestc_bbs/files/image/
        因为保存图片之后会将图片插入至系统相册，重复保存没有意义
     */
    val appImageDir2: String = MyApp.context().getExternalFilesDir("image").absolutePath
    /*
        目录为/storage/emulated/0/Android/data/com.febers.uestc_bbs/files/apk/，记住跟FileProvide的匹配
        主要用于下载更新的安装包之后利用FileProvider打开
     */
    val appApkDir: String = MyApp.context().getExternalFilesDir("apk").absolutePath

    /*
        保存其他的下载文件
     */
    val appFileDir: String = MyApp.context().getExternalFilesDir("other").absolutePath
    /*
        目录为/storage/emulated/0/Android/data/com.febers.uestc_bbs/cache
      */
    val appCacheDir: String = MyApp.context().externalCacheDir.absolutePath

    /*
        Glide的缓存目录
     */
    val glideCacheDir = MyApp.context().cacheDir.toString() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR

    /*
        与AndroidManifest中的authorities匹配
     */
    const val fileProviderAuthorities = "com.febers.uestc_bbs.fileProvider"

    /**
     * 获取Assets文件夹中文件的String值
     *
     * @param context
     * @param fileName 直接使用文件名(包含后缀)
     * @return string
     */
    fun getAssetsString(context: Context, fileName: String): String {
        val inputReader = InputStreamReader(
                context.assets.open(fileName))
        try {
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
        } finally {
            inputReader.tryClose()
        }
        return "error"
    }

    /**
     * 将Uri路径转换成文件
     * Android 4.2 及以上
     *
     * @param uri
     * @param context
     * @return file
     */
    fun getFileByUri(uri: Uri, context: Context): File? {
        var path: String? = null
        if ("content" == uri.scheme) {
            val projections = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(uri, projections, null, null, null)
            if (cursor!!.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                path = cursor.getString(columnIndex)
            }
            cursor.tryClose()
            return File(path)
        } else {
            i("Uri", uri.scheme)
        }
        return null
    }

    /**
     * 获取xml资源文件的Uri
     *
     * @param context
     * @param id
     * @return string
     */
    fun getUriByResourceId(context: Context, id: Int): String = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
            context.resources.getResourcePackageName(id) + "/" +
            context.resources.getResourceTypeName(id) + "/" +
            context.resources.getResourceEntryName(id)


    /**
     * 获取格式化单位
     *
     * @param size
     * @return string
     */
    fun getFormatSize(size: Double): String {

        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return size.toString() + "Byte"
        }

        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(java.lang.Double.toString(kiloByte))
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
        }

        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(java.lang.Double.toString(megaByte))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(java.lang.Double.toString(gigaByte))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }


    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file
     * @return size
     */
    fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            for (aFileList in fileList) {
                size += if (aFileList.isDirectory) {
                    getFolderSize(aFileList)
                } else {
                    aFileList.length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }


    /**
     * 按目录删除文件夹文件方法
     *
     * @param filePath 目录路径
     * @param deleteThisPath 为true，则在删除文件之后也会删除文件夹
     * @return boolean
     */
    fun deleteFolderFile(filePath: String, deleteThisPath: Boolean): Boolean {
        try {
            val file = File(filePath)
            if (file.isDirectory) {
                val files = file.listFiles()
                for (file1 in files) {
                    deleteFolderFile(file1.absolutePath, true)
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory) {
                    file.delete()
                } else {
                    if (file.listFiles().isEmpty()) {
                        file.delete()
                    }
                }
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * 调用系统安装器安装apk
     *
     * @param context 上下文
     * @param file apk文件
     */
    fun installApk(context: Context, file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, fileProviderAuthorities, file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            uri = Uri.fromFile(file)
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        context.startActivity(intent)
    }

    fun getByteArrayFromByteBuffer(byteBuffer: ByteBuffer): ByteArray {
        val bytesArray = ByteArray(byteBuffer.remaining())
        byteBuffer.get(bytesArray, 0, bytesArray.size)
        return bytesArray
    }
}

fun Closeable.tryClose() {
    try {
        this.close()
    } catch (e: IOException) {
    }
}