package com.eng.momen.ncitlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;

import com.eng.momen.ncitlogin.R;
import com.eng.momen.ncitlogin.entry.FirstActivity;
import com.eng.momen.ncitlogin.user.UserInfo;

public class MainActivity extends Activity {



    public static final String PREFS_USER_INFO = "user_info";
    public static final String PREFS_USER_NAME = "user_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // check if the user is login before
        SharedPreferences sharedPreferences = this.getSharedPreferences(PREFS_USER_INFO, Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString(PREFS_USER_NAME, "");
        if(userName.isEmpty()) {

            if(TextUtils.isEmpty(UserInfo.userName)) {
                // if user isn't login start first activity
                Intent intent = new Intent(this, FirstActivity.class);
                this.startActivity(intent);
                finish();
            }
        }
    }



    public void signout(View view) {
        //remove all the user information
        UserInfo.userName = "";
        UserInfo.id = -1;
        SharedPreferences data;
        SharedPreferences.Editor editor;
        data = this.getSharedPreferences(PREFS_USER_INFO, Context.MODE_PRIVATE);
        editor = data.edit();
        editor.putString(PREFS_USER_NAME, "");
        editor.commit();
        // start first activity
        Intent intent = new Intent(this,FirstActivity.class);
        this.startActivity(intent);
        finish();
    }
}
