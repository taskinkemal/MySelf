package com.keplersegg.myself.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.keplersegg.myself.Fragments.MasterFragment;
import com.keplersegg.myself.MySelfApplication;
import com.keplersegg.myself.R;
import com.keplersegg.myself.Room.AppDatabase;

public class MasterActivity extends AppCompatActivity {

    public MySelfApplication application;
    public MasterActivity master;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        master = this;
        application = (MySelfApplication) getApplication();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(GetColor(R.color.colorPrimary));

        if (!(this instanceof LoaderActivity)) // because LoaderActivity will do it itself after animations.
            OnCreateInternal();
    }

    public int GetColor(int id) { return ContextCompat.getColor(this, id); }

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

    public void NavigateFragment(boolean addToBackStack, MasterFragment fragment) {

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(
                R.animator.fragment_enter,
                R.animator.fragment_exit,
                R.animator.fragment_pop_enter,
                R.animator.fragment_pop_exit);

        transaction.replace(R.id.fragment_frame, fragment);

        if (addToBackStack)
            transaction.addToBackStack(null);

        transaction.commit();
    }

    void OnCreateInternal() {

        //RetrieveRegisterID();

        LoginCheck();
    }

    private void LoginCheck() {

//        String accessToken = app.GetAccessToken();
//        User user = app.user;
//
//        if (user == null) {
//            if (accessToken != null && !accessToken.isEmpty()) {
//
//                // accesstoken shared preferences'ta dolu ama user bilgisi boş: uygulama yeni açılmış olmalı.
//                new MyGetUserTask().Execute();
//            }
//            else if (master instanceof LoaderActivity) // login durumda değilse ve uygulama açılmışsa direk redirect.
//                NavigateToActivity("Login", true);
//        }
//        else if (master instanceof LoaderActivity) // kullanıcı login durumda ve uygulama yeni açılmışsa direk redirect.
//            NavigateToActivity("Home", true);

        if (master instanceof LoaderActivity) // login durumda değilse ve uygulama açılmışsa direk redirect.
            NavigateToActivity("Login", true);
    }
}
