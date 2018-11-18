package com.keplersegg.myself.Async

import android.os.AsyncTask
import com.keplersegg.myself.Helper.ServiceMethods
import com.keplersegg.myself.Interfaces.ISyncTasksHost

import com.keplersegg.myself.Room.Entity.Task


open class SyncTasks(private var activity: ISyncTasksHost) : AsyncTask<Void, Void, Boolean>() {

    override fun doInBackground(vararg params: Void?): Boolean {

        val list = ServiceMethods.getTasksFromService(activity)

        if (list != null) {

            // successfully connected to the server and retrieved the list.

            for (i in 0 until list.size) {

                upsertTask(list[i])
            }

            val listLocal = activity.AppDB().taskDao().all

            for (i in 0 until listLocal.size) {

                val foundTask = list.firstOrNull { t -> t.Id == listLocal[i].Id }
                if (foundTask == null) {

                    if (listLocal[i].Id < 0) {

                        // newly added task without internet connection

                        val taskId = ServiceMethods.uploadTask(activity, listLocal[i])
                        if (taskId > 0) /* check if uploaded successfully */ {
                            activity.AppDB().taskDao().updateId(taskId, listLocal[i].Id)
                            listLocal[i].Id = taskId
                        }
                    }
                    else {

                        // shouldn't come here
                        ServiceMethods.uploadTask(activity, listLocal[i])
                    }
                }
                else {

                }
            }

            val entries = ServiceMethods.getEntriesFromService(activity)

            if (entries != null) {

                val entriesLocal = activity.AppDB().entryDao().all

                for (i in 0 until entriesLocal.size) {

                    val entry = entries.firstOrNull { t ->
                        t.Day == entriesLocal[i].Day &&
                                t.TaskId == entriesLocal[i].TaskId}

                    if (entry != null) {

                        if (entry.ModificationDate > entriesLocal[i].ModificationDate) {

                            entriesLocal[i].Value = entry.Value
                            activity.AppDB().entryDao().update(entriesLocal[i])
                        }
                        else if (entry.ModificationDate < entriesLocal[i].ModificationDate) {

                            ServiceMethods.uploadEntry(activity, entriesLocal[i])
                        }
                    }
                    else {

                        ServiceMethods.uploadEntry(activity, entriesLocal[i])
                    }
                }

                for (i in 0 until entries.size) {

                    if (!entriesLocal.any { t ->
                                t.Day == entries[i].Day &&
                                t.TaskId == entries[i].TaskId}) {

                        activity.AppDB().entryDao().insert(entries[i])
                    }
                }
            }
        }
        else {

            // can be disconnected or not logged in.

            val listLocal = activity.AppDB().taskDao().all

            if (listLocal.isEmpty()) {

                val newItem1 = Task.CreateItem(-2, "Exercise", 0, "", false, 0, 0, 0, null,null)
                val newItem2 = Task.CreateItem(-3, "Study", 0, "", false, 0, 0, 0, null,null)
                val newItem3 = Task.CreateItem(-4, "Coffee", 1, "cup(s)", false, 0, 0, 0, null,null)

                activity.AppDB().taskDao().insert(newItem1)
                activity.AppDB().taskDao().insert(newItem2)
                activity.AppDB().taskDao().insert(newItem3)
            }
        }

        return true
    }

    override fun onPostExecute(result: Boolean) {

        activity.onSyncTasksSuccess()
    }

    private fun upsertTask(task: Task) {

        val rowsAffected = activity.AppDB().taskDao().update(task)
        if (rowsAffected == 0) {
            activity.AppDB().taskDao().insert(task)
        }
    }
}