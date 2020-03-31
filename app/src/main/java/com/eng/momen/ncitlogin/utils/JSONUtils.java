package com.eng.momen.ncitlogin.utils;

import android.content.Context;
import android.util.Log;

import com.example.maghsalty.fragments.income.Income;
import com.example.maghsalty.fragments.state.State;
import com.example.maghsalty.fragments.wait.WaitTask;
import com.example.maghsalty.user.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;


/**
 * Created by Momen Ali on 7/8/2019.
 */

public class JSONUtils {
    private static final String waitFile = "waiting.json";
    private static final String incomeFile = "income.json";


    private static final String TAG = "JSONUtils";
    /* exercise json Object names */
    final static String OWN_ID = "id";
    final static String OWN_TYPE = "order_type";
    final static String OWN_TIME = "order_created_time";
    final static String OWN_ADDRESS = "delivery_address";
    final static String OWN_PRICE = "total_price";
    final static String OWN_PLACE_NAME = "place_name";
    final static String OWN_PICKUP_TIME = "order_pickup_time";
    final static String OWN_RECIEVE_ADDRESS = "recieve_address";
    final static String OWN_PAY_WAY = "pay_way";
    final static String OWN_CUSTOMER_NAME = "customer_name";
    final static String OWN_WAIT_ARRAY = "waiting";



    // wait items
    final static String OWN_CODE= "code";
    final static String OWN_DATA= "data";
    final static String OWN_ORDER_ID = "order_id";
    final static String OWN_STATUES = "order_status";
    final static String OWN_CREATION_TIME = "order_created_time";
    final static String OWN_TOTAL_PRICE = "total_price";
    final static String OWN_SERVICE_NAME= "Service_name";


    public static ArrayList getWaitTaskList(Context context , String JSONStr) throws JSONException {

        /*{"code":200,
        "data":[
        {"order_id":63
        ,"order_status":"under-approval"
        ,"delivery_address":"Address:Egypt,Cairo,Nasr City,El Methak StreetHouseNumber:5BuildNumber:1FloorNumber:8HouseType:owner",
        "order_created_time":"2019-04-05 23:58:26",
        "order_pickup_time":"2012-09-19 23:11:03",
        "total_price":102,
        "order_type":[
        {"Service_name":"special care"}
        ]
        }
        ]
        }*/

        ArrayList<WaitTask> mWaitTasksList = new ArrayList<>();
        JSONObject mainObject = new JSONObject(JSONStr);
        JSONArray mainArray = null;

        mainArray = mainObject.getJSONArray(OWN_DATA);
        JSONObject waitObject;
        for (int i = 0; i < mainArray.length(); i++) {
            WaitTask waitTask = new WaitTask();
            waitObject = mainArray.getJSONObject(i);
            waitTask.setId(Integer.valueOf(waitObject.getString(OWN_ORDER_ID)));
            waitTask.setDelivery_address(waitObject.getString(OWN_ADDRESS));
            waitTask.setCreation_time(waitObject.getString(OWN_CREATION_TIME));
            waitTask.setPrice(Integer.valueOf(waitObject.getString(OWN_TOTAL_PRICE)));
            waitTask.setPickup_time(waitObject.getString(OWN_PICKUP_TIME));
            waitTask.setStatues(waitObject.getString(OWN_STATUES));
            JSONArray JsonTypeArray = waitObject.getJSONArray(OWN_TYPE);
            String type = "";
            for (int j = 0 ; j < JsonTypeArray.length();j++) {
                JSONObject serveObject = JsonTypeArray.getJSONObject(j);
                type += serveObject.getString(OWN_SERVICE_NAME);
            }
            waitTask.setType(type);
            mWaitTasksList.add(waitTask);
        }
        return mWaitTasksList;
    }

    public static ArrayList getStateTaskList(Context context) throws JSONException {
        ArrayList<State> mStateTasksList = new ArrayList<>();
        String JSONStr = loadJSONFromAsset(context, waitFile);
        JSONObject mainObject = new JSONObject(JSONStr);
        JSONArray mainArray = null;
        mainArray = mainObject.getJSONArray(OWN_WAIT_ARRAY);


        JSONObject stateObject;
        for (int i = 0; i < mainArray.length(); i++) {
            State state = new State();
            stateObject = mainArray.getJSONObject(i);
            state.setId(Integer.valueOf(stateObject.getString(OWN_ID)));
            state.setAddress(stateObject.getString(OWN_ADDRESS));
            state.setDeliver_time(stateObject.getString(OWN_TIME));
            state.setPrice(Integer.valueOf(stateObject.getString(OWN_PRICE)));
            state.setType(stateObject.getString(OWN_TYPE));
            mStateTasksList.add(state);
        }
        return mStateTasksList;
    }

    final static String OWN_EARNED = "earned";
    final static String OWN_INCOME_ARRAY = "income";

    public static ArrayList getIncomeList(Context context) throws JSONException {
        ArrayList<Income> mIncomeList = new ArrayList<>();
        String JSONStr = loadJSONFromAsset(context, incomeFile);
        Log.d(TAG, "getIncomeList: " + JSONStr);
        JSONObject mainObject = new JSONObject(JSONStr);
        JSONArray mainArray = null;
        mainArray = mainObject.getJSONArray(OWN_INCOME_ARRAY);
        Log.d(TAG, "getIncomeList: mainArray " + mainArray.toString());

        JSONObject incomeObject;
        for (int i = 0; i < mainArray.length(); i++) {
            Income income = new Income();
            incomeObject = mainArray.getJSONObject(i);
            income.setId(Integer.valueOf(incomeObject.getString(OWN_ID)));
            income.setStatue(incomeObject.getString(OWN_STATUES));
            income.setAmount(Integer.valueOf(incomeObject.getString(OWN_EARNED)));
            mIncomeList.add(income);
        }
        Log.d(TAG, "getIncomeList: number " + mIncomeList.size());
        return mIncomeList;
    }


    // parsing user data
    public static void parseUserInfo(String jsonStr) throws JSONException {

        final String OWM_MESSAGE_CODE = "code";
        final String MAIN_TAG = "data";
        final String OWN_FIRST_NAME = "firstName";
        final String OWN_ID = "id";
        final String OWN_MOBILE = "mobile";
        final String OWN_EMAIL = "email";
        final String OWN_MESSAGE = "message";
        final String OWN_PASSWORD = "password";

        JSONObject mainObject = new JSONObject(jsonStr);
        if (mainObject.has(OWM_MESSAGE_CODE)) {
            int errorCode = mainObject.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return;
                default:
                    /* Server probably down */
                    try {
                        JSONObject messageObj = mainObject.getJSONObject(OWN_MESSAGE);

                        if (messageObj.has(OWN_PASSWORD)) {
                            UserInfo.feedbackMessage = messageObj.getString(OWN_PASSWORD);
                        } else if (messageObj.has(OWN_EMAIL)) {
                            UserInfo.feedbackMessage = messageObj.getString(OWN_EMAIL);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        UserInfo.feedbackMessage = mainObject.getString(OWN_MESSAGE);
                    }
                    return;
            }
        }
        JSONObject dataObject = mainObject.getJSONObject(MAIN_TAG);
        UserInfo.id = dataObject.getInt(OWN_ID);
        UserInfo.userName = dataObject.getString(OWN_FIRST_NAME);
        UserInfo.email = dataObject.getString(OWN_EMAIL);
        UserInfo.mobile = dataObject.getString(OWN_MOBILE);
        if (dataObject.has(OWN_MESSAGE))
        UserInfo.feedbackMessage = dataObject.getString(OWN_MESSAGE);
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


}
