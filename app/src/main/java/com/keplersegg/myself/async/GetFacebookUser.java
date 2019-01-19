package com.keplersegg.myself.async;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.keplersegg.myself.interfaces.ISetFacebookUser;
import com.keplersegg.myself.models.User;

import org.json.JSONException;
import org.json.JSONObject;


public class GetFacebookUser {

    public void Run(final ISetFacebookUser activity, final AccessToken accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {

                        User user = null;
                        String tokenString = null;

                        try {

                            if (object != null && object.has("email")) {

                                tokenString = accessToken.getToken();

                                user = new User();
                                user.setEmail(object.getString("email"));
                                user.setFirstName(object.getString("first_name"));
                                user.setLastName(object.getString("last_name"));
                                user.setFacebookToken(accessToken);
                                user.setPictureUrl(object.getJSONObject("picture").getJSONObject("data").getString("url"));
                            }

                        } catch (JSONException e) {

                            activity.logException(e, "GetFacebookUser.Run");
                        }

                        activity.onSetFacebookUser(user, tokenString);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,email,gender,birthday,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
