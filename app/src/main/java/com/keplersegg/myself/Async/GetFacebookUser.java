package com.keplersegg.myself.Async;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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

                            User user = new User();
                            user.Email = object.getString("email");
                            user.FirstName = object.getString("first_name");
                            user.LastName = object.getString("last_name");
                            user.FacebookToken = accessToken;

                            activity.setUser(user);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            activity.setUser(null);
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
