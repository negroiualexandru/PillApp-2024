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
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class PillViewActivity extends AppCompatActivity {

    Database database;
    User user;
    ScrollView scroll;
    Button allPillsButton;
    Button historyButton;
    Button doctorsButton;
    public Bundle retrieve = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_view);

        //class variables
        database = Database.getInstance(getApplicationContext());
        scroll = findViewById(R.id.scroller);
        allPillsButton = findViewById(R.id.allPillsButton);
        historyButton = findViewById(R.id.historyButton);
        doctorsButton = findViewById(R.id.DoctorButton);

        //move to other activities
        allPillsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), pillDatabaseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", user.username);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
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

        //get username from login
        retrieve = this.getIntent().getExtras();
        String username = retrieve.getString("username");

        //get all the user's data
        Cursor data = database.getUser(username);
        data.moveToNext();
        user = new User(username, data.getString(1), data.getString(2), data.getString(3),
                data.getInt(4), data.getInt(5), data.getInt(6));

        //get user's pills
        Cursor pillData = database.getUserPills(username);
        pillData.moveToNext();
        while(user.pills.size() < pillData.getCount()) {
            user.pills.add(new Pill(pillData.getString(0), pillData.getInt(1)));
            pillData.moveToNext();
        }

        //addPillsToScrollView
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        int size = user.pills.size();

        for (int i = 0; i < size; i++) {
            //convert pixels to dp
            float density = this.getResources().getDisplayMetrics().density;
            int dp = (int) (density * 10);

            //set the text
            TextView textView = new TextView(this);
            String message = user.pills.get(i).pillName + ": take 1 every " +
                    user.pills.get(i).pillTiming + " hours";
            textView.setText(message);

            //set the styling
            textView.setPadding(dp,dp,dp,dp);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);

            //add Button
            Button button = new Button(this);
            button.setText("Delete " + user.pills.get(i).pillName);
            button.setPadding(dp,dp,dp,dp);
            button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);

            String nameOfPill = user.pills.get(i).pillName;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    database.deleteUserPill(username, nameOfPill);
                    //reloads page
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
            });

            //add button that is used to mark that you took a pill
            Button consumeButton = new Button(this);
            consumeButton.setText("Take " + user.pills.get(i).pillName);
            consumeButton.setPadding(dp,dp,dp,dp);
            consumeButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);

            //add a history to the database
            int timingOfPill = user.pills.get(i).pillTiming;
            consumeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Date date = new Date();
                    Calendar calendar = new GregorianCalendar().getInstance();
                    calendar.setTime(date);
                    int hour = calendar.get(Calendar.HOUR);
                    int min = calendar.get(Calendar.MINUTE);
                    database.addConsumption(user.username, nameOfPill, timingOfPill, hour, min);
                    Toast.makeText(getApplicationContext(), "Consumption Recorded",
                            Toast.LENGTH_SHORT).show();
                }
            });

            layout.addView(textView);
            layout.addView(consumeButton);
            layout.addView(button);
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