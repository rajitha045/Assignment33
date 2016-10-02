package com.hoardings.android.college;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button _loginButton = (Button) findViewById(R.id.btn_login);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        TextView _signupLink = (TextView) findViewById(R.id.link_signup);


        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public void login() {

        EditText _emailText = (EditText) findViewById(R.id.input_email);
        EditText _passwordText = (EditText) findViewById(R.id.input_password);
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        UserDetails userDetails = new Gson().fromJson(preferences.getString(email, null), UserDetails.class);
        System.out.println(email + " - email");
        System.out.println(password + " - password");
        if(userDetails ==null){
            Toast.makeText(getApplicationContext(),"User not registered",Toast.LENGTH_LONG).show();
            System.out.println("unregistered user");
        }else {
            System.out.println("User details : "  + new Gson().toJson(userDetails));
            if (userDetails.getPassword().equals(password)) {
                System.out.println("Log in successful");
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(getApplicationContext(),"Invalid Username and password",Toast.LENGTH_LONG).show();
                System.out.println("Invalid username and password");
            }
        }

    }

}