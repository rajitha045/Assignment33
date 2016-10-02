package com.hoardings.android.college;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;


public class SignupActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button signUpButton = (Button) findViewById(R.id.signup);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = ((TextView) findViewById(R.id.username)).getText().toString();
                String name = ((TextView) findViewById(R.id.name)).getText().toString();
                String password = ((TextView) findViewById(R.id.password)).getText().toString();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                UserDetails userDetails = new UserDetails(userName,password,name);
                System.out.println();
                editor.putString(userName,new Gson().toJson(userDetails));
                editor.commit();
                System.out.println(" username : " + userName + " password : " + password + " name : " + name);
            }
        });

    }

}
