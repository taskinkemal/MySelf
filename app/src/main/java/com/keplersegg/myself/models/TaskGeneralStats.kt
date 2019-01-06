package com.keplersegg.myself.models

import com.keplersegg.myself.R
import com.keplersegg.myself.Room.Entity.Task

class TaskGeneralStats {

    var task: Task? = null
    var total: Int = 0

    companion object {

        fun taskTypeImageResourceId(automationType: Int?) : Int? {

            if (automationType == null || automationType < 1 || automationType > 3) {
                return null
            }
            else if (automationType == 1) {

                return R.drawable.ic_baseline_auto_phone_24px
            }
            else if (automationType == 2) {

                return R.drawable.ic_baseline_auto_app_24px
            }
            else {

                return R.drawable.ic_baseline_auto_wifi_24px
            }
        }
    }
}