package com.keplersegg.myself.Fragments


import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_tasks.*

import com.keplersegg.myself.Adapters.TasksAdapter
import com.keplersegg.myself.Async.GetTaskEntries
import com.keplersegg.myself.Interfaces.IGetTasksHost
import com.keplersegg.myself.R
import com.keplersegg.myself.Room.Entity.TaskEntry


class TasksFragment : MasterFragment(), IGetTasksHost {

    private var adapter: TasksAdapter? = null
    private var day = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layout = R.layout.fragment_tasks
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TasksAdapter(activity!!, day)
        rcylTasks.adapter = adapter
        rcylTasks.layoutManager = LinearLayoutManager(activity)
    }

    override fun onResume() {
        super.onResume()
        SetTitle(R.string.lbl_tasks)

        GetTaskEntries(this).execute(day)
    }

    override fun onGetTasksSuccess(items: List<TaskEntry>) {

        adapter!!.updateData(items)
    }

    override fun onGetTasksError(message: String) {

        showErrorMessage(message)
    }

    companion object {

        fun newInstance(day: Int): TasksFragment {

            val fragment = TasksFragment()

            fragment.day = day

            return fragment
        }
    }
}
