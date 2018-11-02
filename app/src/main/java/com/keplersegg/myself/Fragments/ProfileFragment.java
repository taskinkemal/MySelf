package com.keplersegg.myself.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.keplersegg.myself.Activities.MasterActivity;
import com.keplersegg.myself.Async.ISignOut;
import com.keplersegg.myself.Async.SignOut;
import com.keplersegg.myself.Models.User;
import com.keplersegg.myself.R;

public class ProfileFragment extends MasterFragment
    implements ISignOut {

    private TextView lblUserName;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {

            rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            activity = (MasterActivity) rootView.getContext();

            lblUserName = rootView.findViewById(R.id.lblUserName);
            Button btnLogout = rootView.findViewById(R.id.btnLogout);

            btnLogout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                new SignOut().Run(ProfileFragment.this);
                                            }
                                        }
            );
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SetTitle(R.string.lbl_profile);

        lblUserName.setText(activity.application.user != null ? activity.application.user.FirstName + " " + activity.application.user.LastName : "Guest");
    }

    @Override
    public void onSignOut() {
        activity.NavigateToActivity("Login", true);
    }

    @Override
    public User GetUser() {
        return activity.application.user;
    }

    @Override
    public GoogleApiClient GetGoogleApiClient() {
        return activity.mGoogleApiClient;
    }
}
