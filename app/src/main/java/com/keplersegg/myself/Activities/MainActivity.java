package com.keplersegg.myself.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.keplersegg.myself.Fragments.ProfileFragment;
import com.keplersegg.myself.Fragments.TasksFragment;
import com.keplersegg.myself.R;

public class MainActivity extends MasterActivity {

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
            //actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_white_24dp);
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

    public void NavigateFromMenu(int menuItemID) {

        switch (menuItemID) {

            case R.id.action_tasks:

                NavigateFragment(true, TasksFragment.newInstance());
                break;
            case R.id.action_profile:

                NavigateFragment(true, ProfileFragment.newInstance());
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
}
