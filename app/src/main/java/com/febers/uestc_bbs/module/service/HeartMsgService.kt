package com.febers.uestc_bbs.module.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log.i
import com.febers.uestc_bbs.MyApplication
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.MsgHeartBean
import com.febers.uestc_bbs.home.HomeActivity
import com.febers.uestc_bbs.http.TokenClient
import com.febers.uestc_bbs.module.message.view.PMDetailActivity
import com.febers.uestc_bbs.utils.ApiUtils
import com.febers.uestc_bbs.utils.TimeUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class HeartMsgService : Service() {

    private lateinit var notificationManager: NotificationManager
    private lateinit var notification: Notification

    private val notificationId_p = 970418
    private val notificationId_r = 970419
    private val notificationId_a = 970420
    private val notificationId_s = 970421
    private val requestCode = 0
    private val privateChannelId = "pci"
    private val privateChannelName = "pcn"
    private val otherChannelId = "other"
    private val otherChannelName = "other"
    private val replyChannelId = "rci"
    private val replyChannelName = "rcn"
    private val atChannelId = "aci"
    private val atChannelName = "acn"
    private val systemChannelId = "sci"
    private val systemChannelName = "scn"

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //注意Android8上，当应用运行在后台时，启动服务，会报IllegalStateException
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && MyApplication.appUiHidden()) {
            startForeground(Int.MAX_VALUE, createNotification(title = "后台运行中", content = "", msgType = "", count = 0))
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        HeartMsgThread().start()
    }

    /**
     * 初始化通知栏工具
     */
    private fun createNotification(title: String, content: String, msgType: String, count: Int, uid: Int = -1): Notification {
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
        val intents = arrayOf(Intent(this,
                if (msgType == MSG_TYPE_PRIVATE && count == 1)PMDetailActivity::class.java else HomeActivity::class.java )
                .apply {
                    putExtra(USER_ID, uid.toString())
                    putExtra(MSG_TYPE, msgType)
                    putExtra(MSG_COUNT, count)
                    flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                })
        EventBus.getDefault().post(MsgEvent(BaseCode.SUCCESS, count))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
            notification = Notification.Builder(this, channelId)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(PendingIntent.getActivities(this, requestCode, intents, PendingIntent.FLAG_CANCEL_CURRENT))
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_default_circle)
                    .build()
        } else {
            val builder = NotificationCompat.Builder(this, channelId)
            notification = builder
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(PendingIntent.getActivities(this, requestCode, intents, PendingIntent.FLAG_CANCEL_CURRENT))
                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setSmallIcon(R.mipmap.ic_default_circle)
                    .build()
        }
        return notification
    }

    private fun showNotification(id: Int) {
        notificationManager.notify(id, notification)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getMsgFeedback(event: MsgFeedbackEvent) {
        i("service", "cancel: ${event.type}")
        notificationManager.cancel(when(event.type) {
            MSG_TYPE_REPLY -> notificationId_r
            MSG_TYPE_PRIVATE -> notificationId_p
            MSG_TYPE_AT -> notificationId_a
            MSG_TYPE_SYSTEM -> notificationId_s
            else -> return
        })
    }

    /*
       实时获取未读通知的线程类
     */
    inner class HeartMsgThread: Thread() {
        override fun run() {
            while (true) {
                getHeartMsg()
                sleep(6*1000)
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
                            i("service", "response")
                            if (heartBean.rs != 1 || heartBean.body == null) return
                            if (heartBean.body!!.replyInfo?.count!! > 0) {
                                createNotification(title = "${heartBean.body?.replyInfo?.count}条新回复",
                                        content = TimeUtils.stampChange(heartBean.body?.replyInfo?.time),
                                        msgType = MSG_TYPE_REPLY,
                                        count = heartBean.body?.replyInfo?.count!! )
                                showNotification(notificationId_r)
                            }
                            if (heartBean.body!!.atMeInfo?.count!! > 0) {
                                createNotification(title = "${heartBean.body?.atMeInfo?.count}条@消息",
                                        content = TimeUtils.stampChange(heartBean.body?.atMeInfo?.time),
                                        msgType = MSG_TYPE_AT,
                                        count = heartBean.body?.atMeInfo?.count!!)
                                showNotification(notificationId_a)
                            }
                            if (heartBean.body?.pmInfos?.size!! > 0) {
                                if (heartBean.body!!.pmInfos?.size!! > 3) {
                                    createNotification(title = "${heartBean.body!!.pmInfos?.size}条私信",
                                            content = TimeUtils.stampChange(heartBean.body!!.pmInfos!![0].time),
                                            msgType = MSG_TYPE_PRIVATE,
                                            count = heartBean.body!!.pmInfos?.size!!)
                                    showNotification(notificationId_p)
                                } else {
                                    heartBean.body!!.pmInfos?.forEach {
                                        createNotification(title = "来自${it.fromUid}的消息",
                                                content = TimeUtils.stampChange(it.time) + "之前",
                                                msgType = MSG_TYPE_PRIVATE,
                                                count = 1,
                                                uid = it.fromUid)
                                        showNotification(notificationId_s)
                                    }
                                }
                            }
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
     */
    override fun onDestroy() {
        super.onDestroy()
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
