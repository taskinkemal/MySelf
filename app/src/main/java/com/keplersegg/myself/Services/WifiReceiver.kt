package com.keplersegg.myself.Services

import android.net.wifi.WifiManager
import android.net.NetworkInfo
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import com.keplersegg.myself.Helper.AutoTaskType
import com.keplersegg.myself.Helper.Utils
import com.keplersegg.myself.Room.AppDatabase
import com.keplersegg.myself.Room.Entity.Entry
import com.keplersegg.myself.Room.Entity.Task
import org.jetbrains.anko.doAsync
import java.util.*


class WifiReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val action = intent.action

        if (action == WifiManager.NETWORK_STATE_CHANGED_ACTION) {

            val info = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
            if (info != null && info.isConnected) {
                // Do your work.

                // e.g. To check the Network Name or other info:
                val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifiInfo = wifiManager.connectionInfo
                val networkId = wifiInfo.networkId

                doAsync {

                    val tasks = getAppDB(context)
                            .taskDao().all.filter { t -> t.AutomationType == AutoTaskType.WentTo.typeId && t.AutomationVar == networkId.toString() }

                    for (task: Task in tasks) {

                        updateEntry(context, task.Id, Utils.getToday())
                    }
                }
            }
        }
    }

    private fun updateEntry(context: Context, taskId: Int, day: Int) {

        var entry = getAppDB(context).entryDao()[day, taskId]

        if (entry == null) {

            entry = Entry()
            entry.TaskId = taskId
            entry.Day = day
            entry.Value = 1
            entry.ModificationDate = Date(System.currentTimeMillis())
            getAppDB(context).entryDao().insert(entry)
        }
        else {
            entry.Value = 1
            entry.ModificationDate = Date(System.currentTimeMillis())
            getAppDB(context).entryDao().update(entry)
        }
    }

    private fun getAppDB(context: Context) : AppDatabase {
        return AppDatabase
                .getAppDatabase(context)!!
    }
}