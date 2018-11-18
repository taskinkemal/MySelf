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

    private fun getDateWithoutTime(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar
    }

    fun getDayBack(daysBack: Int): Long {

        val today = getDateWithoutTime()

        today.add(Calendar.DATE, daysBack)

        return today.timeInMillis
    }
}