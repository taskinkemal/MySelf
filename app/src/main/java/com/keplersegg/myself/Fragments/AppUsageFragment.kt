package com.keplersegg.myself.Fragments


import android.os.Bundle

import com.keplersegg.myself.R
import com.keplersegg.myself.Models.ListItem
import kotlinx.android.synthetic.main.fragment_app_usage.*
import android.content.pm.PackageManager
import android.support.v7.widget.RecyclerView

class AppUsageFragment : ListFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layout = R.layout.fragment_app_usage
    }

    override fun showImages(): Boolean { return true }

    override fun getRecyclerView(): RecyclerView { return rcylApps }

    override fun getList(): List<ListItem> {

        val list: ArrayList<ListItem> = ArrayList()

        val pm = activity!!.getPackageManager()
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        for (appInfo in packages) {

            if (appInfo.sourceDir.startsWith("/data/app/") && pm.getLaunchIntentForPackage(appInfo.packageName) != null) {

                val item = ListItem()
                item.ItemId = appInfo.uid
                item.ImageDrawable = pm.getApplicationIcon(appInfo)
                item.Label = pm.getApplicationLabel(appInfo).toString()
                list.add(item)
            }
        }

        list.sortBy { item -> item.Label.toLowerCase() }
        return list
    }

    override fun onResume() {
        super.onResume()
        SetTitle(R.string.autotask_appUsage)
    }

    override fun onSelectListItem(itemId: Int) {
        //TODO:
    }

    companion object {

        fun newInstance(): AppUsageFragment {
            return AppUsageFragment()
        }
    }
}
