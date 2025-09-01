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
            float density = this.getResources().getDisplayMetrics().density;
            int dp = (int) (density * 12); // spacing

            History curr = consumptions.pop();

            String message;
            if (curr.minTaken <= 9) {
                message = curr.pillName + " at " + curr.hourTaken + ":0" + curr.minTaken;
            } else {
                message = curr.pillName + " at " + curr.hourTaken + ":" + curr.minTaken;
            }

            LinearLayout container = new LinearLayout(this);
            container.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(dp, dp / 2, dp, dp / 2); // spacing between items
            container.setLayoutParams(params);
            container.setPadding(dp, dp, dp, dp);
            container.setElevation(6 * density);

            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textView.setText(message);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            textView.setTextColor(Color.parseColor("#2C3E50"));
            textView.setGravity(Gravity.START);

            View accentBar = new View(this);
            LinearLayout.LayoutParams barParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (int) (density * 3)
            );
            barParams.topMargin = dp / 2;
            accentBar.setLayoutParams(barParams);
            if (color % 2 == 0) {
                accentBar.setBackgroundColor(Color.parseColor("#3498DB")); // blue
            } else {
                accentBar.setBackgroundColor(Color.parseColor("#2ECC71")); // green
            }

            container.addView(textView);
            container.addView(accentBar);

            layout.addView(container);

            color++;
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