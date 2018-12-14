package com.febers.uestc_bbs.utils

import android.content.Context
import android.content.pm.PackageManager
import com.febers.uestc_bbs.MyApp
import android.content.Intent
import com.febers.uestc_bbs.R
import java.net.URISyntaxException


class DonateUtils(private val context: Context) {

    /**
     * 打开支付宝进行捐赠
     */
    fun donateByAlipay() {
        val url = "intent://platformapi/startapp?saId=10000007&" +
                "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com/a6x02751i2trlsrltv6ji64%3F_s" +
                "%3Dweb-other&_t=1472443966571#Intent;" +
                "scheme=alipayqr;package=com.eg.android.AlipayGphone;end"
        HintUtils.show(context.getString(R.string.thank_you))
        if (hasInstalledAlipayClient()) {
            try {
                val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                context.startActivity(intent)
            } catch (e: URISyntaxException) {
                e.printStackTrace()
                HintUtils.show(context.getString(R.string.error))
            }

        } else {
            HintUtils.show(context.getString(R.string.no_alipay_installed))
        }
    }


    /**
     * 判断支付宝客户端是否已安装，转账前检查
     * @return 支付宝客户端是否已安装
     */
    private fun hasInstalledAlipayClient(): Boolean {
        val ALIPAY_PACKAGE_NAME = "com.eg.android.AlipayGphone"
        return try {
            val info = MyApp.context().packageManager.getPackageInfo(ALIPAY_PACKAGE_NAME, 0)
            info != null
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }
    }
}