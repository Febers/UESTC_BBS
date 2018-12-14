package com.febers.uestc_bbs.base

import androidx.annotation.UiThread

/**
 * View公共接口
 *
 */
interface BaseView {

    @UiThread
    fun showHint(msg: String)

    @UiThread
    fun showError(msg: String){}
}

/*
 *
 *                   .-~~~~~~~~~-._       _.-~~~~~~~~~-.
 *             __.'              ~.   .~              `.__
 *           .'//                  \./                  \\`.
 *         .'//                     |                     \\`.
 *       .'// .-~"""""""~~~~-._     |     _,-~~~~"""""""~-. \\`.
 *     .'//.-"                 `-.  |  .-'                 "-.\\`.
 *   .'//______.============-..   \ | /   ..-============.______\\`.
 * .'______________________________\|/______________________________`.
 *
 */