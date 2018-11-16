package com.keplersegg.myself.Fragments


import android.content.Context
import android.net.wifi.WifiConfiguration
import android.os.Bundle
import com.keplersegg.myself.Models.ListItem
import kotlinx.android.synthetic.main.fragment_went_to.*

import com.keplersegg.myself.R
import android.net.wifi.WifiManager
import androidx.recyclerview.widget.RecyclerView
import com.keplersegg.myself.Helper.AutoTaskType


class WentToFragment : ListFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layout = R.layout.fragment_went_to
    }

    override fun onResume() {
        super.onResume()
        SetTitle(R.string.autotask_wentTo)
    }

    override fun showImages(): Boolean { return false }

    override fun getRecyclerView(): RecyclerView { return rcylNetworks }

    override fun getList(): List<ListItem> {

        val list: ArrayList<ListItem> = ArrayList()

        val configs = getConfigList(activity!!)

        configs?.forEach { c -> list.add(toListItem(c)) }

        return list
    }

    override fun onSelectListItem(item: ListItem) {

        activity!!.NavigateFragment(true, AddTaskFragment.newInstance(AutoTaskType.WentTo, item))
    }

    companion object {

        fun newInstance(): WentToFragment {
            return WentToFragment()
        }

        fun getItemById(context: Context, id: String): ListItem? {

            val configs = getConfigList(context)

            val wifiConfig = configs?.firstOrNull { p -> p.networkId == id.toInt() }

            if (wifiConfig != null)
                return toListItem(wifiConfig)
            else
                return null
        }

        private fun getConfigList(context: Context): MutableList<WifiConfiguration>? {

            val wm = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            return wm.configuredNetworks
        }

        private fun toListItem(wifiConfig: WifiConfiguration): ListItem {

            val item = ListItem()
            item.ItemId = wifiConfig.networkId
            item.Label = wifiConfig.SSID

            return item
        }
    }
}
