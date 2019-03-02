package com.keplersegg.myself.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.keplersegg.myself.Room.Entity.Task
import com.keplersegg.myself.helper.AutoTaskType
import com.keplersegg.myself.helper.TaskUpdater
import com.keplersegg.myself.helper.Utils
import org.jetbrains.anko.doAsync

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

            val tasks = TaskUpdater.GetAppDB(context)
                    .taskDao().getAll(1).filter { t -> AutoTaskType.valueOf(t.AutomationType!!) == AutoTaskType.CallDuration }

            for (task: Task in tasks) {

                TaskUpdater.UpdateEntry(context, task.Id, today, minutes)
            }
        }
    }
}