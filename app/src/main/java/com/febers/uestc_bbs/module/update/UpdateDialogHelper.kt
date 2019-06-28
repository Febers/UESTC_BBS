package com.febers.uestc_bbs.module.update

import android.content.Context
import com.febers.uestc_bbs.entity.GithubReleaseBean
import com.febers.uestc_bbs.io.FileHelper
import com.febers.uestc_bbs.module.service.NotificationHelper
import com.febers.uestc_bbs.utils.ApiUtils
import com.febers.uestc_bbs.view.custom.UpdateDialog
import org.jetbrains.anko.browse

class UpdateDialogHelper(private val context: Context) {

    fun showGithubUpdateDialog(githubReleaseBean: GithubReleaseBean) {
        val dialog = UpdateDialog(context,
                title = "新版本：${githubReleaseBean.tag_name}",
                time = "发布时间：${githubReleaseBean.published_at}",
                size = "大小：${FileHelper.getFormatSize(0.0+(githubReleaseBean.assets?.get(0)?.size ?: 0))}",
                body = """
更新说明：

${githubReleaseBean.body}
                """)
        dialog.setOnClickCenterButtonListener("网页下载（快）", object : UpdateDialog.ButtonClickListener {
            override fun onClick() {
                dialog.dismiss()
                context.browse(ApiUtils.COOLAPK_URL)
            }
        })
        dialog.setOnClickRightButtonListener("直接下载（慢）", object : UpdateDialog.ButtonClickListener {
            override fun onClick() {
                dialog.dismiss()
                NotificationHelper().showDownloadNotification(context = context,
                        channelId = "download",
                        notificationId = 9527, url = githubReleaseBean.assets?.get(0)?.browser_download_url+"",
                        fileName = githubReleaseBean.assets?.get(0)?.name+"")
            }
        })
        dialog.show()
    }
}