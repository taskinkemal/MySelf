package com.keplersegg.myself.Async;

import android.support.annotation.NonNull;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.keplersegg.myself.Models.User;

public class SignOut {

    public void Run(final ISignOut activity) {

        User user = activity.GetUser();

        if (user != null && user.FacebookToken != null) {
            new GraphRequest(user.FacebookToken, "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
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

    private void GoogleSignOut(final ISignOut activity) {

        Auth.GoogleSignInApi.signOut(activity.GetGoogleApiClient()).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

                FinalizeSignOut(activity);
            }
        });
    }

    private void FinalizeSignOut(final ISignOut activity) {

        activity.onSignOut();
    }
}
