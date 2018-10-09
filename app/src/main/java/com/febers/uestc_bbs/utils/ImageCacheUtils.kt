package com.febers.uestc_bbs.utils

import android.os.Looper

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.febers.uestc_bbs.MyApplication

import java.io.File
import java.math.BigDecimal

object ImageCacheUtils {

    private val imageCacheDir = MyApplication.context().cacheDir.toString() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR

    // 获取Glide磁盘缓存大小
    val cacheSize: String
        get() {
            return try {
                getFormatSize(getFolderSize(File(imageCacheDir)).toDouble())
            } catch (e: Exception) {
                e.printStackTrace()
                "获取失败"
            }
        }

    // 清除Glide磁盘缓存，自己获取缓存文件夹并删除方法
    fun clearCache(): Boolean {
        return deleteFolderFile(imageCacheDir, true)
    }

    // 清除图片磁盘缓存，调用Glide自带方法
    fun clearCacheSelf(): Boolean {
        return try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Thread(Runnable {
                    Glide.get(MyApplication.context()).clearDiskCache()
                }).start()
            } else {
                Glide.get(MyApplication.context()).clearDiskCache()
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    // 清除Glide内存缓存
    fun clearCacheMemory(): Boolean {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(MyApplication.context()).clearMemory()
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    // 获取指定文件夹内所有文件大小的和
    private fun getFolderSize(file: File): Long {
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

    // 按目录删除文件夹文件方法
    private fun deleteFolderFile(filePath: String, deleteThisPath: Boolean): Boolean {
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

    // 格式化单位
    private fun getFormatSize(size: Double): String {
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

}