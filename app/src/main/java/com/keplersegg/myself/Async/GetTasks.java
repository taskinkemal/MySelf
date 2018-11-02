package com.keplersegg.myself.Async;

import android.os.AsyncTask;

import com.keplersegg.myself.Activities.MasterActivity;
import com.keplersegg.myself.Room.Entity.Task;

import java.util.List;

public class GetTasks extends AsyncMaster {

    public GetTasks(MasterActivity activity) {
        super(activity);
    }

    public void Execute() {

        new GetTasksAsync().execute();
    }

    public void OnSuccess(List<Task> list) { }

    private class GetTasksAsync extends AsyncTask<Void, Void, List<Task>> {

        @Override
        protected List<Task> doInBackground(Void... params) {

            List<Task> list = activity.AppDB().taskDao().getAll();

            if (list.isEmpty())
            {
                Task newItem1 = Task.CreateItem("Exercise", 0, null, false, 0, 0, 0);
                Task newItem2 = Task.CreateItem("Study", 0, null, false, 0, 0, 0);
                Task newItem3 = Task.CreateItem("Coffee", 1, "cup(s)", false, 0, 0, 0);

                activity.AppDB().taskDao().insertAll(newItem1, newItem2, newItem3);

                list = activity.AppDB().taskDao().getAll();
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<Task> result) {

            OnSuccess(result);
        }
    }
}
