package com.febers.uestc_bbs.module.update.coolapk

import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.UpdateCheckBean

class CoolApkHtmlResolver {

    /**
     * 通过解析酷安网页的方式检查是否存在新版本
     * 版本元素：
     *
     * <div class="apk_topba_appinfo">
     * <div class="apk_topbar_mss">
     * <p class="detail_app_title">i河畔<span
     * class="list_app_info">  1.1.4</span></p>
     * <p class="apk_topba_message">
     * 6.91M / 44下载 / 2人关注 /
     * ...
     *
     * 下载地址位于js代码中
     *
     * <script type="text/javascript">
     * function onDownloadApk($downloadId) {
     * if ($downloadId) {
     * window.location.href = "https://dl.coolapk.com/down?pn=com.febers.uestc_bbs&id=MjA5MTc2&h=d5d1e219pt8enx&from=click";
     * } else {
     * window.location.href = 'https://dl.coolapk.com/down?pn=com.coolapk.market&id=NDU5OQ&h=46bb9d98&from=from-web';
     * }
     *
     * @param source 网页源码
     */
    fun resolve(source: String): UpdateCheckBean? {
        val versionStart = source.indexOf("list_app_info") + "list_app_info\">".length
        val versionEnd = source.indexOf("span", versionStart)
        val versionRaw = source.substring(versionStart, versionEnd)
        val nowVersion = MyApp.context().resources.getString(R.string.version_value)

        return null
    }
}