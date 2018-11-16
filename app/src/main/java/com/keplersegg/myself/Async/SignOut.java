package com.keplersegg.myself.Async;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.keplersegg.myself.Interfaces.ISignOut;
import com.keplersegg.myself.Models.User;

public class SignOut {

    public void Run(final ISignOut activity) {

        User user = activity.GetUser();

        if (user != null && user.getFacebookToken() != null) {
            new GraphRequest(user.getFacebookToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
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

        if (activity.GetGoogleSignInClient().asGoogleApiClient().isConnected()) {
            activity.GetGoogleSignInClient().signOut();
        }

        FinalizeSignOut(activity);
    }

    private void FinalizeSignOut(final ISignOut activity) {

        activity.onSignOut();
    }
}
