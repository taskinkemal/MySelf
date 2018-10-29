package com.keplersegg.myself.Activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.keplersegg.myself.R;

public class LoaderActivity extends MasterActivity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        ActionBar ab = master.getActionBar();
        if (ab != null)
            ab.hide();

//        OptionalPendingResult opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//
//            Result result = opr.get();
//            handleGPlusSignInResult(result);
//        } else {
//            opr.setResultCallback(new ResultCallback() {
//                @Override
//                public void onResult(@NonNull Result result) {
//                    handleGPlusSignInResult(result);
//                }
//            });
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ProgressBar prgBarLoader = findViewById(R.id.prgBarLoader);

        FadeinView(prgBarLoader);
    }

    private void FadeinView(ProgressBar prgBarLoader) {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.loader);


        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                OnCreateInternal();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        prgBarLoader.startAnimation(anim);
    }
}
