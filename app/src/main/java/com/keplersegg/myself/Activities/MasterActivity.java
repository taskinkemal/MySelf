package com.keplersegg.myself.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.keplersegg.myself.Async.IHttpProvider;
import com.keplersegg.myself.Helper.TokenType;
import com.keplersegg.myself.Interfaces.IErrorMessage;
import com.keplersegg.myself.Models.User;
import com.keplersegg.myself.MySelfApplication;
import com.keplersegg.myself.R;
import com.keplersegg.myself.Room.AppDatabase;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressLint("Registered")
public class MasterActivity extends AppCompatActivity
    implements
        GoogleApiClient.OnConnectionFailedListener,
        IHttpProvider,
        IErrorMessage
{

    public MySelfApplication application;
    public MasterActivity master;
    public GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        master = this;
        application = (MySelfApplication) getApplication();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(GetColor(R.color.colorPrimary));

        initializeGPlusSettings();
    }

    public int GetColor(int id) { return ContextCompat.getColor(this, id); }

    @NonNull
    public AppDatabase AppDB() { return AppDatabase.getAppDatabase(this); }

    public void NavigateToActivity(String activityName, boolean clearTop) {

        Intent i = null;

        switch (activityName) {

            case "Main":

                i = new Intent(application, MainActivity.class);
                break;

            case "Login":

                i = new Intent(application, LoginActivity.class);
                break;

        default:
        break;
    }

        if (i != null)
            NavigateToActivity(i, clearTop);
    }

    protected void NavigateToActivity(Intent i, boolean clearTop) {

        if (clearTop) // ana sayfadan geri dön'e tıklandığında çıkış yapma diyaloğu gelmesi için.
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        if (clearTop) // ana sayfadan geri dön'e tıklandığında çıkış yapma diyaloğu gelmesi için.
            finish();
    }

    private void initializeGPlusSettings(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.google_client_id))
                //.requestServerAuthCode(getString(R.string.google_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
    }

    protected boolean handleGPlusSignInResult(GoogleSignInAccount account) {

        setToken(TokenType.Google, account.getIdToken());

        application.user = new User();
        application.user.Email = account.getEmail();
        application.user.FirstName = account.getGivenName();
        application.user.LastName = account.getFamilyName();
        Uri pictureUri = account.getPhotoUrl();
        if (pictureUri != null)
            application.user.PictureUrl = pictureUri.toString();

        return true;
    }

    public void setToken(TokenType tokenType, String token) {

        switch (tokenType) {

            case MySelf:
                application.dataStore.setAccessToken(token);
                break;

            case Facebook:
                application.dataStore.setFacebookToken(token);
                break;

            case Google:
                application.dataStore.setGoogleToken(token);
                break;

            default:
                break;
        }
    }

    @NonNull
    public ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Nullable
    public String getAccessToken() {
        return application.dataStore.getAccessToken();
    }

    @Nullable
    public String getDeviceId() { return application.dataStore.getRegisterID(); }

    @Override
    public void showErrorMessage(@NonNull final String message) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MasterActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void logException(@NotNull Exception exception, @NotNull String message) {

        //CrashLogger.AddExceptionLog(message, exc);
        showErrorMessage(message);
    }
}
