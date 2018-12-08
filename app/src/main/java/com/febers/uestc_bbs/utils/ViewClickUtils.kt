package com.febers.uestc_bbs.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import android.view.View
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.module.login.model.LoginContext
import com.febers.uestc_bbs.module.image.ImageViewer
import com.febers.uestc_bbs.module.message.view.PMDetailActivity
import com.febers.uestc_bbs.module.post.view.PostDetailActivity
import com.febers.uestc_bbs.module.post.view.edit.*
import com.febers.uestc_bbs.module.user.view.UserDetailActivity
import org.jetbrains.anko.browse

object ViewClickUtils {

    /**
     * TODO 点击站内链接时自动跳转，无需打开浏览器
     */
    fun linkClick(url: String,
                  context: Context) {
//        i("Link ", url)
        if (url.endsWith(".gif")) {
            return
        }
        context.browse(url, true)
    }

    /**
     * 通过uid查看头像大图
     *
     * @param uid 用户id
     * @param context
     */
    fun clickToViewAvatarByUid(uid: Int?,
                               context: Context?) {
        uid ?: return
        context ?: return
        clickToImageViewer(url = "http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=$uid&size=big", context = context)
    }

    /**
     * 打开ImageViewer，目前调用一个Activity进行展示，待改进
     * @param url
     * @param context
     * @param transitionView 进行页面共享的view
     * @param transitionViewName 进行页面共享的view的name
     *
     * 页面共享的文章见:https://blog.csdn.net/sinat_31057219/article/details/78095038
     * 目前没有进行相应的调用
     */
    fun clickToImageViewer(url: String?,
                           context: Context?,
                           transitionView: View? = null,
                           transitionViewName: String? = null) {
        url ?: return
        context ?: return
        var bundle: Bundle? = null
        if (transitionView != null && transitionViewName != null) {
            if (context is Activity)
                bundle = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(context, transitionView, transitionViewName).toBundle()
            context.startActivity(Intent(context, ImageViewer::class.java).apply {
                putExtra(IMAGE_URL, url)
            }, bundle)
            (context as Activity).overridePendingTransition(0, 0)
        } else {
            context.startActivity(Intent(context, ImageViewer::class.java).apply {
                putExtra(IMAGE_URL, url)
            })
        }
    }

    /**
     * 查看用户详情
     *
     * @param context
     * @param uid 用户id
     */
    fun clickToUserDetail(context: Context?,
                          uid: Int?) {
        context ?: return
        if (uid == null) return
        if (!LoginContext.userState(context)) return
        context.startActivity(Intent(context, UserDetailActivity::class.java).apply {
            putExtra(USER_IT_SELF, false)
            putExtra(USER_ID, uid)
        })

    }

    /**
     * 查看帖子详情
     *
     * @param context
     * @param fid 帖子id
     */
    fun clickToPostDetail(context: Context?,
                          fid: Int?) {
        context ?: return
        if (fid == 0 || fid == null) return
        if (!LoginContext.userState(context)) return
        context.startActivity(Intent(context, PostDetailActivity::class.java).apply {
            putExtra(FID, fid)
        })
    }

    /**
     * 查看私信内容
     *
     * @param context
     * @param uid
     * @param userName
     */
    fun clickToPrivateMsg(context: Context?,
                          uid: Int?,
                          userName: String?) {
        context ?: return
        userName ?: return
        if (uid == 0 ||uid == null) return
        if (!LoginContext.userState(context)) return
        context.startActivity(Intent(context, PMDetailActivity::class.java).apply {
            putExtra(USER_ID, uid)
            putExtra(USER_NAME, userName)
        })
    }

    /**
     * 发帖
     * @param context
     * @param fid 板块id
     * @param title 板块标题
     */
    fun clickToPostEdit(context: Context?,
                        fid: Int,
                        title: String) {
        context ?: return
        if (!LoginContext.userState(context)) return
        context.startActivity(Intent(context, PostEditActivity::class.java).apply {
            putExtra(FID, fid)
            putExtra(TITLE, title)
        })
    }

    /**
     * 点击回复之后打开回复的Activity
     *一般由帖子详情界面调用或者帖子回复消息界面调用
     *
     * @param context
     * @param toUserId 所要回复的用户的id
     * @param toUserName
     * @param replyId
     * @param isQuota 是否引用回复
     * @param replySimpleDescription 需要回复的内容的简略说明，
     * 如果回复主贴，则提取主贴的一部分字符串(纯图片帖子的话, 即为“[图片]”)
     * 如果回复的是回帖，那么回复的是原评论内容的简略
     */
    fun clickToPostReply(context: Activity?,
                         toUserId: Int,
                         toUserName: String?,
                         postId:Int,
                         replyId: Int,
                         isQuota: Boolean?,
                         replySimpleDescription: String) {
        context ?: return
        toUserName ?: return
        context.startActivityForResult(Intent(context, PostReplyActivity::class.java).apply {
            putExtra(USER_ID, toUserId)
            putExtra(USER_NAME, toUserName)
            putExtra(POST_ID, postId)
            putExtra(POST_REPLY_ID, replyId)
            putExtra(POST_REPLY_IS_QUOTA, isQuota)
            putExtra(POST_REPLY_DESCRIPTION, replySimpleDescription.getStringSimplified())
        }, POST_REPLY_RESULT_CODE)
    }

    /**
     * TODO 在App内打开web页面
     *
     */
    fun clickToAppWeb(context: Context?,
                      url: String?) {
        context ?: return
        url ?: return
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}