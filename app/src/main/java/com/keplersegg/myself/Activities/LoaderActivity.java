package com.keplersegg.myself.Activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.keplersegg.myself.Async.GetFacebookUser;
import com.keplersegg.myself.Async.ISetUser;
import com.keplersegg.myself.Models.User;
import com.keplersegg.myself.R;

public class LoaderActivity extends MasterActivity implements ISetUser {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        ActionBar ab = master.getActionBar();
        if (ab != null)
            ab.hide();


    }

    private void LoginCheck() {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null)
        {
            if (handleGPlusSignInResult(account)) {

                NavigateToActivity("Main", true);
                return;
            }
        }

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn) {

            new GetFacebookUser().Run(this, accessToken);
        }
        else {

            NavigateToActivity("Login", true);
        }
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

                LoginCheck();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        prgBarLoader.startAnimation(anim);
    }

    @Override
    public void setUser(User user) {

        application.user = user;

        if (user != null) {

            NavigateToActivity("Main", true);
        }
        else {

            NavigateToActivity("Login", true);
        }
    }
}
