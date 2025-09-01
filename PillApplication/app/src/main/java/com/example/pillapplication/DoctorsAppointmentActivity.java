package com.example.pillapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DoctorsAppointmentActivity extends AppCompatActivity {

    Database database;
    Button allPillsButton;
    Button yourPillsButton;
    Button historyButton;
    FloatingActionButton addAppointmentButton;
    User user;
    public Bundle retrieve = null;
    ScrollView scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_appointment);

        //class variables
        allPillsButton = findViewById(R.id.allPillsButton);
        yourPillsButton = findViewById(R.id.yourPillsButton);
        historyButton = findViewById(R.id.historyButton);
        database = Database.getInstance(getApplicationContext());
        addAppointmentButton = findViewById(R.id.addDAButton);
        scroll = findViewById(R.id.scroller);

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

        //create linearlayout
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        //get doctor's appointments
        ArrayList<Appointment> appointments = new ArrayList<>();
        Cursor getAppointments = database.getAppointments(user.username);
        getAppointments.moveToNext();

        //put everything in an arraylist
        for (int i = 0; i < getAppointments.getCount(); i++) {
            appointments.add(new Appointment(getAppointments.getString(0), getAppointments.getInt(1),
            getAppointments.getInt(2), getAppointments.getInt(3), getAppointments.getInt(4),
                    getAppointments.getInt(5)));
            getAppointments.moveToNext();
        }

        int size = appointments.size();
        display(layout, size, appointments);
        scroll.addView(layout);

        //adds an appointment
        addAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float density = getApplicationContext().getResources().getDisplayMetrics().density;
                int dp = (int) (density * 10);

                layout.removeAllViews();
                display(layout, 0, new ArrayList<Appointment>());

                //doctor name editText
                EditText doctorName = new EditText(getApplicationContext());
                doctorName.setHint("Name of Doctor");
                doctorName.setPadding(dp,dp,dp,dp);
                doctorName.setInputType(InputType.TYPE_CLASS_TEXT);
                doctorName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                layout.addView(doctorName);

                //year EditText
                EditText year = new EditText(getApplicationContext());
                year.setHint("Year of Appointment");
                year.setPadding(dp,dp,dp,dp);
                year.setInputType(InputType.TYPE_CLASS_NUMBER);
                year.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                layout.addView(year);

                //month EditText
                EditText month = new EditText(getApplicationContext());
                month.setHint("Month of Appointment (1-12)");
                month.setPadding(dp,dp,dp,dp);
                month.setInputType(InputType.TYPE_CLASS_NUMBER);
                month.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                layout.addView(month);

                //day EditText
                EditText day = new EditText(getApplicationContext());
                day.setHint("Day of Appointment (1-32)");
                day.setPadding(dp,dp,dp,dp);
                day.setInputType(InputType.TYPE_CLASS_NUMBER);
                day.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                layout.addView(day);

                //hourEditText
                EditText hour = new EditText(getApplicationContext());
                hour.setHint("Hour of Appointment (0-23)");
                hour.setPadding(dp,dp,dp,dp);
                hour.setInputType(InputType.TYPE_CLASS_NUMBER);
                hour.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                layout.addView(hour);

                //min EditText
                EditText min = new EditText(getApplicationContext());
                min.setHint("Minute of Appointment (2 digits)");
                min.setPadding(dp,dp,dp,dp);
                min.setInputType(InputType.TYPE_CLASS_NUMBER);
                min.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                layout.addView(min);

                Button button = new Button(getApplicationContext());
                button.setText("Add");
                button.setPadding(dp,dp,dp,dp);
                button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                layout.addView(button);

                //validates if all the EditTexts are valid
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (doctorName.getText().toString().isEmpty() ||
                                year.getText().toString().isEmpty() ||
                                month.getText().toString().isEmpty() ||
                                day.getText().toString().isEmpty()||
                                hour.getText().toString().isEmpty() ||
                                min.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Fill In all Fields",
                                    Toast.LENGTH_SHORT).show();
                        } else if (Integer.parseInt(year.getText().toString()) < 2024) {
                            Toast.makeText(getApplicationContext(), "Year must be at least in 2024",
                                    Toast.LENGTH_SHORT).show();
                        } else if (Integer.parseInt(month.getText().toString()) <= 0 ||
                                Integer.parseInt(month.getText().toString()) > 12) {
                            Toast.makeText(getApplicationContext(), "Must be a valid month of the year",
                                    Toast.LENGTH_SHORT).show();
                        } else if (Integer.parseInt(day.getText().toString()) <= 0 ||
                                Integer.parseInt(day.getText().toString()) > 32) {
                            Toast.makeText(getApplicationContext(), "Day is out of bounds",
                                    Toast.LENGTH_SHORT).show();
                        } else if (Integer.parseInt(hour.getText().toString()) < 0 ||
                                Integer.parseInt(hour.getText().toString()) > 23) {
                            Toast.makeText(getApplicationContext(), "Please enter a hour between 1 and 23",
                                    Toast.LENGTH_SHORT).show();
                        } else if (Integer.parseInt(min.getText().toString()) > 59) {
                            Toast.makeText(getApplicationContext(), "Enter Number from 00 - 59",
                                    Toast.LENGTH_SHORT).show();
                        } else if (min.getText().toString().length() != 2) {
                            Toast.makeText(getApplicationContext(), "Use 2 digits for one digit numbers",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            database.addAppointment(user.username, doctorName.getText().toString(),
                                    Integer.parseInt(year.getText().toString()),
                                    Integer.parseInt(month.getText().toString()),
                                    Integer.parseInt(day.getText().toString()),
                                    Integer.parseInt(hour.getText().toString()),
                                    Integer.parseInt(min.getText().toString()));

                            //reload
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);

                        }
                    }
                });
            }
        });
    }

    public void display(LinearLayout layout, int size, ArrayList<Appointment> appointments) {
            // Convert pixels to dp once
            float density = this.getResources().getDisplayMetrics().density;
            int dp = (int) (density * 10);

            for (int i = 0; i < size; i++) {
                Appointment appt = appointments.get(i);

                // Format the appointment time string
                String formattedTime = appt.min < 10
                        ? appt.hour + ":0" + appt.min
                        : appt.hour + ":" + appt.min;

                String message = "Appointment with " + appt.doctorName +
                        "\nOn " + appt.month + "/" + appt.day + "/" + appt.year +
                        " at " + formattedTime;

                CardView card = new CardView(this);
                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                cardParams.setMargins(dp, dp, dp, dp);
                card.setLayoutParams(cardParams);
                card.setRadius(20f);
                card.setCardElevation(8f);
                card.setUseCompatPadding(true);

                // --- Inner container ---
                LinearLayout container = new LinearLayout(this);
                container.setOrientation(LinearLayout.VERTICAL);
                container.setPadding(dp * 2, dp * 2, dp * 2, dp * 2);

                // --- Appointment details text ---
                TextView textView = new TextView(this);
                textView.setText(message);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setTextColor(Color.parseColor("#2C3E50"));
                textView.setGravity(Gravity.START);

                // --- Button row ---
                LinearLayout buttonRow = new LinearLayout(this);
                buttonRow.setOrientation(LinearLayout.HORIZONTAL);
                buttonRow.setGravity(Gravity.END);

                // Delete button
                Button deleteButton = new Button(this);
                deleteButton.setText("Delete");
                deleteButton.setBackgroundColor(Color.parseColor("#E74C3C"));
                deleteButton.setTextColor(Color.WHITE);
                deleteButton.setAllCaps(false);

                // Delete functionality
                deleteButton.setOnClickListener(view -> {
                    database.deleteAppointment(
                            user.username,
                            appt.doctorName,
                            appt.year,
                            appt.month,
                            appt.day,
                            appt.hour,
                            appt.min
                    );
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                });

                // Add widgets into hierarchy
                buttonRow.addView(deleteButton);
                container.addView(textView);
                container.addView(buttonRow);
                card.addView(container);
                layout.addView(card);
            }

            // Optional: add buffer space at the bottom of the scroll
            TextView buffer = new TextView(this);
            buffer.setHeight((int) (density * 50));
            layout.addView(buffer);
    }
}