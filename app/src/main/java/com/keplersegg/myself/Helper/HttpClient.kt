package com.keplersegg.myself.Helper

import com.keplersegg.myself.Async.IHttpProvider

import org.json.JSONObject

import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


object HttpClient {

    private fun isNetworkAvailable(provider: IHttpProvider): Boolean {

        return provider.getConnectivityManager().activeNetworkInfo != null
    }

    fun hasInternetAccess(provider: IHttpProvider): Boolean {

        if (isNetworkAvailable(provider)) {

            return true

            /* TODO: couldn't do ping from the emulator.
            try {

                Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1    www.google.com");
                int returnVal = p1.waitFor();
                return returnVal == 0;

            } catch (Exception e) {

                CrashLogger.AddExceptionLog("Error checking internet connection", e);
            }

            */
        }

        return false
    }

    fun send(provider: IHttpProvider, url: String, method: String, objParam: JSONObject?): JSONObject? {

        val accessToken = provider.getAccessToken()
        var responseObject: JSONObject?

        var responseString = ""
        var errorString = ""

        try {

            if (!hasInternetAccess(provider)) {

                responseObject = JSONObject()
                responseObject.put("Code", 503)
                responseObject.put("Phrase", "InternetConnectionError")
                responseObject.put("Message", "")

            } else {

                val _url = URL(getAbsoluteUrl(url))
                val client = _url.openConnection() as HttpURLConnection

                client.requestMethod = method.toUpperCase()
                //client.setDoOutput(true);
                client.instanceFollowRedirects = false

                if (accessToken != null) {

                    client.setRequestProperty("Authorization", "Bearer " + accessToken)
                    //client.setRequestProperty("Accept-Language", headers!!.GetCulture())
                }
                client.setRequestProperty("Accept", "application/json")
                client.setRequestProperty("Content-type", "application/json")

                client.useCaches = false

                if (objParam != null) {

                    val bytes = objParam.toString().toByteArray(charset("UTF-8"))

                    client.outputStream.write(bytes)
                }

                val responseCode = client.responseCode

                if (responseCode == 200) {

                    responseString = readStream(client.inputStream)
                    responseObject = JSONObject(responseString)

                } else {

                    errorString = readStream(client.errorStream)
                    responseObject = JSONObject(errorString)
                }
            }
        } catch (exc: Exception) {

            responseObject = JSONObject()
            responseObject.put("Code", 504)
            responseObject.put("Phrase", "GeneralError")
            responseObject.put("Message", exc.message)

            var log = "responseString:$responseString errorString:$errorString"
            log += " url:$url"
            log += " method:$method"

            provider.logException(Exception(log, exc), "HttpClient")
        }

        return responseObject
    }

    private fun getAbsoluteUrl(relativeUrl: String): String {

        return Constant.API.ApiRoot + relativeUrl
    }

    private fun readStream(stream: InputStream): String {

        return stream.bufferedReader().use { it.readText() }
    }
}
