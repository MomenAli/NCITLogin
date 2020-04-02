package com.eng.momen.ncitlogin.entry;

import androidx.appcompat.app.AppCompatActivity;

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

import com.eng.momen.ncitlogin.AppExecuters;
import com.eng.momen.ncitlogin.MainActivity;
import com.eng.momen.ncitlogin.R;
import com.eng.momen.ncitlogin.user.AppDatabase;
import com.eng.momen.ncitlogin.user.User;
import com.eng.momen.ncitlogin.user.UserInfo;
import com.eng.momen.ncitlogin.utils.JSONUtils;
import com.eng.momen.ncitlogin.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.eng.momen.ncitlogin.MainActivity.PREFS_USER_INFO;
import static com.eng.momen.ncitlogin.MainActivity.PREFS_USER_NAME;

public class LoginActivity extends Activity {
    private static final String TAG = "RegisterActivity";
    @BindView(R.id.etUserName)
    TextView tvUserName;
    @BindView(R.id.etPassword)
    EditText etPassword;

    // Member variable for the database
    private AppDatabase mDB;

    Context mContext;
    String userName;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;
        ButterKnife.bind(this);

        mDB = AppDatabase.getsInstance(getApplicationContext());
    }

    public void login(View view) {
        // check if the fields not empty
        if(tvUserName.getText().toString().trim().equals("")|etPassword.getText().toString().trim().equals("")){
            Toast.makeText(this, R.string.complete_you_information_message, Toast.LENGTH_LONG).show();
            return;
        }

        // get the username and password
        userName = tvUserName.getText().toString();
        password = etPassword.getText().toString();
        // check for network connection
        if(!isNetworkAvailable())
        {
            Toast.makeText(this,"لا يوجد إنترنت",Toast.LENGTH_LONG).show();
        }
        new LoginTask().execute();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class LoginTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected String doInBackground(URL... params) {
            Log.d(TAG, "doInBackground: ");
            URL searchUrl = null;
            searchUrl = NetworkUtils.buildLoginUrl();

            String serverResponse = null;
            try {
                serverResponse = NetworkUtils.postResponseToHttpUrl(searchUrl,NetworkUtils.createDataForLogin(userName,password));
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
                User user = null;
                try {
                    user = JSONUtils.parseUserInfo(serverResponse,mContext);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onPostExecute: "+user);
                if (user != null){

                    // if user != null then login success
                    // save information about the user
                    final User temp = new User(user.userName,user.userID);
                    AppExecuters.getsInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDB.userDao().insertUser(temp);
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(intent);
                            Log.d(TAG, "run: insert done");
                            finish();
                        }
                    });

                    // start the main activity

                    return;

                }
            }
            Toast.makeText(getBaseContext(),mContext.getString(R.string.login_failed_message),Toast.LENGTH_LONG).show();

        }
    }

}
