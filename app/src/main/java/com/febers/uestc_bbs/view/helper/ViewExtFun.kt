package com.febers.uestc_bbs.view.helper

import com.scwang.smartrefresh.layout.SmartRefreshLayout

fun SmartRefreshLayout.successFinish() {
    finishRefresh(true)
    finishLoadMore(true)
    setEnableLoadMore(true)
}

fun SmartRefreshLayout.failFinish() {
    finishRefresh(false)
    finishLoadMore(false)
}