package com.keplersegg.myself.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.keplersegg.myself.Async.GetFacebookUser;
import com.keplersegg.myself.Async.ILoginHost;
import com.keplersegg.myself.Async.ISetUser;
import com.keplersegg.myself.Async.ISyncTasksHost;
import com.keplersegg.myself.Async.LoginTask;
import com.keplersegg.myself.Async.SyncTasks;
import com.keplersegg.myself.Helper.TokenType;
import com.keplersegg.myself.Models.User;
import com.keplersegg.myself.R;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class LoginActivity extends AuthActivity implements ISetUser, ILoginHost, ISyncTasksHost {

    private LoginManager fbLoginManager;
    private CallbackManager fbCallbackManager;
    private static final int RC_SIGN_IN = 430;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_login);

        Glide.with(this)
                .load(R.drawable.login_background)
                .apply(RequestOptions.centerCropTransform())
                .into((ImageView)findViewById(R.id.imgLoginBackground));

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

                //application.dataStore.setAccessToken("194044032133214245110100013098164116206239065216108230151152227093182051129179034209198215059120005198162060090156001124114155022206198130022107007035033187205131099148147216228217003192152060");
                NavigateToActivity("Main", true);
            }
        });

        btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fbLoginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile", "user_birthday"));

                /*
                setToken(TokenType.Facebook, "EAAKGheh3UEEBAFnKaQGbGO2bG6r0zfRZBuk8Seq44Bk7a1hRrkrjpsOmAfK4QvA0UVHtmaoden4rkb4VlmiOsKjQMpuLbkPjWorzBvBgI26k0xSOYNMyfnfX3L3UE6KrsiTtK95N6c7U05qZBw5y0OZChAC4TkGcLQMbgaoO9t210qMgmBqs5oLEu8nptenUGQMZBHzwvMbv0xnvfDqxn3xB2nJPcPwsTQhaV4d3HgZDZD");

                User user = new User();
                user.Email = "open_ydzjppo_user@tfbnw.net";
                user.FirstName = "Jacob";
                user.LastName = "Crafty";

                setUser(user);
                */

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
                if (handleGPlusSignInResult(result.getSignInAccount())) {

                    new LoginTask(this).Run(TokenType.Google, application.dataStore.getGoogleToken(), application.user.Email);
                }
            }
            else {
                showErrorMessage(Objects.requireNonNull(result.getStatus().getStatusMessage()));
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
            fbCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void setUser(User user) {

        application.user = user;

        if (user != null) {

            new LoginTask(this).Run(TokenType.Facebook, application.dataStore.getFacebookToken(), application.user.Email);
        }
        else {

            application.dataStore.setAccessToken(null);
            showErrorMessage("Cannot authenticate via Facebook");
        }
    }

    @Override
    public void setToken(TokenType tokenType, String token) {

        super.setToken(tokenType, token);
    }

    @Override
    public void onLoginSuccess() {

        new SyncTasks(this).execute();
    }

    @Override
    public void onLoginError(@NonNull String message) {

        application.user = null;
        showErrorMessage(message);
    }

    @Override
    public void setAccessToken(@NotNull String token) {

        application.dataStore.setAccessToken(token);
        new SyncTasks(this).execute();
    }

    @Override
    public void onSyncTasksSuccess() {

        NavigateToActivity("Main", true);
    }
}
