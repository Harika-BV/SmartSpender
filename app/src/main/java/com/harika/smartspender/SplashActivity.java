package com.harika.smartspender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.harika.smartspender.database.SmartSpender_DB;
import com.harika.smartspender.models.ExpenseModel;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplashActivity extends AppCompatActivity {

    private MaterialButton getStartedButton;
    private ProgressDialog progressDialog;
    private SmartSpender_DB dbHelper;
    private static final int SMS_PERMISSION_REQUEST_CODE = 1;
    private Boolean isMessagesExtracted = false;
    private SmartSpender_DB smartSpender_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        smartSpender_db = new SmartSpender_DB(this);


        if (areSmsPermissionsGranted()) {
            extractMessages();
        } else {
            // Request SMS permissions if not granted
            requestSmsPermissions();
        }

        getStartedButton = findViewById(R.id.getStartedButton);
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areSmsPermissionsGranted()) {
                    if(!isMessagesExtracted)
                        extractMessages();
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                } else {
                    // Request SMS permissions if not granted
                    requestSmsPermissions();
                }
            }
        });

    }


    private void extractMessages () {
        dbHelper = new SmartSpender_DB(this);

        // In your onCreate method
        progressDialog = new ProgressDialog(SplashActivity.this);
        progressDialog.setMessage("Extracting Data...");
        progressDialog.show();

        retrieveSMS();

    }

    private boolean areSmsPermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    // Request SMS permissions
    private void requestSmsPermissions() {
        ActivityCompat.requestPermissions(SplashActivity.this, new String[]{android.Manifest.permission.READ_SMS}, SMS_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions were granted; you can now read SMS messages
                // Add your SMS processing logic here
            } else {
                // Permissions were not granted; you may display a message or take appropriate action
            }
        }
    }


    private void retrieveSMS() {
        Log.d("in retrieveSMS ", "retrieveSMS");
        Uri uri = Uri.parse("content://sms/inbox");

        // Get the ID of the last processed SMS from shared preferences or your database
        long lastProcessedSmsId = dbHelper.getLastProcessedSmsId(this);
        Log.d("lastProcessedSmsId", lastProcessedSmsId +" ");
        // Build a selection string to only retrieve messages with a higher ID than the last processed one
        String selection = "_id > ?";
        String[] selectionArgs = {String.valueOf(lastProcessedSmsId)};
        Cursor cursor = getContentResolver().query(uri, null, selection, selectionArgs, "_id ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int dateColumn = cursor.getColumnIndex("date");
                String sender = cursor.getString(cursor.getColumnIndex("address"));
                String messageBody = cursor.getString(cursor.getColumnIndex("body"));
                long smsId = cursor.getLong(cursor.getColumnIndex("_id")); // Assuming _id is the SMS message ID
                // Classify the message as Debit using keywords or regular expressions
                if (isDebitTransaction(messageBody) && !messageBody.contains("request")) {
                    Log.d("SS Sender ", sender);
                    Log.d("SS Message ", messageBody);
                    Log.d("SS Date ", dateColumn +" ");

                    // Identify the recipient (assuming recipient's name/number is mentioned in the message)
                    String recipient = findRecipient(messageBody);
                    long dateMillis = cursor.getLong(dateColumn);
                    String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(dateMillis));

                    // Now, you can use 'recipient' and 'messageBody' for further processing or display.

                    double amount = extractAmount(messageBody);
                    if (amount > 0) {
                        // The message is new, process it and insert it into the database
                        ExpenseModel expense = processExpenseData(sender, messageBody, amount, recipient, date);
                        if (expense != null) {
                            smartSpender_db.insertExpenseIntoDatabase(expense);
                            lastProcessedSmsId = smsId;
                            Log.d("smsId", smsId +" ");
                        }
                    }


                }
            } while (cursor.moveToNext());
            isMessagesExtracted = true;
            cursor.close();
        }

        // Update the last processed SMS message ID in shared preferences or your database
        dbHelper.setLastProcessedSmsId(this, lastProcessedSmsId);
        progressDialog.dismiss();
    }

    private double extractAmount(String messageBody) {
        // Regular expression to extract currency amount (e.g., "Rs. 25.00")
        Pattern pattern = Pattern.compile("Rs\\. (\\d+(\\.\\d{1,2}))");
        Matcher matcher = pattern.matcher(messageBody);

        if (matcher.find()) {
            String amountStr = matcher.group(1); // Capture the matched amount
            try {
                return Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                // Handle any parsing errors
                e.printStackTrace();
            }
        }

        return 0.0; // Default value if the amount is not found
    }

    private boolean isDebitTransaction(String messageBody) {
        // Check for keywords or regular expressions indicating a debit transaction
        String[] debitKeywords = {"debit", "withdrawal"};
        for (String keyword : debitKeywords) {
            if (messageBody.toLowerCase().contains(keyword)) {
                return true;
            }
        }
        // You can also use regular expressions to match specific patterns.

        return false;
    }

    private String findRecipient(String messageBody) {
        // Regular expression to extract the recipient after "to" and before the first dot (.)
        Pattern pattern = Pattern.compile("to (.*?)\\.");
        Matcher matcher = pattern.matcher(messageBody);

        if (matcher.find()) {
            return matcher.group(1).trim(); // Capture the text after "to" and before the first dot and trim any leading/trailing spaces
        }

        return "Recipient not found"; // Default value if the recipient is not found
    }

    private ExpenseModel processExpenseData(String sender, String messageBody, double amount,
                                            String recipient, String date) {
        // Implement your logic to extract expense data
        // Create and return an Expense object with the extracted data
        // For example:
        ExpenseModel expense = new ExpenseModel();
        expense.setSender(sender);
        expense.setMessageBody(messageBody);
        expense.setAmount(amount);
        expense.setCategory(recipient);
        expense.setDate(date);
        // Set other fields
        return expense;
    }




}