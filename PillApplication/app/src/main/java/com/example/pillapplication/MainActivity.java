package com.example.pillapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    Database database;
    EditText usernameField;
    EditText passwordField;
    Button loginButton;
    Button SignUpButton;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign class variables
        database = Database.getInstance(getApplicationContext());
        usernameField = findViewById(R.id.inputtedUsername);
        passwordField = findViewById(R.id.inputtedPassword);
        loginButton = (Button) findViewById(R.id.LoginButton);
        SignUpButton = (Button) findViewById(R.id.SignUpButton);

        //check if notification permissions was enabled
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }


        //get the information provided in the application
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameField.getText().toString();
                password = passwordField.getText().toString();
                Cursor userSearchResult = database.getLoginInformation(username);

                //validate login information
                if (userSearchResult.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), "No Such Username Exists",
                            Toast.LENGTH_SHORT).show();
                } else {
                    userSearchResult.moveToNext();
                    String correctPassword = userSearchResult.getString(0);
                    if (!(password.equals(correctPassword))) {
                        Toast.makeText(getApplicationContext(), "Incorrect Password",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        openFirstPage(username);
                    }
                }
            }
        });

        //open SignUp page
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    //opens the first page
    public void openFirstPage(String username) {
        Intent intent = new Intent(this, PillViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}