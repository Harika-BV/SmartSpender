package com.harika.smartspender.database;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.harika.smartspender.models.ExpenseModel;
import com.harika.smartspender.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class SmartSpender_DB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_spender.db";
    private static final int DATABASE_VERSION = 2;

    // Define your table and column names
    private static final String TABLE_EXPENSES = "expenses";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SENDER = "sender";
    private static final String COLUMN_MESSAGE_BODY = "message_body";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_UNIX_DATE = "unix_date";

    private static final String USERS = "users";
    private static final String COLUMN_FULLNAME = "fullname";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONENUMBER = "phonenumber";
    private static final String COLUMN_PASSWORD = "password";

    // SQL statement to create the expenses table
    private static final String EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SENDER + " TEXT, " +
            COLUMN_MESSAGE_BODY + " TEXT, " +
            COLUMN_AMOUNT + " REAL, " +
            COLUMN_DATE + " TEXT, " +
            COLUMN_UNIX_DATE + " INTEGER, " +
            COLUMN_CATEGORY + " TEXT);";

    private static final String USERS_TABLE = "CREATE TABLE " + USERS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_FULLNAME + " TEXT, " +
            COLUMN_EMAIL + " TEXT, " +
            COLUMN_PHONENUMBER + " TEXT, " +
            COLUMN_PASSWORD + " TEXT);";

    public SmartSpender_DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // Create the expenses table
        database.execSQL(EXPENSES_TABLE);
        database.execSQL(USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades, if needed
        // Typically, you'd modify the database schema here
    }

    public void setLastProcessedSmsId(Context context, long smsId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("last_processed_sms_id", smsId);
        editor.apply();
    }

    public long getLastProcessedSmsId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getLong("last_processed_sms_id", -1);
    }

    public void insertExpenseIntoDatabase(ExpenseModel expense) {
        // Use your database helper class to insert the expense into the database
        // For example, if you are using SQLiteOpenHelper:
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sender", expense.getSender());
        values.put("message_body", expense.getMessageBody());
        values.put("amount", expense.getAmount());
        values.put("category", expense.getCategory());
        values.put("date", expense.getDate());
        values.put("unix_date", expense.getUnixDate(expense.getDate()));
        // Insert the values into the database
        db.insert("expenses", null, values);
        db.close();
    }

    public void insertUserIntoDatabase(UserModel userModel) {
        // Use your database helper class to insert the expense into the database
        // For example, if you are using SQLiteOpenHelper:
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fullname", userModel.getFullName());
        values.put("email", userModel.getEmail());
        values.put("phonenumber", userModel.getPhonenumber());
        values.put("password", userModel.getPassword());
        // Insert the values into the database
        db.insert("users", null, values);
        db.close();
    }

    public boolean validateLogin(String phoneNumber, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query( USERS,
                new String[] {COLUMN_ID},
                COLUMN_PHONENUMBER + " = ? AND " +
                        COLUMN_PASSWORD + " = ?",
                new String[] {phoneNumber, password},
                null,
                null,
                null
        );

        boolean isValid = cursor.moveToFirst();
        cursor.close();
        db.close();

        return isValid;
    }


    public double getExpenseOfThisMonth (String currentYearMonth) {
        String query = "SELECT SUM(amount) FROM expenses WHERE SUBSTR(date, 4, 7) = '" + currentYearMonth + "'";
        Log.d("Query ", query);
        // Execute the query and retrieve the total expenditure
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        double totalExpenditure = 0.0;

        if (cursor.moveToFirst()) {
            totalExpenditure = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return totalExpenditure;
    }

    public Cursor getRecentTransactions(int count) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT date, amount, sender, category FROM expenses ORDER BY unix_date DESC LIMIT " + count;
        return db.rawQuery(query, null);
    }

    public float getTotalMonthlyExpense() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(amount) AS amount " +
                "FROM expenses " +
                "WHERE substr(date,4,2) = strftime('%m', 'now')";
        Log.d("Query", query);
        Cursor cursor = db.rawQuery(query, null);

        float totalMonthlyExpense = 0;
        if (cursor.moveToFirst()) {
            totalMonthlyExpense = cursor.getFloat(0);
        }

        cursor.close();
        db.close();
        return totalMonthlyExpense;
    }

    public float getTotalYearlyExpense() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(amount) AS amount " +
                "FROM expenses " +
                "WHERE substr(date, 7,4) = strftime('%Y', 'now')";
        Cursor cursor = db.rawQuery(query, null);

        float totalYearlyExpense = 0;
        if (cursor.moveToFirst()) {
            totalYearlyExpense = cursor.getFloat(0);
        }

        cursor.close();
        db.close();
        return totalYearlyExpense;
    }

    public List<Float> getMonthlyExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Float> monthlyExpenses = new ArrayList<>();

        // Loop through the months from January (1) to December (12)
        for (int month = 1; month <= 12; month++) {
            String query = "SELECT SUM(amount) AS totalMonthlyExpense " +
                    "FROM expenses " +
                    "WHERE substr(date,4,2) = ? AND substr(date, 7,4) = strftime('%Y', 'now')";

            Cursor cursor = db.rawQuery(query, new String[]{String.format("%02d", month)});

            if (cursor.moveToFirst()) {
                float monthlyExpense = cursor.getFloat(0);
                monthlyExpenses.add(monthlyExpense);
            }

            cursor.close();
        }

        db.close();
        return monthlyExpenses;
    }
}

