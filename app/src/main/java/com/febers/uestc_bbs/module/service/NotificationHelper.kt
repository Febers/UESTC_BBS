package com.febers.uestc_bbs.module.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.febers.uestc_bbs.R

class NotificationHelper {

    private var notificationManager: NotificationManager? = null
    private var notification: Notification? = null
    private var requestCode = 0

    /**
     * 显示通知
     * 注意的是高版本的通知显示有所不同
     *
     * @param context
     * @param title
     * @param content
     * @param channelId
     * @param channelName
     * @param notificationId
     * @param intents
     * @param autoCancel
     * @param smallIcon
     */
    fun show(context: Context,
             title: String,
             content: String,
             channelId: String,
             channelName: String,
             notificationId: Int,
             intents: Array<Intent>,
             autoCancel: Boolean = true,
             smallIcon: Int = R.drawable.xic_ship_white) {

        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager!!.createNotificationChannel(notificationChannel)
            Notification.Builder(context, channelId)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(PendingIntent.getActivities(context, requestCode, intents, PendingIntent.FLAG_CANCEL_CURRENT))
                    .setAutoCancel(autoCancel)
                    .setSmallIcon(smallIcon)
                    .build()
        } else {
            NotificationCompat.Builder(context, channelId)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(PendingIntent.getActivities(context, requestCode, intents, PendingIntent.FLAG_CANCEL_CURRENT))
                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setAutoCancel(autoCancel)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setSmallIcon(smallIcon)
                    .build()
        }
        notificationManager?.notify(notificationId, notification)
    }

    fun cancelNotification(notificationId: Int){
        notificationManager?.cancel(notificationId)
    }
}