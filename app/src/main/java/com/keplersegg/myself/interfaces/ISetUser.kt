package com.keplersegg.myself.interfaces

import com.keplersegg.myself.helper.TokenType
import com.keplersegg.myself.models.User

interface ISetUser : IErrorMessage {

    fun setUser(user: User?, tokenType: TokenType)

    fun setToken(tokenType: TokenType, token: String?)
}
