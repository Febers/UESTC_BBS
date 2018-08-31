package com.febers.uestc_bbs.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log.i
import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.PostEvent
import org.greenrobot.eventbus.EventBus

object LinkClickUtils {

    fun click(url: String, context: Context) {
        i("Link", url)
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
}