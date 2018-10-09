package com.febers.uestc_bbs.dao

import android.content.Context
import com.febers.uestc_bbs.MyApplication
import com.febers.uestc_bbs.entity.PListResultBean
import com.febers.uestc_bbs.entity.UserPostBean
import com.google.gson.Gson

object PostStore {

    fun savePostList(fid: String, pList: PListResultBean) {
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

    fun getPostList(fid: String): PListResultBean {
        try {
            with(context().getSharedPreferences(fid, 0)) {
                val json: String = this.getString(fid, "")
                return Gson().fromJson(json, PListResultBean::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return PListResultBean()
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

    private fun context(): Context = MyApplication.context()
}