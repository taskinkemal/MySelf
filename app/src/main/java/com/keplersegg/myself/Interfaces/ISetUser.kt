package com.keplersegg.myself.Interfaces

import com.keplersegg.myself.Helper.TokenType
import com.keplersegg.myself.Models.User

interface ISetUser : IErrorMessage {

    fun setUser(user: User?, tokenType: TokenType)

    fun setToken(tokenType: TokenType, token: String?)
}
