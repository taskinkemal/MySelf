package com.keplersegg.myself.Fragments;

import android.app.Fragment;
import android.view.View;

import com.keplersegg.myself.Activities.MainActivity;

public class MasterFragment extends Fragment {

    public MainActivity activity = null;
    protected View rootView = null;

    public void SetTitle(int id) {

        activity.setTitle(id);
    }

    public void SetTitle(String title) {

        activity.setTitle(title);
    }
}
