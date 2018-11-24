package com.keplersegg.myself.helper

import android.os.Build



internal object Constant {

    object API {

        //private val _apiHost = "http://10.0.2.2:5001/"
        //private val _apiHost = "http://10.0.2.2/myself/"
        //private val _apiHost = "http://10.126.21.118/ccw/";
        private val _apiHost = "http://myself.keplersegg.com/api/"

        internal val ApiRoot = _apiHost + "api/"

        fun isEmulator(): Boolean {
            return (Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.startsWith("unknown")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK built for x86")
                    || Build.MANUFACTURER.contains("Genymotion")
                    || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                    || "google_sdk" == Build.PRODUCT)
        }
    }
}