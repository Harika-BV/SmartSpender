package com.harika.smartspender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.harika.smartspender.SignUpActivities.SignUpPhoneNumberActivity;
import com.harika.smartspender.database.SmartSpender_DB;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton loginButton, signUpButton;
    private TextInputLayout phonenumber, password;
    private SmartSpender_DB smartSpender_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phonenumber = findViewById(R.id.phoneNumber);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);

        smartSpender_db = new SmartSpender_DB(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(smartSpender_db.validateLogin(phonenumber.getEditText().getText().toString()
                        , password.getEditText().getText().toString())) {
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }

                else {
                    Toast.makeText(LoginActivity.this,
                            "Please enter valid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpPhoneNumberActivity.class));
            }
        });
    }
}