package com.github.aptemkov.loadapp.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.github.aptemkov.loadapp.DetailActivity
import com.github.aptemkov.loadapp.R

class NotificationService(private val context: Context) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(title: String, content: String) {
        val activityIntent = Intent(context, DetailActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }.apply {
            putExtra("title", title)
            putExtra("content", content)
        }

        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
                PendingIntent.FLAG_IMMUTABLE
        )

        val action = NotificationCompat.Action.Builder(0,"Check the status",activityPendingIntent).build()


        val notification = NotificationCompat.Builder(
            context, MAIN_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(action)
            .setContentIntent(activityPendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(1, notification.build())
    }

    companion object {
        const val MAIN_CHANNEL_ID = "notification_channel"
    }
}