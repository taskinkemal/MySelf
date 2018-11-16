package com.keplersegg.myself.Interfaces

import androidx.fragment.app.FragmentActivity
import com.keplersegg.myself.Models.ListItem


interface IListItemHoster {

    fun onSelectListItem(item: ListItem)

    fun getActivity() : FragmentActivity?
}
