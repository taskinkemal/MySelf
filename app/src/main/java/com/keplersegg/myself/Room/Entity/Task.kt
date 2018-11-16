package com.keplersegg.myself.Room.Entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Task {

    @PrimaryKey
    var Id: Int = 0
    var Label: String = ""
    var DataType: Int = 0
    var Unit: String = ""
    var HasGoal: Boolean? = null
    var GoalMinMax: Int = 0
    var Goal: Int = 0
    var GoalTimeFrame: Int = 0
    var AutomationType: Int? = null
    var AutomationVar: String? = null

    companion object {

        fun CreateItem(id: Int, label: String, dataType: Int, unit: String, hasGoal: Boolean?,
                       goal: Int, goalMinMax: Int, goalTimeFrame: Int,
                       automationType: Int?, automationVar: String?): Task {

            val m = Task()

            m.Id = id
            m.Label = label
            m.DataType = dataType
            m.Unit = unit
            m.HasGoal = hasGoal
            m.Goal = goal
            m.GoalMinMax = goalMinMax
            m.GoalTimeFrame = goalTimeFrame
            m.AutomationType = automationType
            m.AutomationVar = automationVar

            return m
        }
    }
}
