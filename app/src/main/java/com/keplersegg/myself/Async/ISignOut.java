package com.keplersegg.myself.Async;

import com.google.android.gms.common.api.GoogleApiClient;
import com.keplersegg.myself.Models.User;

public interface ISignOut {

    void onSignOut();

    User GetUser();

    GoogleApiClient GetGoogleApiClient();
}
