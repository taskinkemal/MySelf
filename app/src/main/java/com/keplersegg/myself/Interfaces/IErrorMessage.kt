package com.keplersegg.myself.Interfaces

import java.lang.Exception

interface IErrorMessage {

    fun showErrorMessage(message: String)

    fun logException(exception: Exception, message: String)
}
