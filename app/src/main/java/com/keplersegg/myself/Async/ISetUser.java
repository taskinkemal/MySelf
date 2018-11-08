package com.keplersegg.myself.Async;

import com.keplersegg.myself.Helper.TokenType;
import com.keplersegg.myself.Interfaces.IErrorMessage;
import com.keplersegg.myself.Models.User;

public interface ISetUser extends IErrorMessage {

    void setUser(User user);

    void setToken(TokenType tokenType, String token);
}
