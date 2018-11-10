package com.febers.uestc_bbs.module.setting

import android.view.KeyEvent
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import android.app.Dialog
import com.febers.uestc_bbs.entity.UpdateBean
import com.febers.uestc_bbs.io.FileHelper
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.UpgradeInfo


class UpdateActivity: BaseActivity() {

    private var updateDialog: Dialog? = null

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

        }

        updateDialog?.show()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}