package com.keplersegg.myself.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keplersegg.myself.Activities.MasterActivity;
import com.keplersegg.myself.R;

public class ProfileFragment extends MasterFragment {

    private TextView lblDummy;

    public ProfileFragment() { }

    public static ProfileFragment newInstance() { return new ProfileFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {

            rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            activity = (MasterActivity) rootView.getContext();

            lblDummy = rootView.findViewById(R.id.lblDummy);
        }



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SetTitle(R.string.lbl_profile);

        lblDummy.setText(activity.application.dummyData);
    }

}
