package com.example.pillapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Stack;

public class HistoryActivity extends AppCompatActivity {

    Database database;
    Button databaseButton;
    Button yourPillsButton;
    Button doctorsButton;
    User user;
    Stack<History> consumptions;

    ScrollView scroll;
    public Bundle retrieve = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //class variables
        database = Database.getInstance(getApplicationContext());
        databaseButton = findViewById(R.id.allPillsButton);
        yourPillsButton = findViewById(R.id.yourPillsButton);
        scroll = findViewById(R.id.scroller);
        doctorsButton = findViewById(R.id.DoctorButton);


        databaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), pillDatabaseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", user.username);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        yourPillsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PillViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", user.username);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        doctorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DoctorsAppointmentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", user.username);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        database = Database.getInstance(getApplicationContext());
        consumptions = new Stack<History>();

        //get username from login
        retrieve = this.getIntent().getExtras();
        String username = retrieve.getString("username");

        //get all the user's data
        Cursor data = database.getUser(username);
        data.moveToNext();
        user = new User(username, data.getString(1), data.getString(2), data.getString(3),
                data.getInt(4), data.getInt(5), data.getInt(6));

        //get and Display the History
        Cursor history = database.getConsumption(username);
        history.moveToNext();
        for(int i = 0; i < history.getCount(); i++) {
            consumptions.push(new History(history.getString(0), history.getInt(1),
                    history.getInt(2), history.getInt(3)));
            history.moveToNext();
        }

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        int color = 1;
        //display history of pills taken
        while(!consumptions.isEmpty()) {
            //convert pixels to dp
            float density = this.getResources().getDisplayMetrics().density;
            int dp = (int) (density * 10);

            //set textView information
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setGravity(Gravity.CENTER);
            History curr = consumptions.pop();
            String message;
            if (curr.minTaken <= 9) {
                message = curr.pillName + " at " + curr.hourTaken + ":0" + curr.minTaken;
            } else {
                message = curr.pillName + " at " + curr.hourTaken + ":" + curr.minTaken;
            }
            textView.setText(message);
            textView.setPadding(dp, dp, dp, dp);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);

            if (color % 2 == 1) {
                textView.setBackgroundColor(Color.BLACK);
                textView.setTextColor(Color.WHITE);
            }

            color++;
            layout.addView(textView);
        }

        float density = this.getResources().getDisplayMetrics().density;
        int dp = (int) (density * 50);

        //add buffer for scroll
        TextView buffer = new TextView(this);
        buffer.setHeight(dp);

        layout.addView(buffer);

        scroll.addView(layout);
    }
}