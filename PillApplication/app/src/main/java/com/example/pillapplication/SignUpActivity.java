package com.example.pillapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import android.os.Bundle;

public class SignUpActivity extends AppCompatActivity {

    Database database;
    EditText usernameField;
    EditText passwordField;
    EditText firstNameField;
    EditText lastNameField;
    EditText ageField;
    EditText weightField;
    EditText heightField;
    Button signUpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //assign class variables
        database = Database.getInstance(getApplicationContext());
        usernameField = findViewById(R.id.inputtedUsername);
        passwordField = findViewById(R.id.inputtedPassword);
        firstNameField = findViewById(R.id.inputtedFirstName);
        lastNameField = findViewById(R.id.inputtedLastName);
        ageField = findViewById(R.id.inputtedAge);
        weightField = findViewById(R.id.inputtedWeight);
        heightField = findViewById(R.id.inputtedHeight);
        signUpButton = (Button) findViewById(R.id.SignUpButton);

        //check to make sure that every inputted field works
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check username
                String username = usernameField.getText().toString();
                if (username.length() <= 4) {
                    Toast.makeText(getApplicationContext(), "Username must be at least 4 letters",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (database.usernameExists(username)) {
                    Toast.makeText(getApplicationContext(), "Username already exists",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //check password
                String password = passwordField.getText().toString();
                if (password.length() <= 5) {
                    Toast.makeText(getApplicationContext(), "Password must be at least 6 letters",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //check first name
                String firstName = firstNameField.getText().toString();
                if (firstName.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter a First Name",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //check last name
                String lastName = lastNameField.getText().toString();
                if (lastName.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter a Last Name",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //check age
                int age = Integer.parseInt(ageField.getText().toString());
                if (ageField.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter your age",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (age > 122) {
                    Toast.makeText(getApplicationContext(), "Please enter a reasonable age",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //check weight
                int weight = Integer.parseInt(weightField.getText().toString());
                if (weightField.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter your weight",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (weight > 500) {
                    Toast.makeText(getApplicationContext(), "Please enter a reasonable weight",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //check weight
                int height = Integer.parseInt(heightField.getText().toString());
                if (heightField.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter your height",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (height > 105) {
                    Toast.makeText(getApplicationContext(), "Please enter a reasonable height",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //insert data into database
                database.insertNewUser(username, password, firstName, lastName, age, weight, height);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}