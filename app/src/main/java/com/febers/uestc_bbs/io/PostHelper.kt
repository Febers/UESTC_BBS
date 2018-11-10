package com.febers.uestc_bbs.io

import android.content.Context
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.entity.PostListBean
import com.febers.uestc_bbs.entity.UserPostBean
import com.google.gson.Gson

object PostHelper {

    fun savePostList(fid: String, pList: PostListBean) {
        context().getSharedPreferences(fid, 0).edit().apply {
            val json = Gson().toJson(pList)
            putString(fid, json)
            apply()
        }
    }

    fun saveUserPList(fid: String, post: UserPostBean) {
        context().getSharedPreferences(fid, 0).edit().apply {
            val json = Gson().toJson(post)
            putString(fid, json)
            apply()
        }
    }

    fun getPostList(fid: String): PostListBean {
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

    fun getUserPList(fid: String): UserPostBean {
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