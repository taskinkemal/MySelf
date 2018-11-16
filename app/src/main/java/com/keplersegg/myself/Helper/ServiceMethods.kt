package com.keplersegg.myself.Helper

import com.google.gson.GsonBuilder
import com.keplersegg.myself.Interfaces.IHttpProvider
import com.keplersegg.myself.Room.Entity.Entry
import com.keplersegg.myself.Room.Entity.Task
import org.json.JSONObject

object ServiceMethods {

    fun uploadEntry(provider: IHttpProvider, entry: Entry) {

        val jsonParams = JSONObject()
        jsonParams.put("Day", entry.Day)
        jsonParams.put("TaskId", entry.TaskId)
        jsonParams.put("Value", entry.Value)
        //jsonParams.put("ModificationDate", entry.ModificationDate)

        HttpClient.send(provider, "entries", "post", jsonParams)
    }

    fun uploadTask(provider: IHttpProvider, task: Task): Int {

        val jsonParams = JSONObject()
        if (task.Id > 0)
            jsonParams.put("Id", task.Id)
        jsonParams.put("Label", task.Label)
        jsonParams.put("DataType", task.DataType)
        jsonParams.put("Unit", task.Unit)
        jsonParams.put("HasGoal", task.HasGoal)
        jsonParams.put("GoalMinMax", task.GoalMinMax)
        jsonParams.put("Goal", task.Goal)
        jsonParams.put("GoalTimeFrame", task.GoalTimeFrame)
        jsonParams.put("AutomationType", task.AutomationType)
        jsonParams.put("AutomationVar", task.AutomationVar)

        val result = HttpClient.send(provider, "tasks", "post", jsonParams)

        if (result != null && result.has("Value")) {

            return result.getInt("Value")
        }
        else {
            return -1
        }
    }

    fun deleteTask(provider: IHttpProvider, taskId: Int) {

        HttpClient.send(provider, "tasks/" + taskId, "delete", null)
    }

    fun getTasksFromService(provider: IHttpProvider): List<Task>? {

        val result = HttpClient.send(provider, "tasks", "get", null)

        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()

        if (result == null || !result.has("Items")) {

            return null
        }

        val arrTasks = gson.fromJson<Array<Task>>(result.getJSONArray("Items").toString(), Array<Task>::class.java)

        return arrTasks.toList()
    }

    fun getEntriesFromService(provider: IHttpProvider): List<Entry>? {

        val end = Utils.getToday()
        val start = end - 5

        val result = HttpClient.send(provider, "entries?start=" + start + "&end=" + end, "get", null)

        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()

        if (result == null || !result.has("Items")) {

            return null
        }

        val arrEntries = gson.fromJson<Array<Entry>>(result.getJSONArray("Items").toString(), Array<Entry>::class.java)

        return arrEntries.toList()
    }
}