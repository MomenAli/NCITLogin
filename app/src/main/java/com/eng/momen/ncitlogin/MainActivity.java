package com.eng.momen.ncitlogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.eng.momen.ncitlogin.R;
import com.eng.momen.ncitlogin.entry.FirstActivity;
import com.eng.momen.ncitlogin.user.AppDatabase;
import com.eng.momen.ncitlogin.user.User;
import com.eng.momen.ncitlogin.user.UserInfo;

public class MainActivity extends Activity {



    public static final String PREFS_USER_INFO = "user_info";
    public static final String PREFS_USER_NAME = "user_name";
    private static final String TAG = MainActivity.class.getSimpleName();


    // Member variable for the database
    private AppDatabase mDB;
    Context mContext;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        // check if the user is login before
        mDB = AppDatabase.getsInstance(getApplicationContext());
        AppExecuters.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                user = mDB.userDao().loadUser();

                if(user == null ){
                    // if user isn't login start first activity
                    Intent intent = new Intent(mContext, FirstActivity.class);
                    mContext.startActivity(intent);
                    finish();
                }

            }
        });

    }



    public void signout(View view) {
        //remove all the user information
        final User tempUser = new User(user.userName,user.userID);
        AppExecuters.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDB.userDao().deleteUser(tempUser);
            }
        });
        // start first activity
        Intent intent = new Intent(this,FirstActivity.class);
        this.startActivity(intent);
        finish();
    }
}
