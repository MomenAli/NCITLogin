package com.eng.momen.ncitlogin.entry;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.eng.momen.ncitlogin.R;

public class FirstActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }

    public void login(View view) {
        // if login button press open login activity
        Intent intent =  new Intent(this,LoginActivity.class);
        this.startActivity(intent);
        finish();
    }

    public void register(View view) {
        // if register button press open register activity
        Intent intent =  new Intent(this,RegisterActivity.class);
        this.startActivity(intent);
        finish();
    }
}
