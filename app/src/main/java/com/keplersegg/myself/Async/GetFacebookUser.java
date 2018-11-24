package com.keplersegg.myself.Async;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.keplersegg.myself.Helper.TokenType;
import com.keplersegg.myself.Interfaces.ISetUser;
import com.keplersegg.myself.Models.User;

import org.json.JSONException;
import org.json.JSONObject;


public class GetFacebookUser {

    public void Run(final ISetUser activity, final AccessToken accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {

                        try {

                            if (object != null && object.has("email")) {

                                activity.setToken(TokenType.Facebook, accessToken.getToken());

                                User user = new User();
                                user.setEmail(object.getString("email"));
                                user.setFirstName(object.getString("first_name"));
                                user.setLastName(object.getString("last_name"));
                                user.setFacebookToken(accessToken);
                                user.setPictureUrl(object.getJSONObject("picture").getJSONObject("data").getString("url"));

                                activity.setUser(user, TokenType.Facebook);
                            }
                            else {

                                activity.setUser(null, TokenType.Facebook);
                            }

                        } catch (JSONException e) {

                            activity.logException(e, "GetFacebookUser.Run");
                            activity.setUser(null, TokenType.Facebook);
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,email,gender,birthday,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
