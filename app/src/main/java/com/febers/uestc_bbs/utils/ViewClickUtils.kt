package com.febers.uestc_bbs.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.FragmentActivity
import android.util.Log.i
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.module.message.view.PMDetailActivity
import com.febers.uestc_bbs.module.post.view.PostDetailActivity
import com.febers.uestc_bbs.module.user.view.UserDetailActivity
import org.greenrobot.eventbus.EventBus

object ViewClickUtils {

    fun linkClick(url: String, context: Context) {
        if (url.endsWith(".gif")) {
            return
        }
        if (url.contains("mailto")) {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse(url.removeRange(0,  url.lastIndexOf("mailto")))
            context.startActivity(intent)
            return
        }
        if (url.contains("http://bbs.stuhome.net/forum.php?mod=viewthread&tid")) {
                i("Link Change", url.removeRange(0, url.lastIndexOf("tid=")+4))
                EventBus.getDefault().post(PostEvent(BaseCode.SUCCESS, url.removeRange(0, url.lastIndexOf("tid=")+4)))
                return
        }
        val uri = Uri.parse(url)
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    fun clickToImgBig(url: String?, context: Context?) {
        url ?: return
        context ?: return
        i("IMG", url)
    }

    fun clickToUserDetail(context: Context?, activity: FragmentActivity?, uid: String?) {
        context ?: return
        activity ?: return
        uid ?: return
        context.startActivity(Intent(activity, UserDetailActivity::class.java).apply {
            putExtra(USER_IT_SELF, false)
            putExtra(USER_ID, uid)
        })
    }

    fun clickToUserDetail(activity: FragmentActivity?, uid: String?) {
        activity ?: return
        uid ?: return
        activity.startActivity(Intent(activity, UserDetailActivity::class.java).apply {
            putExtra(USER_IT_SELF, false)
            putExtra(USER_ID, uid)
        })
    }

    fun clickToUserDetail(activity: Activity?, uid: String?) {
        activity ?: return
        uid ?: return
        activity.startActivity(Intent(activity, UserDetailActivity::class.java).apply {
            putExtra(USER_IT_SELF, false)
            putExtra(USER_ID, uid)
        })
    }

    fun clickToPostDetail(context: Context?, activity: FragmentActivity?, fid: String?) {
        context ?: return
        activity ?: return
        fid ?: return
        context.startActivity(Intent(activity, PostDetailActivity::class.java).apply {
            putExtra(FID, fid)
        })
    }

    fun clickToPM(context: Context?, activity: FragmentActivity?, uid: String?, userName: String?) {
        context ?: return
        activity ?: return
        uid ?: return
        userName ?: return
        context.startActivity(Intent(activity, PMDetailActivity::class.java).apply {
            putExtra(USER_ID, uid)
            putExtra(USER_NAME, userName)
        })
    }

    fun clickToPM(activity: Activity?, uid: String?, userName: String?) {
        activity ?: return
        uid ?: return
        userName ?: return
        activity.startActivity(Intent(activity, PMDetailActivity::class.java).apply {
            putExtra(USER_ID, uid)
            putExtra(USER_NAME, userName)
        })
    }
}