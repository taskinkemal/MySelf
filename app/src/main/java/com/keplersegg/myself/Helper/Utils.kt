package com.keplersegg.myself.Helper

import java.util.*
import java.util.concurrent.TimeUnit

object Utils {

    fun getToday(): Int {

        val calZero = Calendar.getInstance()
        calZero.set(2018, Calendar.JANUARY, 1)

        val msDiff = Calendar.getInstance().timeInMillis - calZero.timeInMillis
        return TimeUnit.MILLISECONDS.toDays(msDiff).toInt()
    }
}