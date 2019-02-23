package com.keplersegg.myself.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.keplersegg.myself.Room.AppDatabase
import com.keplersegg.myself.Room.Entity.Entry
import com.keplersegg.myself.Room.Entity.Task
import com.keplersegg.myself.helper.AutoTaskType
import com.keplersegg.myself.helper.Utils
import org.jetbrains.anko.doAsync
import java.util.*

class CallDurationReceiver : BroadcastReceiver() {

    private var flag = false
    private var start: Long = 0
    private var map = hashMapOf<Int, Long>()

    override fun onReceive(context: Context, intent: Intent) {

        val action = intent.action

        if (action == "android.intent.action.PHONE_STATE") {
            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_OFFHOOK) {

                flag = true
                start = System.currentTimeMillis()
            }

            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE) {

                if (flag) {

                    flag = false
                    updateCallDuration(context, System.currentTimeMillis() - start)
                }
            }
        }
    }

    private fun updateCallDuration(context: Context, duration: Long) {

        val today = Utils.getToday()
        if (!map.containsKey(today)) {
            map[today] = duration
        }
        else {
            val current = if (map[today] != null) map[today] else 0
            map[today] = current!! + duration
        }

        var minutes = (map[today]!! / 60000).toInt()
        if (minutes == 0 && duration > 0) minutes = 1

        doAsync {

            val tasks = getAppDB(context)
                    .taskDao().getAll(1).filter { t -> AutoTaskType.valueOf(t.AutomationType!!) == AutoTaskType.CallDuration }

            for (task: Task in tasks) {

                updateEntry(context, task.Id, today, minutes)
            }
        }
    }

    private fun getAppDB(context: Context) : AppDatabase {
        return AppDatabase
                .getAppDatabase(context)!!
    }

    private fun updateEntry(context: Context, taskId: Int, day: Int, duration: Int) {

        var entry = getAppDB(context).entryDao()[day, taskId]

        if (entry == null) {

            entry = Entry()
            entry.TaskId = taskId
            entry.Day = day
            entry.Value = duration
            entry.ModificationDate = Date(System.currentTimeMillis())
            getAppDB(context).entryDao().insert(entry)
        }
        else {
            entry.Value = duration
            entry.ModificationDate = Date(System.currentTimeMillis())
            getAppDB(context).entryDao().update(entry)
        }
    }
}