package com.keplersegg.myself.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.keplersegg.myself.Fragments.AddTaskFragment;
import com.keplersegg.myself.Fragments.MasterFragment;
import com.keplersegg.myself.Fragments.ProfileFragment;
import com.keplersegg.myself.Fragments.TasksPagerFragment;
import com.keplersegg.myself.R;

public class MainActivity extends AuthActivity {

    DrawerLayout lytDrawerHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNavigationView();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);
        }
    }

    protected void setupNavigationView() {

        lytDrawerHome = findViewById(R.id.lytDrawerHome);
        NavigationView navigationView = findViewById(R.id.navigation);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                menuItem.setChecked(true);
                lytDrawerHome.closeDrawers();

                NavigateFromMenu(menuItem.getItemId());

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        NavigationView navigationView = findViewById(R.id.navigation);

        ImageView imgUserPicture = navigationView.getHeaderView(0).findViewById(R.id.imgUserPicture);
        TextView lblNavUserName = navigationView.getHeaderView(0).findViewById(R.id.lblNavUserName);

        lblNavUserName.setText((application.user != null) ? application.user.FirstName + " " + application.user.LastName : "Guest");

        if (application.user.PictureUrl != null && !application.user.PictureUrl.isEmpty()) {

            Glide.with(this)
                    .load(application.user.PictureUrl)
                    .apply(new RequestOptions()
                    .placeholder(R.drawable.ic_baseline_account_circle_24px)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgUserPicture);
        }
    }

    public void NavigateFromMenu(int menuItemID) {

        switch (menuItemID) {

            case R.id.action_tasks:

                NavigateFragment(true, TasksPagerFragment.Companion.newInstance());
                break;
            case R.id.action_profile:

                NavigateFragment(true, ProfileFragment.Companion.newInstance());
                break;
            case R.id.action_add_task:

                NavigateFragment(true, AddTaskFragment.Companion.newInstance(-1));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            lytDrawerHome.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void NavigateFragment(boolean addToBackStack, MasterFragment fragment) {

        FragmentManager manager = getSupportFragmentManager();
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
}
