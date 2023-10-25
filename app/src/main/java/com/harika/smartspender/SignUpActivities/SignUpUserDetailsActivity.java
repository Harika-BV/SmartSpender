package com.harika.smartspender.SignUpActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.harika.smartspender.LoginActivity;
import com.harika.smartspender.R;
import com.harika.smartspender.database.SmartSpender_DB;
import com.harika.smartspender.models.UserModel;

public class SignUpUserDetailsActivity extends AppCompatActivity {

    private MaterialButton nextButton;
    private TextInputLayout fullName, email, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user_details);

        nextButton = findViewById(R.id.nextButton);
        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullName.getEditText() == null) {
                    Toast.makeText(SignUpUserDetailsActivity.this,
                            "Please enter the full name", Toast.LENGTH_SHORT).show();
                }
                else if (email.getEditText() == null) {
                    Toast.makeText(SignUpUserDetailsActivity.this,
                            "Please enter the email", Toast.LENGTH_SHORT).show();
                }
                else if (password.getEditText() == null) {
                    Toast.makeText(SignUpUserDetailsActivity.this,
                            "Please enter the password", Toast.LENGTH_SHORT).show();
                }
                else if (confirmPassword.getEditText() == null) {
                    Toast.makeText(SignUpUserDetailsActivity.this,
                            "Please enter the confirm password", Toast.LENGTH_SHORT).show();
                }
                else if (password.getEditText().getText() == confirmPassword.getEditText().getText()) {
                    Toast.makeText(SignUpUserDetailsActivity.this,
                            "Confirm password don't match with password", Toast.LENGTH_SHORT).show();
                }
                else {
                    UserModel userModel = new UserModel();
                    userModel.setFullName(fullName.getEditText().getText().toString());
                    userModel.setEmail(email.getEditText().getText().toString());
                    userModel.setPassword(password.getEditText().getText().toString());
                    userModel.setPhonenumber(sharedPreferences.getString("phonenumber", "-1"));

                    SmartSpender_DB smartSpender_db = new SmartSpender_DB(SignUpUserDetailsActivity.this);
                    smartSpender_db.insertUserIntoDatabase(userModel);
                    Toast.makeText(SignUpUserDetailsActivity.this,
                            "Account created successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpUserDetailsActivity.this, LoginActivity.class));
                }
            }
        });

    }
}