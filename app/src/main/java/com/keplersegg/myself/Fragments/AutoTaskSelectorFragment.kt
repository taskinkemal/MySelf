package com.keplersegg.myself.Fragments


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.keplersegg.myself.Helper.AutoTaskType
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
                if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_CALL_LOG)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    // Ask for permision
                    ActivityCompat.requestPermissions(activity!!, Array(1) { Manifest.permission.READ_CALL_LOG}, 1)
                }
                activity!!.NavigateFragment(true, AddTaskFragment.newInstance(AutoTaskType.CallDuration, null))
            }
            AutoTaskType.AppUsage -> activity!!.NavigateFragment(true, AppUsageFragment.newInstance())
            AutoTaskType.WentTo -> activity!!.NavigateFragment(true, WentToFragment.newInstance())
        }
    }

    companion object {

        fun newInstance(): AutoTaskSelectorFragment {
            return AutoTaskSelectorFragment()
        }
    }
}
