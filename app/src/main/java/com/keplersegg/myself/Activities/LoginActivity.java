package com.keplersegg.myself.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.keplersegg.myself.Async.GetFacebookUser;
import com.keplersegg.myself.Async.ISetUser;
import com.keplersegg.myself.Models.User;
import com.keplersegg.myself.R;

import java.util.Arrays;

public class LoginActivity extends AuthActivity
        implements
        ISetUser {

    private CallbackManager fbCallbackManager;
    private LoginManager fbLoginManager;
    private static final int RC_SIGN_IN = 430;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_login);

        fbLoginManager = com.facebook.login.LoginManager.getInstance();
        fbCallbackManager = CallbackManager.Factory.create();
        fbLoginManager.registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // here write code When Login successfully

                new GetFacebookUser().Run(LoginActivity.this, loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
                // here write code when get error
            }
        });


        Button btnLoginFacebook = findViewById(R.id.btnLoginFacebook);
        Button btnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        TextView txtContinueWithoutAccount = findViewById(R.id.txtContinueWithoutAccount);

        txtContinueWithoutAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavigateToActivity("Main", true);
            }
        });

        btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbLoginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile", "user_birthday"));
            }
        });

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleApiClient.clearDefaultAccountAndReconnect();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                mGoogleApiClient.connect();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                handleGPlusSignInResult(result.getSignInAccount());
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
            fbCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void setUser(User user) {

        application.user = user;

        NavigateToActivity("Main", true);
    }
}
