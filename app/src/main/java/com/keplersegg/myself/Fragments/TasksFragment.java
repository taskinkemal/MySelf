package com.keplersegg.myself.Fragments;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keplersegg.myself.Activities.MainActivity;
import com.keplersegg.myself.Adapters.TasksAdapter;
import com.keplersegg.myself.Async.GetTasks;
import com.keplersegg.myself.R;
import com.keplersegg.myself.Room.Entity.Task;

import java.util.ArrayList;
import java.util.List;


public class TasksFragment extends MasterFragment {

    private List<Task> tasks = null;
    private TasksAdapter adapter = null;

    public TasksFragment() { }

    public static TasksFragment newInstance() { return new TasksFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {

            rootView = inflater.inflate(R.layout.fragment_tasks, container, false);
            activity = (MainActivity) rootView.getContext();

            RecyclerView rcylTasks = rootView.findViewById(R.id.rcylTasks);

            tasks = new ArrayList<>();
            adapter = new TasksAdapter(activity, tasks);
            rcylTasks.setAdapter(adapter);
            rcylTasks.setLayoutManager(new LinearLayoutManager(activity));
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SetTitle(R.string.lbl_tasks);

        new MyGetTasks().Execute();
    }

    private class MyGetTasks extends GetTasks {

        MyGetTasks() {
            super(TasksFragment.this.activity);
        }

        @Override
        public void OnSuccess(List<Task> items) {

            tasks.clear();
            tasks.addAll(items);

            adapter.notifyDataSetChanged();
        }

        @Override
        public void OnError(String errPhrase) {

            //adapter.notifyDataSetChanged();
        }

        @Override
        public void OnConnectionError() {

            //adapter.notifyDataSetChanged();
        }
    }
}
