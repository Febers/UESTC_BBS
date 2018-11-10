package com.febers.uestc_bbs.io

import android.os.Looper

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.febers.uestc_bbs.MyApp

import java.io.File
import java.math.BigDecimal

/**
 * 主要是提供清除图片缓存功能
 */
object CacheHelper {

    private val imageCacheDir = MyApp.context().cacheDir.toString() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR

    // 获取Glide磁盘缓存大小
    val imageCacheSize: String
        get() {
            return try {
                FileHelper.getFormatSize(getFolderSize(File(imageCacheDir)).toDouble())
            } catch (e: Exception) {
                e.printStackTrace()
                "获取失败"
            }
        }

    // 清除Glide磁盘缓存，自己获取缓存文件夹并删除方法
    fun clearCache(): Boolean {
        return FileHelper.deleteFolderFile(imageCacheDir, true)
    }

    // 清除图片磁盘缓存，调用Glide自带方法
    fun clearCacheSelf(): Boolean {
        return try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Thread(Runnable {
                    Glide.get(MyApp.context()).clearDiskCache()
                }).start()
            } else {
                Glide.get(MyApp.context()).clearDiskCache()
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
                Glide.get(MyApp.context()).clearMemory()
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





}