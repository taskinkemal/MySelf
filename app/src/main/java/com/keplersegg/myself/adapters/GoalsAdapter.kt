package com.keplersegg.myself.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keplersegg.myself.R
import com.keplersegg.myself.Room.Entity.Goal
import com.keplersegg.myself.activities.MainActivity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class GoalsAdapter(private val activity: MainActivity) : RecyclerView.Adapter<GoalsAdapter.DataObjectHolder>() {

    val items: ArrayList<Goal> = ArrayList()

    inner class DataObjectHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val lblTask: TextView = itemView.findViewById(R.id.lblTask)
        val lblCurrentValue: TextView = itemView.findViewById(R.id.lblCurrentValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataObjectHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_goal_card, parent, false)

        return DataObjectHolder(itemView = view)
    }

    init {

        this.setHasStableIds(true)
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {

        // object item based on the position
        val item = if (items.size > position) items[position] else null

        updateUi(holder, item!!)
    }

    private fun updateUi(holder: DataObjectHolder, item: Goal) {

        holder.lblCurrentValue.text = item.CurrentValue.toString()

        doAsync {
            val task = activity.AppDB().taskDao().get(item.TaskId)

            uiThread {
                holder.lblTask.text = task.Label
            }
        }
    }

    override fun getItemId(position: Int): Long {

        return items[position].Id.toLong()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(list: List<Goal>) {

        this.activity.runOnUiThread {
            items.clear()
            items.addAll(list)
            notifyDataSetChanged()
        }
    }
}