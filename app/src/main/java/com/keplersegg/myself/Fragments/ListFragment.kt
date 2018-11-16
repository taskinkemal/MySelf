package com.keplersegg.myself.Fragments


import android.os.Bundle
import android.view.View
import com.keplersegg.myself.Adapters.ListItemAdapter
import com.keplersegg.myself.Models.ListItem

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.keplersegg.myself.Interfaces.IListItemHoster
import com.keplersegg.myself.Models.ListSource


abstract class ListFragment : MasterFragment(), IListItemHoster {

    private var listSource: ListSource = ListSource()
    private var adapter: ListItemAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listSource.showImage = showImages()
        adapter = ListItemAdapter(this, listSource)
        getRecyclerView().adapter = adapter
        getRecyclerView().layoutManager = LinearLayoutManager(activity)
    }

    abstract fun getList(): List<ListItem>
    abstract fun showImages(): Boolean
    abstract fun getRecyclerView(): RecyclerView

    override fun onResume() {
        super.onResume()

        adapter!!.updateData(getList())
    }
}
