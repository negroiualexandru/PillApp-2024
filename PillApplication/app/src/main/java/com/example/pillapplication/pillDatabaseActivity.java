package com.example.pillapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class pillDatabaseActivity extends AppCompatActivity {

    Database database;
    Button pillViewButton;
    Button historyButton;
    Button searchButton;
    EditText searchField;
    User user;
    ScrollView scroll;
    Button addNewPill;
    Button doctorsButton;
    public Bundle retrieve = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_database);

        //class variables
        database = Database.getInstance(getApplicationContext());
        pillViewButton = findViewById(R.id.yourPillsButton);
        scroll = findViewById(R.id.scroller);
        searchButton = findViewById(R.id.searchButton);
        searchField = findViewById(R.id.searchbar);
        addNewPill = findViewById(R.id.addNewPillButton);
        historyButton = findViewById(R.id.historyButton);
        doctorsButton = findViewById(R.id.DoctorButton);

        //buttons to move between views
        pillViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PillViewActivity.class);
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

        //getAllPills
        ArrayList<Pill> pills = new ArrayList<>();
        Cursor allPills = database.getAllPills();
        allPills.moveToNext();

        //put everything in an arraylist
        for (int i = 0; i < allPills.getCount(); i++) {
            pills.add(new Pill(allPills.getString(0), allPills.getInt(1)));
            allPills.moveToNext();
        }

        //create linear layout
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        int size = pills.size();
        display(layout, size, pills);

        //searchButton onclick
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //search through the database
                ArrayList<Pill> result = search(searchField.getText().toString(), pills,
                        0, size - 1);
                int resultSize = result.size();
                if (resultSize == 0) {
                    //if no results presented
                    layout.removeAllViews();
                    display(layout, size, pills);
                    float density = getApplicationContext().getResources().getDisplayMetrics().density;
                    int dp = (int) (density * 70);

                    //add buffer for scroll
                    TextView buffer = new TextView(getApplicationContext());
                    buffer.setHeight(dp);

                    layout.addView(buffer);
                    Toast.makeText(getApplicationContext(), "Pill isn't in database",
                            Toast.LENGTH_SHORT).show();
                } else if (resultSize == 1){
                    //if there is a result
                    layout.removeAllViews();
                    display(layout, resultSize, result);

                    //add reset Button
                    float density = getApplicationContext().getResources().getDisplayMetrics().density;
                    int dp = (int) (density * 10);
                    Button reset = new Button(getApplicationContext());
                    reset.setText("Reset Search");
                    reset.setPadding(dp,dp,dp,dp);
                    reset.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                    layout.addView(reset);

                    //reset search functionality
                    reset.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            layout.removeAllViews();
                            display(layout, size, pills);

                            float density = getApplicationContext().getResources().getDisplayMetrics().density;
                            int dp = (int) (density * 70);

                            //add buffer for scroll
                            TextView buffer = new TextView(getApplicationContext());
                            buffer.setHeight(dp);

                            layout.addView(buffer);
                        }
                    });
                }
            }
        });

        //to add another pill to the database if it does not exist already
        addNewPill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.removeAllViews();
                display(layout, 0, new ArrayList<Pill>());

                float density = getApplicationContext().getResources().getDisplayMetrics().density;
                int dp = (int) (density * 10);

                EditText pillName = new EditText(getApplicationContext());
                EditText pillTiming = new EditText(getApplicationContext());
                Button button = new Button(getApplicationContext());

                pillName.setHint("Name of Pill");
                pillName.setPadding(dp,dp,dp,dp);
                pillTiming.setInputType(InputType.TYPE_CLASS_TEXT);
                pillName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                layout.addView(pillName);

                pillTiming.setHint("How many hours between taking these pills");
                pillTiming.setPadding(dp,dp,dp,dp);
                pillTiming.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                pillTiming.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(pillTiming);

                button.setText("Add");
                button.setPadding(dp,dp,dp,dp);
                button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                layout.addView(button);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (pillName.getText().toString().length() == 0) {
                            Toast.makeText(getApplicationContext(), "Enter Pill Name",
                                    Toast.LENGTH_SHORT).show();
                        } else if (pillTiming.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Enter a valid timing",
                                    Toast.LENGTH_SHORT).show();
                        } else if (Integer.parseInt(pillTiming.getText().toString()) > 24 ||
                                Integer.parseInt(pillTiming.getText().toString()) <= 0) {
                            Toast.makeText(getApplicationContext(), "Enter a valid timing",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                database.addToDatabaseOfPills(pillName.getText().toString(),
                                        Integer.parseInt(pillTiming.getText().toString()));
                            } catch (android.database.sqlite.SQLiteConstraintException e) {
                                Toast.makeText(getApplicationContext(), "Already in Database",
                                        Toast.LENGTH_SHORT).show();
                            }

                            layout.removeAllViews();
                            display(layout, size, pills);

                            float density = getApplicationContext().getResources().getDisplayMetrics().density;
                            int dp = (int) (density * 70);

                            //add buffer for scroll
                            TextView buffer = new TextView(getApplicationContext());
                            buffer.setHeight(dp);

                            layout.addView(buffer);

                            //reload Activity
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        }
                    }
                });
            }
        });

        float density = this.getResources().getDisplayMetrics().density;
        int dp = (int) (density * 70);

        //add buffer for scroll
        TextView buffer = new TextView(this);
        buffer.setHeight(dp);

        layout.addView(buffer);

        scroll.addView(layout);
    }

    //set display
    public void display(LinearLayout layout, int size, ArrayList<Pill> pills) {
        for (int i = 0; i < size; i++) {
            //convert pixels to dp
            float density = this.getResources().getDisplayMetrics().density;
            int dp = (int) (density * 10);

            //set the text
            TextView textView = new TextView(this);
            String message = pills.get(i).pillName + " | Timing: " + pills.get(i).pillTiming + " hours";
            textView.setText(message);

            //set the styling
            textView.setPadding(dp,dp,dp,dp);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);

            //add button
            Button button = new Button(this);
            String placeholder = "Add " + pills.get(i).pillName + " to your list";
            button.setText(placeholder);
            button.setPadding(dp,dp,dp,dp);
            button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);

            //add button functionality
            String pillName = pills.get(i).pillName;
            int pillTiming = pills.get(i).pillTiming;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        database.addPillToUser(user.username, pillName, pillTiming);
                        Toast.makeText(getApplicationContext(), "Done",
                                Toast.LENGTH_SHORT).show();
                    } catch (android.database.sqlite.SQLiteConstraintException e) {
                        Toast.makeText(getApplicationContext(), "Pill already added",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            layout.addView(textView);
            layout.addView(button);
        }
    }

    //search bar functionality
    public ArrayList<Pill> search (String key, ArrayList<Pill> pills, int x, int y) {
        int size = pills.size();
        ArrayList<Pill> result = new ArrayList<>();
        if (y < size / 2) {
            return result;
        }
        if (pills.get(x).pillName.equals(key)) {
            result.add(pills.get(x));
            return result;
        }
        if (pills.get(y).pillName.equals(key)) {
            result.add(pills.get(y));
            return result;
        }
        else {
            return search(key, pills, x + 1, y - 1);
        }
    }
}