package com.febers.uestc_bbs.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log.i
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.module.more.ImageActivity
import com.febers.uestc_bbs.module.message.view.PMDetailActivity
import com.febers.uestc_bbs.module.more.WebActivity
import com.febers.uestc_bbs.module.post.view.PostDetailActivity
import com.febers.uestc_bbs.module.post.view.edit.PostEditActivity
import com.febers.uestc_bbs.module.user.view.UserDetailActivity
import org.greenrobot.eventbus.EventBus

object ViewClickUtils {

    fun linkClick(url: String, context: Context) {
        i("Link Change", url)
        if (url.endsWith(".gif")) {
            return
        }
        if (url.contains("mailto")) {
//            val intent = Intent(Intent.ACTION_SENDTO)
//            intent.data = Uri.parse(url.removeRange(0,  url.lastIndexOf("mailto")))
//            context.startActivity(intent)
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

    fun clickToImageViewerByUid(uid: Int?, context: Context?) {
        uid ?: return
        context ?: return
        clickToImageViewer(url = "http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=$uid&size=big", context = context)
    }

    fun clickToImageViewer(url: String?, context: Context?) {
        url ?: return
        context ?: return
        context.startActivity(Intent(context, ImageActivity::class.java).apply {
            putExtra(IMAGE_URL, url)
        })
    }


    fun clickToUserDetail(context: Context?, uid: Int?) {
        context ?: return
        uid ?: return
        context.startActivity(Intent(context, UserDetailActivity::class.java).apply {
            putExtra(USER_IT_SELF, false)
            putExtra(USER_ID, uid)
        })
    }

    fun clickToPostDetail(context: Context?, fid: Int?) {
        context ?: return
        fid ?: return
        context.startActivity(Intent(context, PostDetailActivity::class.java).apply {
            putExtra(FID, fid)
        })
    }

    fun clickToPrivateMsg(context: Context?, uid: Int?, userName: String?) {
        context ?: return
        uid ?: return
        userName ?: return
        context.startActivity(Intent(context, PMDetailActivity::class.java).apply {
            putExtra(USER_ID, uid)
            putExtra(USER_NAME, userName)
        })
    }

    fun clickToPostEdit(context: Context?, fid: Int?) {
        context ?:return
        fid ?: return
        context.startActivity(Intent(context, PostEditActivity::class.java).apply {
            putExtra(FID, fid)
        })
    }

    fun clickToAppWeb(context: Context?, url: String?) {
        context ?: return
        url ?: return
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
//        context.startActivity(Intent(context, WebActivity::class.java).apply {
//            putExtra(URL, url)
//        })

    }
}