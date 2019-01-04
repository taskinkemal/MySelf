package com.keplersegg.myself.interfaces

import com.keplersegg.myself.MySelfApplication
import com.keplersegg.myself.Room.AppDatabase

interface ISyncTasksHost : ISyncHost {

    fun onSyncTasksSuccess()
}
