package com.keplersegg.myself.Adapters

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

import com.keplersegg.myself.Activities.MainActivity
import com.keplersegg.myself.Fragments.AddTaskFragment
import com.keplersegg.myself.Helper.HttpClient
import com.keplersegg.myself.Helper.ServiceMethods
import com.keplersegg.myself.R
import com.keplersegg.myself.Room.Entity.Entry
import com.keplersegg.myself.Room.Entity.TaskEntry
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.util.*

class TasksAdapter(private val activity: MainActivity?, private val items: List<TaskEntry>?, private val day: Int) : RecyclerView.Adapter<TasksAdapter.DataObjectHolder>() {

    private var lastPosition = -1

    inner class DataObjectHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lytListItem: LinearLayout? = itemView.findViewById(R.id.lytListItem)
        val lblLabel: TextView = itemView.findViewById(R.id.lblLabel)
        val imgDone: ImageButton? = itemView.findViewById(R.id.imgDone)
        val imgPlus: ImageButton? = itemView.findViewById(R.id.imgPlus)
        val imgMinus: ImageButton? = itemView.findViewById(R.id.imgMinus)
        val txtValue: TextView? = itemView.findViewById(R.id.txtValue)
        val txtUnit: TextView? = itemView.findViewById(R.id.txtUnit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataObjectHolder {

        val layout = if (viewType == 0) R.layout.list_item_task_boolean else R.layout.list_item_task_numeric
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)

        return DataObjectHolder(itemView = view)
    }

    init {

        this.setHasStableIds(true)
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {

        // object item based on the position
        val item = if (items != null) items[position] else null

        if (item != null) {

            var entry: Entry? = item.entry
            if (entry == null) {

                entry = Entry()
                entry.TaskId = item.task.id
                entry.Day = day
                entry.Value = 0
                entry.ModificationDate = Date(System.currentTimeMillis())
                item.entry = entry

                doAsync {
                    activity!!.AppDB().entryDao().insert(item.entry)
                    if (HttpClient.hasInternetAccess(activity)) {
                        uploadEntry(item.entry)
                    }
                uiThread { updateUi(holder, item, position) }
                }
            } else {
                updateUi(holder, item, position)
            }
        }

        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            if (item != null) {

                activity!!.NavigateFragment(true, AddTaskFragment.newInstance(item.task.id))
                return@OnLongClickListener true
            }
            false
        })

        if (holder.itemViewType == 0) {

            holder.imgDone!!.setOnClickListener {
                val entry = item!!.entry
                entry.Value = if (entry.Value == 0) 1 else 0
                entry.ModificationDate = Date(System.currentTimeMillis())
                setTint(holder.imgDone, entry.Value == 1)

                doAsync {
                    activity!!.AppDB().entryDao().update(item.entry)
                    if (HttpClient.hasInternetAccess(activity)) {
                        uploadEntry(item.entry)
                    }
                }
            }
        }
        else {

            holder.imgPlus!!.setOnClickListener {
                val entry = item!!.entry
                entry.Value++
                entry.ModificationDate = Date(System.currentTimeMillis())

                holder.txtValue!!.text = item.entry.Value.toString()

                doAsync {
                    activity!!.AppDB().entryDao().update(item.entry)
                    if (HttpClient.hasInternetAccess(activity)) {
                        uploadEntry(item.entry)
                    }
                }
            }

            holder.imgMinus!!.setOnClickListener {
                val entry = item!!.entry
                if (entry.Value > 0)
                    entry.Value--
                entry.ModificationDate = Date(System.currentTimeMillis())

                holder.txtValue!!.text = item.entry.Value.toString()

                doAsync {
                    activity!!.AppDB().entryDao().update(item.entry)
                    if (HttpClient.hasInternetAccess(activity)) {
                        uploadEntry(item.entry)
                    }
                }
            }
        }
    }

    private fun updateUi(holder: DataObjectHolder, item: TaskEntry, position: Int) {

        holder.lblLabel.text = item.task.label
        if (holder.itemViewType == 0) {
            setTint(holder.imgDone!!, item.entry.Value == 1)
        }
        else {
            holder.txtValue!!.text = item.entry.Value.toString()
            holder.txtUnit!!.text = item.task.unit
        }
        setAnimation(holder.lytListItem, position)
    }

    private fun setTint(imgDone: ImageButton, isChecked: Boolean) {

        val color = if (isChecked) R.color.colorAccent else android.R.color.darker_gray
        imgDone.setColorFilter(ContextCompat.getColor(activity!!, color),
                android.graphics.PorterDuff.Mode.SRC_IN)
    }

    private fun setAnimation(viewToAnimate: View?, position: Int) {
        if (activity == null) return

        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(activity, R.anim.slide_up)
            viewToAnimate!!.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun onViewDetachedFromWindow(holder: DataObjectHolder) {

        super.onViewDetachedFromWindow(holder)

        if (holder.lytListItem != null)
            holder.lytListItem.clearAnimation()
    }

    override fun getItemId(position: Int): Long {

        return items!![position].task.id.toLong()
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (items != null && items.count() > position)
            items[position].task.dataType
        else 0
    }

    private fun uploadEntry(entry: Entry) {

        ServiceMethods.uploadEntry(activity!!, entry)
    }
}