package com.example.pillapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;


public class Database extends SQLiteOpenHelper {

    private static Database instance = null;
    private Database(Context context) {
        super(context, "informationDatabase.db", null, 1);
    }

    //singleton implementation
    public static Database getInstance(Context context){
        if(instance == null){
            instance = new Database(context);
        }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {}
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    public void insertNewUser(String username, String password, String firstName, String lastName,
                              int age, int weight, int height) {
        SQLiteDatabase database = this.getWritableDatabase();

        database.execSQL("create Table IF NOT EXISTS users(username TEXT unique primary key," +
                "password TEXT, firstName TEXT, lastName TEXT, age INTEGER, weight INTEGER," +
                "height INTEGER)");

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("firstName", firstName);
        values.put("lastName", lastName);
        values.put("age", age);
        values.put("weight", weight);
        values.put("height", height);

        try {
            database.insertOrThrow("users", null, values);
        } catch (android.database.sqlite.SQLiteConstraintException e) {
            throw e;
        }
    }

    public Cursor getLoginInformation(String username) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("create Table IF NOT EXISTS users(username TEXT unique primary key," +
                "password TEXT, firstName TEXT, lastName TEXT, age INTEGER, weight INTEGER," +
                "height INTEGER)");
        String[] enteredUsername = {username};
        return database.rawQuery("SELECT password FROM users WHERE username = ?", enteredUsername);
    }

    public Cursor getUser(String username) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("create Table IF NOT EXISTS users(username TEXT unique primary key," +
                "password TEXT, firstName TEXT, lastName TEXT, age INTEGER, weight INTEGER," +
                "height INTEGER)");
        String[] enteredUsername = {username};
        return database.rawQuery("SELECT username, password, firstName, lastName, age, weight," +
                " height FROM users WHERE username = ?", enteredUsername);
    }

    public Boolean usernameExists(String username) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("create Table IF NOT EXISTS users(username TEXT unique primary key," +
                "password TEXT, firstName TEXT, lastName TEXT, age INTEGER, weight INTEGER," +
                "height INTEGER)");
        String[] enteredUsername = {username};
        if (database.rawQuery("SELECT password FROM users WHERE username = ?", enteredUsername)
                .getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void addPillToUser(String username, String pillName, int pillTiming) {
        SQLiteDatabase database = this.getWritableDatabase();

        database.execSQL("create Table IF NOT EXISTS userPills(username TEXT," +
                "pillName TEXT unique primary key, pillTiming INTEGER)");

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("pillName", pillName);
        values.put("pillTiming", pillTiming);

        try {
            database.insertOrThrow("userPills", null, values);
        } catch (android.database.sqlite.SQLiteConstraintException e) {
            throw e;
        }
    }

    public Cursor getUserPills (String username) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("create Table IF NOT EXISTS userPills(username TEXT," +
                "pillName TEXT unique primary key, pillTiming INTEGER)");
        String[] selection = {username};
        return database.rawQuery("SELECT pillName, pillTiming FROM userPills WHERE username = ?", selection);
    }

    public void deleteUserPill(String username, String pillName) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("create Table IF NOT EXISTS userPills(username TEXT," +
                "pillName TEXT unique primary key, pillTiming INTEGER)");
        String[] selection = {username, pillName};
        database.execSQL("DELETE FROM userPills WHERE (username = ? AND pillName = ?)", selection);
    }

    public void addToDatabaseOfPills(String pillName,int pillTiming) {
        SQLiteDatabase database = this.getWritableDatabase();

        database.execSQL("create Table IF NOT EXISTS allPills(pillName TEXT unique primary key, " +
                "pillTiming INTEGER)");

        ContentValues values = new ContentValues();
        values.put("pillName", pillName);
        values.put("pillTiming", pillTiming);

        try {
            database.insertOrThrow("allPills", null, values);
        } catch (android.database.sqlite.SQLiteConstraintException e) {
            throw e;
        }
    }

    public Cursor getAllPills() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("create Table IF NOT EXISTS allPills(pillName TEXT unique primary key, " +
                "pillTiming INTEGER)");

        return database.rawQuery("SELECT * FROM allPills", null);
    }

    public void addConsumption(String username, String pillName,int pillTiming, int hourTaken, int minTaken) {
        SQLiteDatabase database = this.getWritableDatabase();

        database.execSQL("create Table IF NOT EXISTS consumptions(username TEXT, pillName TEXT, " +
                "pillTiming INTEGER, hourTaken INTEGER, minTaken INTEGER)");

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("pillName", pillName);
        values.put("pillTiming", pillTiming);
        values.put("hourTaken", hourTaken);
        values.put("minTaken", minTaken);

        try {
            database.insertOrThrow("consumptions", null, values);
        } catch (android.database.sqlite.SQLiteConstraintException e) {
            throw e;
        }
    }

    public Cursor getConsumption(String username) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("create Table IF NOT EXISTS consumptions(username TEXT, pillName TEXT, " +
                "pillTiming INTEGER, hourTaken INTEGER, minTaken INTEGER)");
        String[] selection = {username};
        return database.rawQuery("SELECT pillName, pillTiming, hourTaken, minTaken" +
                " FROM consumptions WHERE username = ?", selection);
    }

    public void addAppointment(String username, String name, int year, int month, int day, int hour, int min) {
        SQLiteDatabase database = this.getWritableDatabase();

        database.execSQL("create Table IF NOT EXISTS doctors(username TEXT, name TEXT," +
                "year INTEGER, month INTEGER, day INTEGER, hour INTEGER, min INTEGER)");

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("name", name);
        values.put("year", year);
        values.put("month", month);
        values.put("day", day);
        values.put("hour", hour);
        values.put("min", min);

        try {
            database.insertOrThrow("doctors", null, values);
        } catch (android.database.sqlite.SQLiteConstraintException e) {
            throw e;
        }
    }

    public void deleteAppointment(String username, String name, int year, int month, int day, int hour, int min) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("create Table IF NOT EXISTS doctors(username TEXT, name TEXT," +
                "year INTEGER, month INTEGER, day INTEGER, hour INTEGER, min INTEGER)");
        Object[] selection = {username, name, year, month, day, hour, min};
        database.execSQL("DELETE FROM doctors " +
                "WHERE (username = ? AND name = ? AND year = ? AND month = ? and day = ? AND hour =?" +
                        "AND min = ?)",
                selection);
    }

    public Cursor getAppointments(String username) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("create Table IF NOT EXISTS doctors(username TEXT, name TEXT," +
                "year INTEGER, month INTEGER, day INTEGER, hour INTEGER, min INTEGER)");

        String[] selection = {username};
        return database.rawQuery("SELECT name, year, month, day, hour, min" +
                " FROM doctors WHERE username = ?", selection);
    }
}
