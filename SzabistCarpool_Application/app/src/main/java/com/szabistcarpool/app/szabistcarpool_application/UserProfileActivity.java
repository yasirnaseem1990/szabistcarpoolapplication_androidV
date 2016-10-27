package com.szabistcarpool.app.szabistcarpool_application;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by ttwyf on 5/6/2016.
 */
public class UserProfileActivity extends Activity {
    TextView textView;
    TextView  tv_pass, tv_email;
    SharedPreferences pref;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        textView = (TextView)findViewById(R.id.textView2);
        tv_email = (TextView) findViewById(R.id.second_text_email);
        tv_pass = (TextView) findViewById(R.id.second_text_pass);

        pref = getSharedPreferences("Login", 0);
        String email = pref.getString("uEmail", null);
        String pass = pref.getString("uPassword", null);
// Now set these value into textview of second activity
        tv_pass.setText(pass);
        tv_email.setText(email);
    }
}
