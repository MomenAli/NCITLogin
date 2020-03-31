package com.eng.momen.ncitlogin.entry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eng.momen.ncitlogin.MainActivity;
import com.eng.momen.ncitlogin.R;
import com.eng.momen.ncitlogin.user.UserInfo;
import com.eng.momen.ncitlogin.utils.JSONUtils;
import com.eng.momen.ncitlogin.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.eng.momen.ncitlogin.MainActivity.PREFS_USER_INFO;
import static com.eng.momen.ncitlogin.MainActivity.PREFS_USER_NAME;

public class RegisterActivity extends Activity {

    private static final String TAG = "RegisterActivity";
    @BindView(R.id.etUserName)
    TextView tvUserName;
    @BindView(R.id.etPassword1)
    EditText etPassword1;
    @BindView(R.id.etPassword2)
    EditText etPassword2;


    SharedPreferences sharedPreferences ;


    Context mContext;
    String userName;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = this;
        ButterKnife.bind(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void register(View view) {
        // check if the fields not empty
        if(tvUserName.getText().toString().trim().equals("")|etPassword1.getText().toString().trim().equals("")|etPassword2.getText().toString().trim().equals("")){
            Toast.makeText(this, R.string.complete_you_information_message, Toast.LENGTH_LONG).show();
            return;
        }
        // check if the password are the same
        if(!etPassword1.getText().toString().equals(etPassword2.getText().toString())){
            Toast.makeText(this, R.string.password_not_matched_message, Toast.LENGTH_LONG).show();
            return;
        }
        // get the username and password
        userName = tvUserName.getText().toString();
        password = etPassword1.getText().toString();
        // check for network connection
        if(!isNetworkAvailable())
        {
            Toast.makeText(this,"لا يوجد إنترنت",Toast.LENGTH_LONG).show();
        }
        new RegisterTask().execute();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class RegisterTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected String doInBackground(URL... params) {
            Log.d(TAG, "doInBackground: ");
            URL searchUrl = null;
            searchUrl = NetworkUtils.buildRegisterUrl();

            String serverResponse = null;
            try {
                serverResponse = NetworkUtils.postResponseToHttpUrl(searchUrl,NetworkUtils.createDataForRegister(userName,password));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return serverResponse;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String serverResponse) {
            Log.d(TAG, "onPostExecute: "+serverResponse);
            if (serverResponse != null && !serverResponse.equals("")) {
                Log.d(TAG, "onPostExecute: "+serverResponse);

                if (JSONUtils.parseRegisterResponse(serverResponse,mContext)){
                    if (!UserInfo.feedbackMessage.isEmpty()){
                        Toast.makeText(getBaseContext(),UserInfo.feedbackMessage,Toast.LENGTH_LONG).show();
                    }

                    SharedPreferences data;
                    SharedPreferences.Editor editor;
                    data = mContext.getSharedPreferences(PREFS_USER_INFO, Context.MODE_PRIVATE);
                    editor = data.edit();
                    editor.putString(PREFS_USER_NAME, UserInfo.userName);
                    editor.commit();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);

                    finish();
                }else{
                    if (!UserInfo.feedbackMessage.isEmpty()){
                        Toast.makeText(getBaseContext(),UserInfo.feedbackMessage,Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }



}
