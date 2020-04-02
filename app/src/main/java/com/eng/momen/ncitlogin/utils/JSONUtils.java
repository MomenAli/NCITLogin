package com.eng.momen.ncitlogin.utils;

import android.content.Context;

import com.eng.momen.ncitlogin.R;
import com.eng.momen.ncitlogin.user.AppDatabase;
import com.eng.momen.ncitlogin.user.User;
import com.eng.momen.ncitlogin.user.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;


/**
 * Created by Momen Ali on 7/8/2019.
 */

public class JSONUtils {

    // parsing user data
    public static User parseUserInfo(String jsonStr, Context mContext) throws JSONException {

        final String OWM_MESSAGE_CODE = "code";
        final String OWN_NAME = "user_name";
        final String OWN_ID = "user_id";
        final String OWN_ERROR_ID = "error_id";
        final String OWN_ERROR_NAME= "error_name";

        JSONObject mainObject = new JSONObject(jsonStr);
        if (mainObject.has(OWM_MESSAGE_CODE)) {
            int errorCode = mainObject.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:

                    return null;
            }
        }
        if(mainObject.has(OWN_ID)) {

            String userName = mainObject.getString(OWN_NAME);
            String id = mainObject.getString(OWN_ID);

            User user =  new User(userName,id);
            return user;

        }

        return null;
    }

    public static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public static boolean parseRegisterResponse(String jsonStr,Context mContext) {
        final String OWN_RESPONSE_ID = "response_id";
        final String OWN_RESPONSE_NAME= "response_name";
        final String OWN_ERROR_ID = "error_id";
        final String OWN_ERROR_NAME= "error_name";

        try {
            JSONObject mainObject = new JSONObject(jsonStr);
            if (mainObject.has(OWN_RESPONSE_ID)) {
                return true;
            }
            if (mainObject.has(OWN_ERROR_ID)) {
                return false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


}
