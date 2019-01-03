package com.keplersegg.myself.fragments


import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.keplersegg.myself.helper.AutoTaskType
import com.keplersegg.myself.R
import kotlinx.android.synthetic.main.fragment_auto_task_selector.*

class AutoTaskSelectorFragment : MasterFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layout = R.layout.fragment_auto_task_selector
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lytCallDuration.setOnClickListener { selectAutoTaskType(AutoTaskType.CallDuration) }
        lytAppUsage.setOnClickListener { selectAutoTaskType(AutoTaskType.AppUsage) }
        lytWentTo.setOnClickListener { selectAutoTaskType(AutoTaskType.WentTo) }
    }

    override fun onResume() {
        super.onResume()
        SetTitle(R.string.lbl_auto_task_selector)
    }

    private fun selectAutoTaskType(type: AutoTaskType) {

        when (type) {

            AutoTaskType.CallDuration -> {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    // Ask for permision
                    ActivityCompat.requestPermissions(activity, Array(1) { Manifest.permission.READ_CALL_LOG}, 1)
                }
                activity.NavigateFragment(true, AddTaskFragment.newInstance(AutoTaskType.CallDuration, null))
            }
            AutoTaskType.AppUsage -> {

                if (!checkForPermission(activity))
                {
                    //TODO: show a message here.
                    startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                }

                if (!checkForPermission(activity)) {
                    //TODO: show a message here
                    return
                }
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.PACKAGE_USAGE_STATS)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    // Ask for permision
                    ActivityCompat.requestPermissions(activity, Array(1) { Manifest.permission.PACKAGE_USAGE_STATS }, 2)

                }

                activity.NavigateFragment(true, AppUsageFragment.newInstance())
            }
            AutoTaskType.WentTo -> activity.NavigateFragment(true, WentToFragment.newInstance())
        }
    }

    private fun checkForPermission(context: Context): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.packageName)
        return mode == AppOpsManager.MODE_ALLOWED
    }

    companion object {

        fun newInstance(): AutoTaskSelectorFragment {
            return AutoTaskSelectorFragment()
        }
    }
}
