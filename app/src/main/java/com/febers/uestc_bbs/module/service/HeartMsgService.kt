package com.febers.uestc_bbs.module.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.MSG_TYPE
import com.febers.uestc_bbs.entity.MsgHeartBean
import com.febers.uestc_bbs.home.HomeActivity
import com.febers.uestc_bbs.http.TokenClient
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

class HeartMsgService : Service() {

    private lateinit var notification: Notification
    private lateinit var notificationManager: NotificationManager
    private val notificationId = 970418
    private val requestCode = 0
    private val channelId = "channel_id"
    private val channelName = "channel_name"

    override fun onCreate() {
        super.onCreate()
        //HeartMsgThread().start()
    }

    /**
     * 初始化通知栏工具
     */
    private fun createNotification(title: String, content: String, msgType: String, count: Int) {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this, "1")
        val intents = arrayOf(Intent(this, HomeActivity::class.java).apply {
            putExtra(MSG_TYPE, msgType)
            putExtra("msg_count", count)
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
            notification = Notification.Builder(this, channelId)
                    .setChannelId(channelId)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(PendingIntent.getActivities(this, requestCode, intents, PendingIntent.FLAG_CANCEL_CURRENT))
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_default_circle)
                    .build()
        } else {
            notification = builder
                    .setContentTitle("标题")
                    .setContentText("这是通知的内容")
                    .setContentIntent(PendingIntent.getActivities(this, requestCode, intents, PendingIntent.FLAG_CANCEL_CURRENT))
                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setSmallIcon(R.mipmap.ic_default_circle)
                    .build()
        }
    }

    private fun showNotification() {
        notificationManager.notify(notificationId, notification)
    }

    /*
       实时获取未读通知的线程类
     */
    inner class HeartMsgThread: Thread() {
        override fun run() {
            while (true) {
                getHeartMsg()
                sleep(5*1000)
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
                            if (response.body()?.rs != 1) return

                        }
                    })
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}

interface HeartMsgInterface {
    @GET(ApiUtils.BBS_MESSAGE_HEART_URL)
    fun getHeartMsg(): Call<MsgHeartBean>
}
