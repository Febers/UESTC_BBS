package com.febers.uestc_bbs.module.service

import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.annotation.RequiresApi
import com.febers.uestc_bbs.base.NetworkChangeEvent
import com.febers.uestc_bbs.utils.logd
import com.febers.uestc_bbs.utils.postEvent

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class NetworkCallbackImpl: ConnectivityManager.NetworkCallback() {
    /**
     * 当网络发生变化时，回调该方法
     */
    override fun onLost(network: Network) {
        super.onLost(network)
        logd { "网络lost，即将发送网络变化事件" }
        postEvent(NetworkChangeEvent(true))
    }
}