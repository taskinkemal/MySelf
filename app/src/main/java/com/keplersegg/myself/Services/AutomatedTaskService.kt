package com.keplersegg.myself.Services

import android.app.job.JobParameters
import android.app.job.JobService
import android.app.usage.UsageStatsManager
import android.content.Context
import java.util.*
import android.provider.CallLog
import com.keplersegg.myself.Helper.AutoTaskType
import com.keplersegg.myself.Helper.Utils
import com.keplersegg.myself.Room.AppDatabase
import com.keplersegg.myself.Room.Entity.Entry
import com.keplersegg.myself.Room.Entity.Task
import org.jetbrains.anko.doAsync


class AutomatedTaskService: JobService() {

    override fun onStopJob(params: JobParameters?): Boolean {

        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {

        doAsync {

            val tasks = getAppDB()
                    .taskDao().all.filter { t -> t.AutomationType != null }

            for (task: Task in tasks) {

                when (AutoTaskType.valueOf(task.AutomationType!!)) {

                    AutoTaskType.CallDuration -> { getCallDurations(task.Id) }
                    AutoTaskType.AppUsage -> { getAppUsage(task.Id, task.AutomationVar!!.hashCode()) }
                    else -> { }
                }
            }
        }

        jobFinished(params, true)
        return false
    }

    private fun getAppUsage(taskId: Int, id: Int) {

        for (i: Int in -5 .. 0) {

            val mUsageStatsManager:UsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

            val fromDate = Utils.getDayBack(i)
            val toDate = Utils.getDayBack(i + 1)

            val stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                    fromDate, toDate)

            val today = Utils.getToday()
            val pkgStats = stats.firstOrNull { s -> s.packageName.hashCode() == id }

            if (pkgStats != null) {

                val totalTime = pkgStats.totalTimeInForeground

                val minutes = (totalTime / 60000).toInt()

                updateEntry(taskId, today + i, minutes)

            }
        }
    }

    private fun updateEntry(taskId: Int, day: Int, duration: Int) {

        var entry = getAppDB().entryDao()[day, taskId]

        if (entry == null) {

            entry = Entry()
            entry.TaskId = taskId
            entry.Day = day
            entry.Value = duration
            entry.ModificationDate = Date(System.currentTimeMillis())
            getAppDB().entryDao().insert(entry)
        }
        else {
            entry.Value = duration
            entry.ModificationDate = Date(System.currentTimeMillis())
            getAppDB().entryDao().update(entry)
        }
    }

    private fun getAppDB() : AppDatabase {
        return AppDatabase
                .getAppDatabase(applicationContext)!!
    }

    private fun getCallDurations(taskId: Int) {

        for (i: Int in -5 .. 0) {

            val duration = getCalldetails(-i)
            val today = Utils.getToday()

            updateEntry(taskId, today + i, duration / 60)

            //if (HttpClient.hasInternetAccess(application!!)) {
            //    uploadEntry(item.entry!!)
            //}
        }
    }

    private fun getCalldetails(daysBack: Int) : Int {
        var totalSeconds = 0

        val strOrder = android.provider.CallLog.Calls.DATE + " DESC"

        val fromDate = Utils.getDayBack(daysBack)
        val toDate = Utils.getDayBack(daysBack - 1)

        val whereValue = arrayOf(fromDate.toString(), toDate.toString())

        val managedCursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, android.provider.CallLog.Calls.DATE + " BETWEEN ? AND ?", whereValue, strOrder)

        if (managedCursor != null) {

            val duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION)

            while (managedCursor.moveToNext()) {

                totalSeconds += managedCursor.getInt(duration)

            }

            managedCursor.close()
        }

        return totalSeconds
    }
}