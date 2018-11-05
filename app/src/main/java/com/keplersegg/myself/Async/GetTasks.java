package com.keplersegg.myself.Async;

import android.os.AsyncTask;

import com.keplersegg.myself.Activities.MasterActivity;
import com.keplersegg.myself.Room.Entity.Task;
import com.keplersegg.myself.Room.Entity.TaskEntry;

import java.util.List;

public class GetTasks extends AsyncMaster {

    public GetTasks(MasterActivity activity) {
        super(activity);
    }

    public void Execute(int day) {

        new GetTasksAsync().execute(day);
    }

    public void OnSuccess(List<TaskEntry> list) { }

    private class GetTasksAsync extends AsyncTask<Integer, Void, List<TaskEntry>> {

        @Override
        protected List<TaskEntry> doInBackground(Integer... params) {

            int day = params[0];

            List<Task> list = activity.AppDB().taskDao().getAll();

            if (list.isEmpty())
            {
                Task newItem1 = Task.CreateItem("Exercise", 0, null, false, 0, 0, 0);
                Task newItem2 = Task.CreateItem("Study", 0, null, false, 0, 0, 0);
                Task newItem3 = Task.CreateItem("Coffee", 1, "cup(s)", false, 0, 0, 0);

                activity.AppDB().taskDao().insertAll(newItem1, newItem2, newItem3);
            }

            return activity.AppDB().taskEntryDao().getTasks(day);
        }

        @Override
        protected void onPostExecute(List<TaskEntry> result) {

            OnSuccess(result);
        }
    }
}
