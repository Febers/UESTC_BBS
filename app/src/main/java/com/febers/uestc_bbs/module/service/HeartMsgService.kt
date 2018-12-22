package com.febers.uestc_bbs.module.service

import android.app.*
import android.content.Intent
import android.os.IBinder
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.MsgHeartBean
import com.febers.uestc_bbs.home.HomeActivity
import com.febers.uestc_bbs.http.TokenClient
import com.febers.uestc_bbs.utils.ApiUtils
import com.febers.uestc_bbs.utils.TimeUtils
import com.febers.uestc_bbs.utils.log
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * 后台轮询消息的Service
 * 当前代码和功能过于冗杂
 * 有空重构：将显示Notification、
 * 网络线程以及其他， 划分出去
 */
class HeartMsgService : Service() {

    private var notificationHelper: NotificationHelper? = null
    private var msgThread: Thread? = null
    private var flag: Boolean = true

    private val pNotificationId = 970418; private val rNotificationId = 970419
    private val aNotificationId = 970420; private val sNotificationId = 970421
    private val privateChannelId = "pci"; private val privateChannelName = "pcn"
    private val otherChannelId = "other"; private val otherChannelName = "other"
    private val replyChannelId = "rci"; private val replyChannelName = "rcn"
    private val atChannelId = "aci"; private val atChannelName = "acn"
    private val systemChannelId = "sci"; private val systemChannelName = "scn"
    private var pmCount = 0; private var rmCount = 0
    private var amCount = 0; private var smCount = 0

    /*
     * 对于Service来说，只有第一次start会调用onCreate
     * 但是每次都会调用onStartCommand
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!MyApp.getUser().valid) return super.onStartCommand(intent, flags, startId)

        //注意Android8上，当应用运行在后台时，启动服务，会报IllegalStateException
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && MyApp.uiHidden) {
//            startForeground(Int.MAX_VALUE, showNotification(title = getString(R.string.receiving_message_backround), content = "", msgType = "", count = 0))
//        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        flag = true
        msgThread = HeartMsgThread()
        msgThread?.start()
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * 初始化通知栏工具
     */
    private fun showMsgNotification(title: String, content: String, notificationId: Int, msgType: String, count: Int, uid: Int = -1) {
        val channelId: String =  when(msgType) {
            MSG_TYPE_REPLY -> replyChannelId
            MSG_TYPE_PRIVATE -> privateChannelId
            MSG_TYPE_AT -> atChannelId
            MSG_TYPE_SYSTEM -> systemChannelId
            else -> otherChannelId
        }
        val channelName: String = when(msgType) {
            MSG_TYPE_REPLY -> replyChannelName
            MSG_TYPE_PRIVATE -> privateChannelName
            MSG_TYPE_AT -> atChannelName
            MSG_TYPE_SYSTEM -> systemChannelName
            else -> otherChannelName
        }
        val intents = arrayOf(Intent(this, HomeActivity::class.java).apply {
                    putExtra(USER_ID, uid.toString())
                    putExtra(MSG_TYPE, msgType)
                    putExtra(MSG_COUNT, count)
                    flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                })
        EventBus.getDefault().post(MsgEvent(BaseCode.SUCCESS, count, msgType))
        notificationHelper = NotificationHelper()
        notificationHelper!!.show(context = this,
                title = title,
                content = content,
                channelId = channelId,
                channelName = channelName,
                notificationId = notificationId,
                intents = intents)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMsgFeedback(event: MsgFeedbackEvent) {
//        i("service", "cancel: ${event.type}")
        log("service and type ${event.type}")
        when(event.type) {
            MSG_TYPE_REPLY -> {
                rmCount = 0
                notificationHelper?.cancelNotification(rNotificationId)
            }
            MSG_TYPE_PRIVATE -> {
                pmCount = 0
                notificationHelper?.cancelNotification(pNotificationId)
            }
            MSG_TYPE_AT -> {
                amCount = 0
                notificationHelper?.cancelNotification(aNotificationId)
                aNotificationId
            }
            MSG_TYPE_SYSTEM -> {
                smCount = 0
                notificationHelper?.cancelNotification(sNotificationId)
            }
        }
    }

    /*
       实时获取未读通知的线程类
     */
    inner class HeartMsgThread: Thread() {
        override fun run() {
            while (flag) {
                getHeartMsg()
                try {
                    sleep(6*1000)
                } catch (e: InterruptedException) {
                }
            }
        }

        private fun getHeartMsg() {
            Retrofit.Builder()
                    .baseUrl(ApiUtils.BBS_BASE_URL)
                    .client(TokenClient.get())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(HeartMsgInterface::class.java)
                    .getHeartMsg().enqueue(object : Callback<MsgHeartBean> {
                        override fun onFailure(call: Call<MsgHeartBean>, t: Throwable) {
                        }

                        override fun onResponse(call: Call<MsgHeartBean>, response: Response<MsgHeartBean>) {
                            val heartBean = response.body() ?: return
                            if (heartBean.rs != 1 || heartBean.body == null) return
                            log("私信数量:${heartBean.body?.pmInfos?.size!!}")
                            log("pmCount is $pmCount")
                            if (heartBean.body!!.replyInfo?.count!! > rmCount) {
                                rmCount = heartBean.body!!.replyInfo?.count!!
                                showMsgNotification(title = "您有${heartBean.body?.replyInfo?.count}条新回复",
                                        content = TimeUtils.stampChange(heartBean.body?.replyInfo?.time),
                                        notificationId = rNotificationId,
                                        msgType = MSG_TYPE_REPLY,
                                        count = heartBean.body?.replyInfo?.count!! )
                            }
                            if (heartBean.body!!.atMeInfo?.count!! > amCount) {
                                amCount = heartBean.body!!.atMeInfo?.count!!
                                showMsgNotification(title = "您有${heartBean.body?.atMeInfo?.count}条@消息",
                                        content = TimeUtils.stampChange(heartBean.body?.atMeInfo?.time),
                                        notificationId = rNotificationId,
                                        msgType = MSG_TYPE_AT,
                                        count = heartBean.body?.atMeInfo?.count!!)
                            }
                            if (heartBean.body?.pmInfos?.size!! > pmCount) {
                                pmCount = heartBean.body?.pmInfos?.size!!
                                showMsgNotification(title = "您有${heartBean.body!!.pmInfos?.size}条私信",
                                        content = TimeUtils.stampChange(heartBean.body!!.pmInfos!![0].time),
                                        notificationId = rNotificationId,
                                        msgType = MSG_TYPE_PRIVATE,
                                        count = heartBean.body!!.pmInfos?.size!!)
                            }
                            System.gc()
                        }
                    })
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    /**
     * 正常的声明周期中，Service被销毁会回调以下方法
     * 在其中重启服务
     * 但是如果被管家类应用kill，并不会走该方法，无视
     * !!!禁用重启服务，因为产生的影响未知
     */
    override fun onDestroy() {
        log("stop service")
        super.onDestroy()
        flag = false
        msgThread?.interrupt()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(Intent(this, HeartMsgService::class.java))
//        } else {
//            startService(Intent(this, HeartMsgService::class.java))
//        }
    }
}

interface HeartMsgInterface {
    @GET(ApiUtils.BBS_MESSAGE_HEART_URL)
    fun getHeartMsg(): Call<MsgHeartBean>
}
