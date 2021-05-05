package com.example.todoapp.alarmes.servi

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.todoapp.alarmes.reciv.AlarmReceiver
import com.example.todoapp.utils.Constants
import com.example.todoapp.utils.RandomUtil

class AlarmService(private val context: Context) {
    private val alarmManager: AlarmManager? =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?


    fun setExactAlarm(timeInMillis: Long, string: String) {
        setAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = Constants.ACTION_SET_EXACT
                    putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                    putExtra(Constants.ExTRA_ALARM_TITLE, string)
                }
            )
        )
    }

    //1 Week
    fun setRepetitiveAlarm(timeInMillis: Long, string: String, every: Int) {
        setAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = Constants.ACTION_SET_REPETITIVE_EXACT
                    putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                    putExtra(Constants.ExTRA_ALARM_TITLE, string)
                    putExtra(Constants.ExTRA_ALARM_DURATION, every)
                }
            )
        )
    }

    private fun getPendingIntent(intent: Intent) =
        PendingIntent.getBroadcast(
            context,
            getRandomRequestCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


    private fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent) {
        alarmManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            }
        }
    }

    private fun getIntent() = Intent(context, AlarmReceiver::class.java)

    private fun getRandomRequestCode() = RandomUtil.getRandomInt()

}