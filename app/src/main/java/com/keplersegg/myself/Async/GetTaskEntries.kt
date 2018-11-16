package com.keplersegg.myself.Async

import android.os.AsyncTask
import com.keplersegg.myself.Interfaces.IGetTasksHost

import com.keplersegg.myself.Room.Entity.TaskEntry


open class GetTaskEntries(private var activity: IGetTasksHost) : AsyncTask<Int, Void, List<TaskEntry>>() {

    override fun doInBackground(vararg params: Int?): List<TaskEntry> {

        val day = params[0]

        return activity.getAppDB().taskEntryDao().getTasks(day!!)
    }

    override fun onPostExecute(result: List<TaskEntry>) {

        activity.onGetTasksSuccess(result)
    }
}