package com.keplersegg.myself.helper

import android.content.Context
import com.keplersegg.myself.Room.AppDatabase
import com.keplersegg.myself.Room.Entity.Entry
import java.util.*

object TaskUpdater {

    fun UpdateEntry(context: Context, taskId: Int, day: Int, value: Int) {

        var entry = GetAppDB(context).entryDao()[day, taskId]

        if (entry == null) {

            entry = Entry()
            entry.TaskId = taskId
            entry.Day = day
            entry.Value = value
            entry.ModificationDate = Date(System.currentTimeMillis())
            GetAppDB(context).entryDao().insert(entry)
        }
        else {
            entry.Value = value
            entry.ModificationDate = Date(System.currentTimeMillis())
            GetAppDB(context).entryDao().update(entry)
        }
    }


    fun GetAppDB(context: Context) : AppDatabase {
        return AppDatabase
                .getAppDatabase(context)!!
    }
}