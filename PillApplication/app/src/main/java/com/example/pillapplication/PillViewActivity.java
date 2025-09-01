package com.example.pillapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
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

        //define density
        float density = this.getResources().getDisplayMetrics().density;
        int dp = (int) (density * 50);

        for (int i = 0; i < size; i++) {
            String pillName = user.pills.get(i).pillName;
            String pillTiming = String.valueOf(user.pills.get(i).pillTiming);

            CardView card = new CardView(this);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            // Apply margin to the CardView itself to create space between cards
            cardParams.setMargins(0, (int) getResources().getDisplayMetrics().density * 8, 0, 0); // 8dp top margin
            card.setLayoutParams(cardParams);
            card.setRadius(20f);
            card.setCardElevation(8f);
            card.setUseCompatPadding(true);

            // Inner container for card content
            LinearLayout pillLayout = new LinearLayout(this);
            LinearLayout.LayoutParams pillLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            pillLayout.setLayoutParams(pillLayoutParams);
            pillLayout.setOrientation(LinearLayout.VERTICAL);
            // Add padding to the inner layout
            pillLayout.setPadding((int) getResources().getDisplayMetrics().density * 16, // 16dp left padding
                    (int) getResources().getDisplayMetrics().density * 16, // 16dp top padding
                    (int) getResources().getDisplayMetrics().density * 16, // 16dp right padding
                    (int) getResources().getDisplayMetrics().density * 16); // 16dp bottom padding

            // Pill info text
            TextView textView = new TextView(this);
            String message = pillName + ": take 1 every " + pillTiming + " hours";
            textView.setText(message);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setTextColor(Color.parseColor("#2C3E50"));

            // Horizontal button row
            LinearLayout buttonRow = new LinearLayout(this);
            LinearLayout.LayoutParams buttonRowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            buttonRowParams.setMargins(0, (int) getResources().getDisplayMetrics().density * 8, 0, 0); // 8dp top margin
            buttonRow.setLayoutParams(buttonRowParams);
            buttonRow.setOrientation(LinearLayout.HORIZONTAL);
            buttonRow.setGravity(Gravity.END); // Aligns buttons to the right

            // Delete button
            Button deleteButton = new Button(this);
            LinearLayout.LayoutParams deleteButtonParams = new LinearLayout.LayoutParams(
                    (int) getResources().getDisplayMetrics().density * 80, // 80dp width
                    (int) getResources().getDisplayMetrics().density * 40  // 40dp height
            );
            deleteButton.setLayoutParams(deleteButtonParams);
            deleteButton.setText("Delete");
            deleteButton.setBackgroundColor(Color.parseColor("#E74C3C"));
            deleteButton.setTextColor(Color.WHITE);
            deleteButton.setAllCaps(false);

            deleteButton.setOnClickListener(view -> {
                database.deleteUserPill(username, pillName);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            });

            // Consume button
            Button consumeButton = new Button(this);
            LinearLayout.LayoutParams consumeButtonParams = new LinearLayout.LayoutParams(
                    (int) getResources().getDisplayMetrics().density * 80, // 80dp width
                    (int) getResources().getDisplayMetrics().density * 40  // 40dp height
            );
            consumeButtonParams.setMargins((int) getResources().getDisplayMetrics().density * 8, 0, 0, 0); // 8dp left margin
            consumeButton.setLayoutParams(consumeButtonParams);
            consumeButton.setText("Take");
            consumeButton.setBackgroundColor(Color.parseColor("#27AE60"));
            consumeButton.setTextColor(Color.WHITE);
            consumeButton.setAllCaps(false);

            // Add buttons to row
            buttonRow.addView(deleteButton);
            buttonRow.addView(consumeButton);

            // Add everything into pill card
            pillLayout.addView(textView);
            pillLayout.addView(buttonRow);
            card.addView(pillLayout);

            // Finally add card to container
            layout.addView(card);

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
                    database.addConsumption(user.username, pillName, timingOfPill, hour, min);
                    Toast.makeText(getApplicationContext(), "Consumption Recorded",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        //add buffer for scroll
        TextView buffer = new TextView(this);
        buffer.setHeight(dp);

        layout.addView(buffer);

        scroll.addView(layout);
    }
}