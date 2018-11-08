package com.keplersegg.myself.Fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.keplersegg.myself.Helper.ServiceMethods
import kotlinx.android.synthetic.main.fragment_add_task.*

import com.keplersegg.myself.R
import com.keplersegg.myself.Room.Entity.Task
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class AddTaskFragment : MasterFragment() {

    var TaskId = 0 // 0 means new task.
    private var task: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layout = R.layout.fragment_add_task
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtUnits!!.addTextChangedListener(InputTextWatcher())

        swcUnits!!.setOnCheckedChangeListener { _, isChecked ->
            SetUnitsVisibility(isChecked)
            SetUnitsLabel(isChecked, txtUnits!!.text.toString())
        }

        swcHasGoal!!.setOnCheckedChangeListener { _, isChecked -> SetGoalVisibility(isChecked) }

        btnSave.setOnClickListener { _ -> SaveTask() }

        if (TaskId != 0) {

            doAsync { task = activity!!.AppDB().taskDao().get(TaskId)
                uiThread { PrefillTask() }
            }
        } else {

            SetUnitsVisibility(false)
            SetGoalVisibility(false)
        }
    }

    private fun SaveTask() {

        val label = txtLabel!!.text.toString()
        val dataType = if (swcUnits!!.isChecked) 1 else 0
        val unit = if (dataType == 1) txtUnits!!.text.toString() else ""
        val hasGoal = swcHasGoal!!.isChecked
        val goal = if (hasGoal) ConvertToInteger(txtGoal!!.text.toString()) else 0
        val goalMinMax = if (hasGoal) spnGoalMinMax!!.selectedItemId.toInt() else 0
        val goalTimeFrame = if (hasGoal) spnGoalTimeFrame!!.selectedItemId.toInt() else 0

        if (label.length > 0 &&
                (dataType == 0 || unit.length > 0) &&
                goal >= 0 &&
                (!hasGoal || goalMinMax >= 1 && goalMinMax <= 3 && goalTimeFrame >= 1 && goalTimeFrame <= 3)) {

            doAsync {
                AddOrUpdateTask(label, dataType, unit, hasGoal, goal, goalMinMax, goalTimeFrame)
                uiThread {
                    activity!!.NavigateFragment(true, TasksPagerFragment.newInstance())
                }
            }
        }
    }

    private fun PrefillTask() {

        if (task != null) {

            txtLabel!!.setText(task!!.label)
            swcUnits!!.isChecked = task!!.dataType == 1
            SetUnitsVisibility(task!!.dataType == 1)
            txtUnits!!.setText(task!!.unit)
            swcHasGoal!!.isChecked = task!!.hasGoal!!
            SetGoalVisibility(task!!.hasGoal!!)
            txtGoal!!.setText(String.format(Locale.getDefault(), "%d", task!!.goal))
            val unit = if (task!!.dataType == 0) task!!.unit else ""
            lblUnits!!.text = if (unit != null && unit.length > 0) unit else "items"
            if (task!!.goalMinMax > 0) {

                val position = Arrays.asList(*activity!!.resources.getStringArray(R.array.spinner_goalMinMax_values)).indexOf(task!!.goalMinMax.toString())

                spnGoalMinMax!!.setSelection(position)
            }

            if (task!!.goalTimeFrame > 0) {

                val position = Arrays.asList(*activity!!.resources.getStringArray(R.array.spinner_goalTimeFrame_values)).indexOf(task!!.goalTimeFrame.toString())

                spnGoalMinMax!!.setSelection(position)
            }
        }
    }

    private fun AddOrUpdateTask(label: String, dataType: Int, unit: String, hasGoal: Boolean, goal: Int,
                                goalMinMax: Int, goalTimeFrame: Int): Boolean {

        if (this.TaskId != 0) {

            if (this.activity!!.AppDB().taskDao().getCountByLabelExcludeId(TaskId, label) == 0) {

                task!!.label = label
                task!!.dataType = dataType
                task!!.unit = unit
                task!!.hasGoal = hasGoal
                task!!.goal = goal
                task!!.goalMinMax = goalMinMax
                task!!.goalTimeFrame = goalTimeFrame

                var canUpdate = true

                if (!activity!!.application.dataStore.getAccessToken().isNullOrBlank()) {
                    val taskId = ServiceMethods.uploadTask(activity!!, task!!)
                    canUpdate = taskId != -1
                }

                if (canUpdate) {

                    activity!!.AppDB().taskDao().update(task)
                    return true
                }
                else
                    return false
            }
        } else {

            if (activity!!.AppDB().taskDao().getCountByLabel(label) == 0) {

                val newTask = Task.CreateItem(-1, label, dataType, unit, hasGoal, goal, goalMinMax, goalTimeFrame)

                var taskId = -1

                if (!activity!!.application.dataStore.getAccessToken().isNullOrBlank()) {
                    taskId = ServiceMethods.uploadTask(activity!!, newTask)
                }
                else {
                    taskId = this.activity!!.AppDB().taskDao().maxId
                }

                if (taskId != -1) {
                    newTask.Id = taskId
                    activity!!.AppDB().taskDao().insert(newTask)
                    return true
                }
                else
                    return false
            }
        }

        return false
    }

    private fun ConvertToInteger(text: String): Int {

        var result = 0

        try {
            result = Integer.parseInt(text)
        } catch (nfe: NumberFormatException) {
        }

        return result
    }

    private fun SetUnitsVisibility(isVisible: Boolean) {

        lytUnits!!.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun SetGoalVisibility(isVisible: Boolean) {

        lytGoal!!.visibility = if (isVisible) View.VISIBLE else View.GONE
        spnGoalMinMax!!.visibility = if (isVisible) View.VISIBLE else View.GONE
        spnGoalTimeFrame!!.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun SetUnitsLabel(isUnitsSelected: Boolean, text: String?) {

        val result = if (isUnitsSelected && text != null && text.length > 0) text else ""

        lblUnits!!.text = if (result.length > 0) result else "items"
    }

    override fun onResume() {
        super.onResume()
        SetTitle(R.string.lbl_task_details)
    }

    private inner class InputTextWatcher internal constructor() : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable) {

            SetUnitsLabel(swcUnits!!.isChecked, s.toString())
        }
    }

    companion object {

        fun newInstance(taskId: Int): AddTaskFragment {

            val fragment = AddTaskFragment()

            fragment.TaskId = taskId

            return fragment
        }
    }
}