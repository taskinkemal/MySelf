package com.keplersegg.myself.Fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.keplersegg.myself.Helper.AutoTaskType
import com.keplersegg.myself.Helper.ServiceMethods
import com.keplersegg.myself.Models.ListItem
import kotlinx.android.synthetic.main.fragment_add_task.*

import com.keplersegg.myself.R
import com.keplersegg.myself.Room.Entity.Task
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import androidx.appcompat.app.AlertDialog



class AddTaskFragment : MasterFragment() {

    var TaskId = 0 // 0 means new task.
    var automationType: AutoTaskType? = null
    var automationData: ListItem? = null

    private var task: Task? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layout = R.layout.fragment_add_task

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtUnits!!.addTextChangedListener(InputTextWatcher())

        swcUnits!!.setOnCheckedChangeListener { _, isChecked ->
            SetUnitsVisibility(isChecked)
            SetUnitsLabel(isChecked, txtUnits!!.text.toString())
        }

        swcHasGoal!!.setOnCheckedChangeListener { _, isChecked -> SetGoalVisibility(isChecked) }

        if (TaskId != 0) {

            doAsync { task = activity!!.AppDB().taskDao().get(TaskId)
                uiThread {
                    PrefillTask()
                    FillAutomationDetails(false)
                }
            }
        } else {

            SetUnitsVisibility(false)
            SetGoalVisibility(false)

            FillAutomationDetails(true)
        }
    }

    private fun FillAutomationDetails(autoFillTask: Boolean) {

        lblAutomationSubHeader.visibility = if (automationType != null) View.VISIBLE else View.GONE
        lytAutomation.visibility = if (automationType != null) View.VISIBLE else View.GONE
        lytAutomationVar.visibility = if (automationData != null) View.VISIBLE else View.GONE

        if (automationType != null) {

            var labelResource = 0

            when (automationType) {

                AutoTaskType.CallDuration -> { labelResource = R.string.autotask_callDuration }
                AutoTaskType.AppUsage -> { labelResource = R.string.autotask_appUsage }
                AutoTaskType.WentTo -> { labelResource = R.string.autotask_wentTo }
            }

            lblAutomationType.text = activity!!.getString(labelResource)

            if (automationData != null) {

                when (automationType) {

                    AutoTaskType.AppUsage -> { lblAutomationVarHeader.text = "Application:" }
                    AutoTaskType.WentTo -> { lblAutomationVarHeader.text = "Network:" }
                    else -> { }
                }

                lblAutomationVar.text = automationData!!.Label
            }

            if (autoFillTask) {

                when (automationType) {

                    AutoTaskType.CallDuration -> {
                        txtLabel.setText(activity!!.getString(R.string.autotask_callDuration))
                        swcUnits.isChecked = true
                        txtUnits.setText("minute(s)")
                        swcUnits.isEnabled = false
                        txtUnits.isEnabled = false
                    }
                    AutoTaskType.AppUsage -> {
                        txtLabel.setText(activity!!.getString(R.string.autotask_appUsage) + " " + lblAutomationVar.text)
                        swcUnits.isChecked = true
                        txtUnits.setText("minute(s)")
                        swcUnits.isEnabled = false
                        txtUnits.isEnabled = false
                    }
                    AutoTaskType.WentTo -> {
                        txtLabel.setText(activity!!.getString(R.string.autotask_wentTo) + " " + lblAutomationVar.text)
                        swcUnits.isChecked = false
                        swcUnits.isEnabled = false
                    }
                }
            }
        }
    }

    private fun SaveTask() {

        val label = txtLabel!!.text.toString()
        val dataType = if (swcUnits!!.isChecked) 1 else 0
        val unit = if (dataType == 1) txtUnits!!.text.toString() else ""
        val hasGoal = swcHasGoal!!.isChecked
        val goal = if (hasGoal) ConvertToInteger(txtGoal!!.text.toString()) else 0
        val goalMinMax = if (hasGoal) spnGoalMinMax!!.selectedItemPosition else 0
        val goalTimeFrame = if (hasGoal) spnGoalTimeFrame!!.selectedItemPosition else 0
        val automationVar : String? = automationData?.ItemId.toString()

        if (label.length > 0 &&
                (dataType == 0 || unit.length > 0) &&
                goal >= 0 &&
                (!hasGoal || goalMinMax >= 1 && goalMinMax <= 3 && goalTimeFrame >= 1 && goalTimeFrame <= 3)) {

            doAsync {
                AddOrUpdateTask(label, dataType, unit, hasGoal, goal, goalMinMax, goalTimeFrame, automationType, automationVar)
                uiThread {
                    activity!!.NavigateFragment(true, TasksPagerFragment.newInstance())
                }
            }
        }
    }

    private fun PrefillTask() {

        if (task != null) {

            txtLabel.setText(task!!.Label)
            swcUnits.isChecked = task!!.DataType == 1
            SetUnitsVisibility(task!!.DataType == 1)
            txtUnits.setText(task!!.Unit)
            swcHasGoal.isChecked = task!!.HasGoal!!
            SetGoalVisibility(task!!.HasGoal!!)
            txtGoal.setText(String.format(Locale.getDefault(), "%d", task!!.Goal))
            val unit = if (task!!.DataType == 0) task!!.Unit else ""
            lblUnits.text = if (unit.length > 0) unit else "items"
            if (task!!.GoalMinMax > 0) {

                val position = Arrays.asList(*activity!!.resources.getStringArray(R.array.spinner_goalMinMax_values)).indexOf(task!!.GoalMinMax.toString())

                spnGoalMinMax.setSelection(position)
            }

            if (task!!.GoalTimeFrame > 0) {

                val position = Arrays.asList(*activity!!.resources.getStringArray(R.array.spinner_goalTimeFrame_values)).indexOf(task!!.GoalTimeFrame.toString())

                spnGoalTimeFrame.setSelection(position)
            }

            if (task!!.AutomationType != null) {

                GetAutomationDetails(AutoTaskType.valueOf(task!!.AutomationType!!)!!, task!!.AutomationVar)
            }
        }
    }

    private fun GetAutomationDetails(automationType: AutoTaskType, automationVar: String?) {

        this.automationType = automationType

        when (automationType) {

            AutoTaskType.AppUsage -> {
                this.automationData = AppUsageFragment.getItemById(activity!!, automationVar!!)
            }
            AutoTaskType.WentTo -> {
                this.automationData = WentToFragment.getItemById(activity!!, automationVar!!)
            }
            else -> {
            }
        }
    }

    private fun AddOrUpdateTask(label: String, dataType: Int, unit: String, hasGoal: Boolean, goal: Int,
                                goalMinMax: Int, goalTimeFrame: Int, automationType: AutoTaskType?, automationVar: String?): Boolean {

        val automationTypeInt = if (automationType != null) automationType.typeId else null

        if (this.TaskId != 0) {

            if (this.activity!!.AppDB().taskDao().getCountByLabelExcludeId(TaskId, label) == 0) {

                task!!.Label = label
                task!!.DataType = dataType
                task!!.Unit = unit
                task!!.HasGoal = hasGoal
                task!!.Goal = goal
                task!!.GoalMinMax = goalMinMax
                task!!.GoalTimeFrame = goalTimeFrame
                task!!.AutomationType = automationTypeInt
                task!!.AutomationVar = automationVar

                var canUpdate = true

                if (!activity!!.application!!.dataStore!!.getAccessToken().isNullOrBlank()) {
                    val taskId = ServiceMethods.uploadTask(activity!!, task!!)
                    canUpdate = !taskId.equals(-1)
                }

                if (canUpdate) {

                    activity!!.AppDB().taskDao().update(task!!)
                    return true
                }
                else
                    return false
            }
        } else {

            if (activity!!.AppDB().taskDao().getCountByLabel(label) == 0) {

                val newTask = Task.CreateItem(-1, label, dataType, unit, hasGoal, goal, goalMinMax, goalTimeFrame, automationTypeInt, automationVar)

                val taskId: Int

                if (!activity!!.application!!.dataStore!!.getAccessToken().isNullOrBlank()) {
                    taskId = ServiceMethods.uploadTask(activity!!, newTask)
                }
                else {
                    taskId = this.activity!!.AppDB().taskDao().minId
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

        tiUnits!!.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun SetGoalVisibility(isVisible: Boolean) {

        lytGoal!!.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun SetUnitsLabel(isUnitsSelected: Boolean, text: String?) {

        val result = if (isUnitsSelected && text != null && text.length > 0) text else ""

        lblUnits!!.text = if (result.length > 0) result else "items"
        txtGoal!!.hint = if (result.length > 0) result else "items"
    }

    override fun onResume() {
        super.onResume()
        SetTitle(R.string.lbl_task_details)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_add_task, menu)
        super.onCreateOptionsMenu(menu, inflater)

        menu.getItem(1).isVisible = TaskId != 0
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.menu_save -> SaveTask()
            R.id.menu_delete ->
            {
                if (task != null) {

                    AlertDialog.Builder(activity!!)
                            .setMessage("Do you really want to delete this task?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes) { _, _ ->
                                doAsync {
                                    activity!!.AppDB().taskDao().delete(task!!.Id)
                                    ServiceMethods.deleteTask(activity!!, task!!.Id)

                                    uiThread {
                                        activity!!.NavigateFragment(false, TasksPagerFragment.newInstance())
                                    }
                                }
                            }
                            .setNegativeButton(android.R.string.no, null).show()
                }
            }
        }
        return true

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

        fun newInstance(automationType: AutoTaskType, automationData: ListItem?): AddTaskFragment {

            val fragment = AddTaskFragment()

            fragment.automationType = automationType
            fragment.automationData = automationData

            return fragment
        }
    }
}