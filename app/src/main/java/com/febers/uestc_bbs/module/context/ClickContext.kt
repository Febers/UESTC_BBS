package com.febers.uestc_bbs.module.context

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import android.view.View
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.module.image.ImageActivity
import com.febers.uestc_bbs.module.webview.BrowserActivity
import com.febers.uestc_bbs.module.image.ImageDialog
import com.febers.uestc_bbs.module.message.view.PMDetailActivity
import com.febers.uestc_bbs.module.more.DebugActivity
import com.febers.uestc_bbs.module.post.view.PostDetailActivity2
import com.febers.uestc_bbs.module.post.view.edit.*
import com.febers.uestc_bbs.module.user.view.UserDetailActivity
import com.febers.uestc_bbs.utils.getStringSimplified
import com.febers.uestc_bbs.utils.log
import org.jetbrains.anko.browse

object ClickContext {

    /**
     * 根据url，判断用户是否在打开一个帖子界面或者河畔用户界面
     * 帖子的url形式:
     * http://bbs.uestc.edu.cn/forum.php?mod=viewthread&tid=1661409
     * http://bbs.uestc.edu.cn/forum.php?mod=viewthread&tid=1456557&page=1#pid25591902
     *
     * 用户的url(第三个暂时不解析，不仅少见，而且需要通过name获取id)：
     * http://bbs.uestc.edu.cn/home.php?mod=space&uid=110170
     * http://bbs.stuhome.net/u.php?action=show&uid=6359
     * http://bbs.uestc.edu.cn/home.php?mod=space&username=cq365423762
     *
     * 板块的url：
     * http://bbs.uestc.edu.cn/forum.php?mod=forumdisplay&fid=174
     */
    fun linkClick(url: String,
                  context: Context) {
        log("Link ", url)
        if (url.endsWith(".gif")) {
            return
        }
        //如果是外链，直接通过浏览器打开，无需浪费时间判断
        if (url.contains("bbs.uestc.edu.cn") || url.contains("bbs.stuhome.net")) {
            clickInApp(url, context)
        } else {
            //context.browse(url, true)
            clickToAppWeb(context, url)
        }
    }

    private fun clickInApp(url: String,
                           context: Context)  {
        try {
            //以下为帖子
            if (url.contains("mod=viewthread&tid=")) {
                val start = url.indexOf("mod=viewthread&tid=") + "mod=viewthread&tid=".length
                val end = url.indexOf("&page", start)
                val postId =
                        if (end == -1) {
                            url.substring(start, url.length)
                        } else {
                            url.substring(start, end+1)
                        }
                clickToPostDetail(context, postId.toInt(), "")
                return
            }
            //以下为用户
            if (url.contains("mod=space&uid=")) {
                val start = url.indexOf("mod=space&uid=") + "mod=space&uid=".length
                val tid = url.substring(start, url.length)
                clickToUserDetail(context, tid.toInt())
                return
            }
            if (url.contains("action=show&uid=")) {
                val start = url.indexOf("action=show&uid=") + "action=show&uid=".length
                val tid = url.substring(start, url.length)
                clickToUserDetail(context, tid.toInt())
                return
            }
            //以下为板块,TODO
//        if (url.contains("forum.php?mod=forumdisplay&fid=")) {
//            val start = url.indexOf("forum.php?mod=forumdisplay&fid=")
//            val fid = url.substring(start, url.length)
//            return
//        }
            clickToAppWeb(context, url)
        } catch (e: Exception) {
            e.printStackTrace()
            clickToAppWeb(context, url)
        }
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
        val url = "http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=$uid&size=big"
        clickToImageViewer(url = url, urls = arrayOf(url), context = context)
    }

    /**
     * 打开ImageViewer
     * @param url
     * @param context
     *
     * 全屏 Dialog 文章： https://juejin.im/post/58de0a9a44d904006d04cead
     * 页面共享的文章见:https://blog.csdn.net/sinat_31057219/article/details/78095038
     * 目前没有进行相应的调用
     */
    fun clickToImageViewer(url: String?,
                           urls: Array<String>,
                           context: Context?) {
        url ?: return
        context ?: return
//        val bundle = Bundle()
//        bundle.putString(IMAGE_URL, url)
//        val imageViewer = ImageDialog()
//        imageViewer.arguments = bundle
//        try {
//            imageViewer.show((context as BaseActivity).supportFragmentManager, "image")
//        } catch (e: Exception) {
//            log(e.toString())
//        }
        context.startActivity(Intent(context, ImageActivity::class.java).apply {
            putExtra(IMAGE_URLS, urls)
            putExtra(IMAGE_URL, url)
        })
        (context as? Activity)?.overridePendingTransition(0, 0)
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
     * @param postTitle 帖子标题
     * @param userName 楼主名称
     */
    fun clickToPostDetail(context: Context?,
                          fid: Int?,
                          postTitle: String? = "",
                          userName: String? = "") {
        context ?: return
        if (fid == 0 || fid == null) return
        if (!LoginContext.userState(context)) return
        context.startActivity(Intent(context, PostDetailActivity2::class.java).apply {
            putExtra(FID, fid)
            putExtra(POST_TITLE, postTitle)
            putExtra(USER_NAME, userName)
        })
    }

    fun clickToPostDetail(context: Context?,
                          fid: Int?,
                          postTitle: String?,
                          transitionView: View? = null,
                          transitionViewName: String? = null) {
        context ?: return
        postTitle ?: return
        if (fid == 0 || fid == null) return
        if (!LoginContext.userState(context)) return
        var bundle: Bundle? = null
        if (transitionView != null && transitionViewName != null) {
            if (context is Activity)
                bundle = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(context, transitionView, transitionViewName).toBundle()
            context.startActivity(Intent(context, PostDetailActivity2::class.java).apply {
                putExtra(FID, fid)
                putExtra(POST_TITLE, postTitle)
            }, bundle)
        }
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
     *
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
     *
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
     * 在应用内打开网页视图
     *
     * @param context
     * @param url
     */
    fun clickToAppWeb(context: Context?,
                      url: String?) {
        context ?: return
        url ?: return
        context.startActivity(Intent(context, BrowserActivity::class.java).apply {
            putExtra(URL, url)
        })
    }

    fun clickToDebug(context: Context?, content: String) {
        context ?: return
        context.startActivity(Intent(context, DebugActivity::class.java).apply {
            putExtra("debug", content)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }
}