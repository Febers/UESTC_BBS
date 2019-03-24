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

    /**
     * @deprecated 占用内存
     */
    fun savePostListToSp(fid: String, pList: PostListBean) {
        context().getSharedPreferences(fid, 0).edit().apply {
            val json = Gson().toJson(pList)
            putString(fid, json)
            apply()
        }
    }

    /**
     * @deprecated 占用内存
     */
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

    /**
     * 保存帖子列表至手机内存，由于是使用了FileProvider，所以这里其实是不需要存储权限的
     *
     * @param fid 用作文件的名称
     * @param pList class类
     */
    fun savePostListToFile(fid: String, pList: PostListBean) {
        val fileWriter: FileWriter = FileWriter(FileHelper.appFileDir+"/$fid.txt")
        try {
            fileWriter.write(Gson().toJson(pList))
            fileWriter.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fileWriter.tryClose()
        }
    }

    /**
     * 从文件中还原帖子列表，实际上还原的是包含帖子列表的bean类
     *
     * @param fid 用于寻找文件
     * @return bean
     */
    fun getPostListByFile(fid: String): PostListBean {
        val fileReader: FileReader = FileReader(FileHelper.appFileDir+"/$fid.txt")
        try {
            val result = StringBuilder()
            val charArray: CharArray = CharArray(1)
            while (fileReader.read(charArray) != -1) {
                result.append(charArray)
            }
            if (result.isEmpty()) PostListBean()
            return Gson().fromJson(result.toString(), PostListBean::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fileReader.tryClose()
        }
        return PostListBean();
    }

    /**
     * 保存用户的收藏、发表的帖子列表，由于这几个页面服务器响应很快，没必要本地缓存
     * @deprecated 占用内存,没必要
     */
    fun saveUserPListToSp(fid: String, post: UserPostBean) {
        context().getSharedPreferences(fid, 0).edit().apply {
            val json = Gson().toJson(post)
            putString(fid, json)
            apply()
        }
    }

    /**
     * @deprecated 占用内存
     */
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