package com.keplersegg.myself.Async

import android.support.v4.app.FragmentActivity

interface IListItemHoster {

    fun onSelectListItem(itemId: Int)

    fun getActivity() : FragmentActivity?
}
