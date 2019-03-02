package com.keplersegg.myself.activities

import android.Manifest
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.keplersegg.myself.R
import androidx.core.view.GravityCompat
import com.google.android.material.snackbar.Snackbar
import com.keplersegg.myself.MySelfApplication
import com.keplersegg.myself.fragments.*
import com.keplersegg.myself.helper.AutoTaskType
import com.keplersegg.myself.helper.TaskUpdater
import com.keplersegg.myself.helper.Utils
import com.keplersegg.myself.interfaces.ISyncTasksHost
import com.keplersegg.myself.widgets.TasksWidget
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import android.widget.RemoteViews
import com.keplersegg.myself.Room.Entity.TaskEntry


class MainActivity : AuthActivity(), ISyncTasksHost {

    override fun GetApplication(): MySelfApplication {
        return app
    }

    override fun onSyncTasksSuccess(missingPermissions: List<AutoTaskType>) {

        requestPermissions(missingPermissions)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handleWidgetIntent(intent)

        setupNavigationView()

        setSupportActionBar(toolbar)

        val actionBar = supportActionBar

        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px)
        }
    }

    private fun setupNavigationView() {

        navigation.setNavigationItemSelectedListener { menuItem ->

            menuItem.isChecked = true

            lytDrawerHome.closeDrawers()

            navigateFromMenu(menuItem.itemId)

            true
        }
    }

    override fun onResume() {
        super.onResume()

        val imgUserPicture = navigation.getHeaderView(0).findViewById<ImageView>(R.id.imgUserPicture)
        val lblNavUserName = navigation.getHeaderView(0).findViewById<TextView>(R.id.lblNavUserName)

        lblNavUserName.text = if (app.user != null) app.user!!.FirstName + " " + app.user!!.LastName else "Guest"

        if (app.user != null && app.user!!.PictureUrl != null && !app.user!!.PictureUrl!!.isEmpty()) {

            Glide.with(this)
                    .load(app.user!!.PictureUrl)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.ic_baseline_account_circle_24px)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgUserPicture)
        } else {

            Glide.with(this)
                    .load(R.drawable.ic_baseline_account_circle_24px)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgUserPicture)
        }

        showNewBadgeDialog()
    }

    private fun navigateFromMenu(menuItemID: Int) {

        when (menuItemID) {

            R.id.action_tasks ->
                NavigateFragment(true, TasksPagerFragment.newInstance())
            R.id.action_profile ->
                NavigateFragment(true, ProfileFragment.newInstance())
            R.id.action_add_task ->
                NavigateFragment(true, AddTaskFragment.newInstance(-1))
            R.id.action_add_automated_task ->
                NavigateFragment(true, AutoTaskSelectorFragment.newInstance())
            R.id.action_add_goal ->
                NavigateFragment(true, AddGoalFragment.newInstance(-1, -1))
            R.id.action_goals ->
                NavigateFragment(true, GoalsFragment.newInstance())
            R.id.action_statistics ->
                NavigateFragment(true, StatsMainFragment.newInstance())
        }
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            lytDrawerHome.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(menuItem)
    }

    fun NavigateFragment(addToBackStack: Boolean, fragment: MasterFragment) {

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.setCustomAnimations(
                R.animator.fragment_enter,
                R.animator.fragment_exit,
                R.animator.fragment_pop_enter,
                R.animator.fragment_pop_exit)

        transaction.replace(R.id.fragment_frame, fragment)

        if (addToBackStack)
            transaction.addToBackStack(null)

        transaction.commit()
    }

    fun showSnackbarMessage(message: String) {

        val snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG)

        snackbar.show()
    }

    fun showNewBadgeDialog() {

        val newBadgeId = app.dataStore.popNewBadge()

        if (newBadgeId != null) {

            doAsync {

                val badge = AppDB().userBadgeDao()[newBadgeId]
                val imageResourceId =
                        when (newBadgeId) {
                            1 -> R.drawable.ic_startup
                            2 -> R.drawable.ic_flag
                            else -> R.drawable.ic_trophy
                        }

                uiThread {

                    if (badge != null) {
                        val dialog = DialogNewBadgeFragment()
                        dialog.badgeId = newBadgeId
                        dialog.imageResourceId = imageResourceId
                        dialog.level = badge.Level

                        dialog.show(supportFragmentManager, "")
                    }
                }
            }
        }
    }

    private fun navigateToCallDuration() {
        NavigateFragment(true, AddTaskFragment.newInstance(AutoTaskType.CallDuration, null))
    }

    private fun navigateToAppUsage() {
        NavigateFragment(true, AppUsageFragment.newInstance())
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                when (requestCode) {
                    1 -> {
                        navigateToCallDuration()
                    }
                    2 -> {
                        navigateToAppUsage()
                    }
                    else -> {

                    }
                }
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                when (requestCode) {
                    1 -> {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                            showErrorMessage(getString(R.string.permission_error_phoneCalls))
                        }
                        else {
                            //Never ask again selected, or device policy prohibits the app from having that permission.
                            //TODO: improvement : completely disabling the feature.
                        }
                    }
                    2 -> {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.PACKAGE_USAGE_STATS)) {
                            showErrorMessage(getString(R.string.permission_error_apps))
                        }
                        else {
                            //Never ask again selected, or device policy prohibits the app from having that permission.
                            //TODO: improvement : completely disabling the feature.
                        }
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun handleWidgetIntent(intent: Intent?) {

        if (intent == null) return

        if (intent.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {

            val value = intent.getIntExtra("value", 0)

            doAsync {

                val task = TaskUpdater.GetAppDB(master)
                        .taskEntryDao().getTasks(Utils.getToday()).first()

                TaskUpdater.UpdateEntry(master, task.task!!.Id, Utils.getToday(), task.entry!!.Value + value)

                uiThread {

                }
            }
        }
    }

    public fun updateWidget(items: List<TaskEntry>) {

        val appWidgetManager = AppWidgetManager.getInstance(master)
        val remoteViews = RemoteViews(master.getPackageName(), R.layout.tasks_widget)
        val thisWidget = ComponentName(master, TasksWidget::class.java)

        remoteViews.removeAllViews(R.id.lytWidgetContainer)

        val filteredItems = items.sortedBy { i -> i.task!!.AutomationType }
        for (i in 0..items.size) {

            if (i > 2) break

            val componentWidget = RemoteViews(master.getPackageName(), R.layout.component_widget_task)
            //Set the text of the TextView that is inside the above specified listEntryLayout RemoteViews
            componentWidget.setTextViewText(R.id.lblTask, items[i].task!!.Label)

            componentWidget.setTextViewText(R.id.lblValue, items[i].entry!!.Value.toString() + " " + items[i].task!!.Unit)

            if (items[i].task!!.DataType == 1)
                componentWidget.setViewVisibility(R.id.imgDone, View.GONE)
            else
                componentWidget.setViewVisibility(R.id.lblValue, View.GONE)

            //Add the new remote view to the parent/containing Layout object
            remoteViews.addView(R.id.lytWidgetContainer, componentWidget)
        }

        /*
        remoteViews.setOnClickPendingIntent(R.id.imgMinus,
                TasksWidget.getPendingIntent(master, -1))
        remoteViews.setOnClickPendingIntent(R.id.imgPlus,
                TasksWidget.getPendingIntent(master, 1))
*/
        appWidgetManager.updateAppWidget(thisWidget, remoteViews)
/*
        appWidgetManager.updateAppWidget(thisWidget, remoteViews)

        val man = AppWidgetManager.getInstance(this)

        val ids = man.getAppWidgetIds(
                ComponentName(this, TasksWidget::class.java))

        val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        updateIntent.putExtra("label", "Coffee")
        sendBroadcast(updateIntent)
        */
    }
}
