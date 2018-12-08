package com.febers.uestc_bbs.io

import android.content.Context
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.entity.PostListBean
import com.febers.uestc_bbs.entity.UserPostBean
import com.google.gson.Gson
import java.io.*
import java.lang.StringBuilder


/**
 * 原先使用SharedPreference来保存超长string
 * 会导致极大的内存占用，做法极蠢
 * 改为使用文件保存
 */
object PostHelper {

    fun savePostListToSp(fid: String, pList: PostListBean) {
        context().getSharedPreferences(fid, 0).edit().apply {
            val json = Gson().toJson(pList)
            putString(fid, json)
            apply()
        }
    }

    fun savePostListToFile(fid: String, pList: PostListBean) {
        try {
            val fileWriter: FileWriter = FileWriter(FileHelper.appFileDir+"/$fid.txt")
            fileWriter.flush()
            fileWriter.write(Gson().toJson(pList))
            fileWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveUserPListToSp(fid: String, post: UserPostBean) {
        context().getSharedPreferences(fid, 0).edit().apply {
            val json = Gson().toJson(post)
            putString(fid, json)
            apply()
        }
    }

    fun getPostListBySp(fid: String): PostListBean {
        try {
            with(context().getSharedPreferences(fid, 0)) {
                val json: String = this.getString(fid, "")
                return Gson().fromJson(json, PostListBean::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return PostListBean()
        }
    }

    fun getPostListByFile(fid: String): PostListBean {
        return try {
            val result = StringBuilder()
            val fileReader: FileReader = FileReader(FileHelper.appFileDir+"/$fid.txt")
            val charArray: CharArray = CharArray(1)
            while (fileReader.read(charArray) != -1) {
                result.append(charArray)
            }
            Gson().fromJson(result.toString(), PostListBean::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            PostListBean()
        }
    }

    fun getUserPListBySp(fid: String): UserPostBean {
        try {
            with(context().getSharedPreferences(fid, 0)) {
                val json: String = this.getString(fid, "")
                return Gson().fromJson(json, UserPostBean::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return UserPostBean()
        }
    }

    private fun context(): Context = MyApp.context()
}