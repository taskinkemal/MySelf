package com.keplersegg.myself.Async;

import android.support.annotation.NonNull;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class SignOut {

    public void Run(final ISignOut activity) {

        if (activity.GetUser() != null) {
            new GraphRequest(activity.GetUser().FacebookToken, "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                    .Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {

                    LoginManager.getInstance().logOut();

                    GoogleSignOut(activity);
                }
            }).executeAsync();
        }
        else {
            GoogleSignOut(activity);
        }
    }

    private void GoogleSignOut(final ISignOut activity)
    {
        Auth.GoogleSignInApi.signOut(activity.GetGoogleApiClient()).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

                activity.onSignOut();
            }
        });
    }
}
