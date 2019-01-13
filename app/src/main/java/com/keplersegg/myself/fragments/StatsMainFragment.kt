package com.keplersegg.myself.fragments


import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager

import com.keplersegg.myself.R
import com.keplersegg.myself.Room.Entity.Task
import com.keplersegg.myself.adapters.StatsMainAdapter
import kotlinx.android.synthetic.main.fragment_stats_main.*
import com.keplersegg.myself.helper.GridSpacingItemDecoration
import com.keplersegg.myself.models.TaskGeneralStats
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class StatsMainFragment : MasterFragment() {

    private lateinit var adapter: StatsMainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layout = R.layout.fragment_stats_main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = StatsMainAdapter(activity)
        rcylTasks!!.adapter = adapter
        rcylTasks.layoutManager = GridLayoutManager(activity, 2)

        rcylTasks.addItemDecoration(GridSpacingItemDecoration(4, 2))
     }

    override fun onResume() {
        super.onResume()
        SetTitle(R.string.lbl_statistics)

        doAsync {

            val allTasks = activity.AppDB().taskDao().getAll(1)
            val counts = activity.AppDB().entryDao().getCounts()

            val list = ArrayList<TaskGeneralStats>()

            for (task: Task in allTasks) {

                val count = counts.firstOrNull { c -> c.TaskId == task.Id }

                val item = TaskGeneralStats()
                item.task = task
                item.total = if (count == null) 0 else count.EntryCount!!
                list.add(item)
            }

            uiThread {
                adapter.updateData(list)
            }
        }
    }

    companion object {

        fun newInstance(): StatsMainFragment {
            return StatsMainFragment()
        }
    }
}
