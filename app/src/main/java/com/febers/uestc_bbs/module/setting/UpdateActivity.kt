package com.febers.uestc_bbs.module.setting

import android.view.KeyEvent
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.febers.uestc_bbs.entity.UpdateBean
import com.febers.uestc_bbs.io.DownloadHelper
import com.febers.uestc_bbs.io.FileHelper
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.UpgradeInfo
import java.io.File


class UpdateActivity: BaseActivity() {

    private var updateDialog: Dialog? = null
    private var btnEnter: Button? = null

    override fun enableThemeHelper(): Boolean = false

    override fun setView(): Int = R.layout.activity_update

    override fun initView() {
        var upgradeInfo: UpgradeInfo? = null
        //服务器不稳定时，无法检测到更新，重复一次
        for (i in 0..1) {
            upgradeInfo = Beta.getUpgradeInfo()
            if (upgradeInfo != null) {
                break
            }
        }
        upgradeInfo ?: return

        val update = UpdateBean()
        update.body = upgradeInfo.newFeature
        update.size = FileHelper.getFormatSize(upgradeInfo.fileSize.toDouble())
        update.versionName = upgradeInfo.versionName
        update.downloadUrl = upgradeInfo.apkUrl

        if (updateDialog == null) {
            updateDialog = AlertDialog.Builder(this@UpdateActivity).create()
        }

        updateDialog?.show()
        updateDialog?.setCanceledOnTouchOutside(false)
        updateDialog?.setContentView(getDialogView())
        btnEnter?.setOnClickListener {
            DownloadHelper().download(url = update.downloadUrl!!, fileName = "UESTC_BBS${update.versionName}.apk",
                    listener = object : DownloadHelper.OnDownloadListener {
                        override fun onDownloadSuccess(file: File) {
                            btnEnter?.text = "安装"
                            btnEnter?.setOnClickListener {
                                FileHelper.installApk(this@UpdateActivity, file)
                            }
                        }

                        override fun onDownloading(progress: Int) {
                            runOnUiThread{
                                btnEnter?.text ="$progress%"
                            }
                        }

                        override fun onDownloadFailed() {
                            btnEnter?.text = "下载失败"
                        }
                    })
        }
    }

    private fun getDialogView(): View {
        val view = LayoutInflater.from(this@UpdateActivity).inflate(R.layout.dialog_update_app, null)
        val btnCancel = view.findViewById<Button>(R.id.btn_update_cancel)
        btnCancel.setOnClickListener { finish() }
        btnEnter = view.findViewById(R.id.btn_update_enter)
        return view
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}