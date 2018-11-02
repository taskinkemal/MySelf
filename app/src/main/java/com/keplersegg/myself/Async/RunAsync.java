package com.keplersegg.myself.Async;

import android.os.AsyncTask;

import com.keplersegg.myself.Activities.MasterActivity;

import java.util.concurrent.Callable;

public class RunAsync extends AsyncMaster {

    public RunAsync(MasterActivity activity) {
        super(activity);
    }

    public void Execute(Callable<Boolean> callable) {

        new RunAsyncInternal().execute(callable);
    }

    public void OnSuccess() { }

    private class RunAsyncInternal extends AsyncTask<Callable<Boolean>, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Callable<Boolean>... params) {

            try {
                return params[0].call();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result)
                OnSuccess();
        }
    }
}
