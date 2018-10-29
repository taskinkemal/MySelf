package com.keplersegg.myself.Async;

import com.keplersegg.myself.Activities.MasterActivity;

public class AsyncMaster {

    MasterActivity activity;

    AsyncMaster(MasterActivity activity) {

        this.activity = activity;
    }

    public void OnError(String errPhrase) { }

    public void OnConnectionError() { }
}

