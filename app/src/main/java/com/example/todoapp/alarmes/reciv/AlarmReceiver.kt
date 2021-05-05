package com.example.todoapp.alarmes.reciv

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.format.DateFormat
import androidx.core.app.NotificationCompat
import com.example.todoapp.MainActivity
import com.example.todoapp.R
import com.example.todoapp.alarmes.servi.AlarmService
import com.example.todoapp.utils.Constants
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {
    private var notificationManager: NotificationManager? = null
    private lateinit var title: String
    private var duration: Int? = null
    private val channelId = "com.example.todoapp.alarmes.reciv"
    override fun onReceive(context: Context, intent: Intent) {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val timeInMillis = intent.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L)
        title = intent.getStringExtra(Constants.ExTRA_ALARM_TITLE) ?: "No Task Title"
        duration = intent.getIntExtra(Constants.ExTRA_ALARM_DURATION, 0)
        when (intent.action) {
            Constants.ACTION_SET_EXACT -> {
                createNotification(
                    context,
                    "Task Reminder",
                    title,
                    convertDate(timeInMillis)
                )
            }

            Constants.ACTION_SET_REPETITIVE_EXACT -> {
                setRepetitiveAlarm(AlarmService(context), duration!!, title)
                createNotification(
                    context,
                    "Periodic Task Reminder",
                    title,
                    convertDate(timeInMillis)
                )
            }
        }
    }

    private fun createNotification(
        context: Context,
        name: String,
        desc: String = "No data Found",
        convertDate: String
    ) {

        val notificationId = 23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH).apply {
                    description = desc
                }
            notificationManager?.createNotificationChannel(channel)
        }
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(101, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val op = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.barbarians)
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(name)
            .setContentText("Task Reminder for $desc Created On : $convertDate")
            .setSmallIcon(android.R.drawable.sym_def_app_icon)
            .setAutoCancel(true)
            .setSound(op)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()
        notificationManager?.notify(notificationId, notification)
    }

    private fun setRepetitiveAlarm(alarmService: AlarmService, duration: Int, string: String) {
        val cal = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(duration.toLong())
            Timber.d("Set alarm for next week same time - ${convertDate(this.timeInMillis)}")
        }
        alarmService.setRepetitiveAlarm(cal.timeInMillis, string, duration)
    }

    private fun convertDate(timeInMillis: Long): String =
        DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()
}