package com.eng.momen.ncitlogin.utils;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * Created by MomenAli on 10/15/2017.
 */

public class NetworkUtils {


    private static final String TAG = "NetworkUtils";

    final static String BASE_URL =
            URLParameters.BASE_URL;
    final static String REGISTER_URL =
            URLParameters.REGISTER;
    final static String LOGIN_URL =
            URLParameters.LOGIN;
    final static String USERS_URL =
            URLParameters.USERS;

    final static String ID_PARAM = "user_id";
    final static String NAME_PARAM = "user_name";
    final static String PASSWORD_PARAM = "user_pass";


    //register delegate build url
    public static URL buildRegisterUrl(){

        Uri builtUri = Uri.parse(BASE_URL + REGISTER_URL).buildUpon()
                .build();
        //Log.d(TAG, "buildRegisterUrl: " + builtUri.toString());
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    //Login delegate build url
    public static URL buildLoginUrl(){

        Uri builtUri = Uri.parse(BASE_URL + LOGIN_URL).buildUpon()
                .build();
        Log.d(TAG, "buildRegisterUrl: " + builtUri.toString());
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }



    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String createDataForRegister(String name,String pass) throws UnsupportedEncodingException {
        // Create data variable for sent values to server

        String data = URLEncoder.encode(NAME_PARAM, "UTF-8")
                + "=" + URLEncoder.encode(name, "UTF-8");


        data += "&" + URLEncoder.encode(PASSWORD_PARAM, "UTF-8")
                + "=" + URLEncoder.encode(pass, "UTF-8");
        return data;
    }


    public static String createDataForLogin(String name, String pass) throws UnsupportedEncodingException {
        // Create data variable for sent values to server

        String data = URLEncoder.encode(NAME_PARAM, "UTF-8")
                + "=" + URLEncoder.encode(name, "UTF-8");
        data += "&" + URLEncoder.encode(PASSWORD_PARAM, "UTF-8")
                + "=" + URLEncoder.encode(pass, "UTF-8");

        return data;
    }




    public static String postResponseToHttpUrl(URL url, String data) throws IOException {
        Log.d(TAG, "postResponseFromHttpUrl: "+url.toString()+data);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        BufferedReader reader = null;
        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);


            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(data);
            wr.flush();
            try {
                // Get the server response

                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + "\n");
                }


                String text = sb.toString();
                Log.d(TAG, "postResponseToHttpUrl: " + text);
                return text;
            } catch (Exception ex) {

            } finally {
                try {

                    reader.close();
                } catch (Exception ex) {
                }
            }
          /*  Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }*/
            return null;
        } finally {
            urlConnection.disconnect();
        }
    }
}