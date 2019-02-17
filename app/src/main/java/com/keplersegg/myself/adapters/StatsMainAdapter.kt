package com.keplersegg.myself.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keplersegg.myself.R
import com.keplersegg.myself.activities.MainActivity
import com.keplersegg.myself.fragments.AppUsageFragment
import com.keplersegg.myself.fragments.StatsTaskFragment
import com.keplersegg.myself.helper.AutoTaskType
import com.keplersegg.myself.models.TaskGeneralStats
import java.util.*

class StatsMainAdapter(private val activity: MainActivity) : RecyclerView.Adapter<StatsMainAdapter.DataObjectHolder>() {

    private val items: ArrayList<TaskGeneralStats> = ArrayList()

    inner class DataObjectHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val lblTask: TextView = itemView.findViewById(R.id.lblTask)
        val lblTotal: TextView = itemView.findViewById(R.id.lblTotal)
        val imgAutomationType: ImageView = itemView.findViewById(R.id.imgAutomationType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataObjectHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_stats_main, parent, false)

        return DataObjectHolder(itemView = view)
    }

    init {

        this.setHasStableIds(true)
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {

        // object item based on the position
        val item = if (items.size > position) items[position] else null

        holder.lblTask.text = item!!.task!!.Label
        holder.lblTotal.text = item.total.toString()
        val imgResourceId = TaskGeneralStats.taskTypeImageResourceId(item.task!!.AutomationType)
        if (imgResourceId == null) {

            holder.imgAutomationType.visibility = View.INVISIBLE

        } else {

            holder.imgAutomationType.visibility = View.VISIBLE
            if (item.task!!.AutomationType == AutoTaskType.AppUsage.typeId) {
                holder.imgAutomationType.setImageDrawable(AppUsageFragment.getApplicationIcon(activity, item.task!!.AutomationVar!!))
            } else {
                holder.imgAutomationType.setImageResource(imgResourceId)
            }
        }

        holder.itemView.setOnClickListener {
            activity.NavigateFragment(true, StatsTaskFragment.newInstance(item.task!!.Id))
        }
    }

    override fun getItemId(position: Int): Long {

        return items[position].task!!.Id.toLong()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(list: List<TaskGeneralStats>) {

        this.activity.runOnUiThread {
            items.clear()
            items.addAll(list)
            notifyDataSetChanged()
        }
    }
}