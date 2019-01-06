package com.keplersegg.myself.fragments


import android.os.Bundle
import com.github.mikephil.charting.data.Entry

import com.keplersegg.myself.R
import com.keplersegg.myself.helper.ChartsHelper
import kotlinx.android.synthetic.main.fragment_stats_task.*
import com.keplersegg.myself.helper.Utils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.collections.ArrayList


class StatsTaskFragment : MasterFragment() {

    private var TaskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layout = R.layout.fragment_stats_task
    }

    override fun onResume() {
        super.onResume()

        doAsync {

            val entries = getEntries()

            if (entries.size > 1)
            {
                ChartsHelper.initEntriesChart(graphEntriesPerDay, entries)
            }

            val allGoals = activity.AppDB().goalDao().all.filter { g -> g.TaskId == TaskId }

            uiThread {

                lblGoalSuccessful.text = allGoals.count { g -> g.AchievementStatus == 1 }.toString()
                lblGoalFailed.text = allGoals.count { g -> g.AchievementStatus == 2 }.toString()
                lblGoalInProgress.text = allGoals.count { g -> g.AchievementStatus == 0 }.toString()
            }
        }
    }

    private fun getEntries() : ArrayList<Entry> {

        val entries = ArrayList<Entry>()

        val allEntries = activity.AppDB().entryDao().all.filter { e -> e.TaskId == TaskId }.sortedBy { e -> e.Day }

        if (allEntries.size > 1)
        {
            val minDay = allEntries.first().Day
            val maxDay = Utils.getToday()

            var index = 0
            for (day: Int in minDay..maxDay) {

                var value = 0

                if (allEntries[index].Day == day) {

                    value = allEntries[index].Value
                    index++
                }

                entries.add(Entry(day.toFloat(), value.toFloat()))
            }
        }

        return entries
    }

    companion object {

        fun newInstance(taskId: Int): StatsTaskFragment {
            val fragment = StatsTaskFragment()
            fragment.TaskId = taskId
            return fragment
        }
    }
}
