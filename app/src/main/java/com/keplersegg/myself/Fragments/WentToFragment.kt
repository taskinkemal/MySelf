package com.keplersegg.myself.Fragments


import android.content.Context
import android.os.Bundle
import com.keplersegg.myself.Models.ListItem
import kotlinx.android.synthetic.main.fragment_went_to.*

import com.keplersegg.myself.R
import android.net.wifi.WifiManager
import android.support.v7.widget.RecyclerView


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

        val wm = activity!!.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val configs = wm.configuredNetworks

        for (c in configs) {

            val item = ListItem()
            item.ItemId = c.networkId
            item.Label = c.SSID
            list.add(item)
        }

        return list
    }

    override fun onSelectListItem(itemId: Int) {
        //TODO:
    }

    companion object {

        fun newInstance(): WentToFragment {
            return WentToFragment()
        }
    }
}
