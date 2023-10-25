package com.harika.smartspender.SignUpActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.harika.smartspender.R;

public class SignUpPhoneNumberActivity extends AppCompatActivity {

    private MaterialButton sendOTPButton;
    private TextInputLayout phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_phone_number);

        sendOTPButton = findViewById(R.id.sendOTPButton);
        phoneNumber = findViewById(R.id.phoneNumber);


        sendOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phoneNumber.getEditText() !=null) {
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("phonenumber", phoneNumber.getEditText().getText().toString());
                    editor.apply();

                    startActivity(new Intent(SignUpPhoneNumberActivity.this, SignUpOTPActivity.class));
                }
                else {
                    Toast.makeText(SignUpPhoneNumberActivity.this,
                            "Please enter phone number", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}