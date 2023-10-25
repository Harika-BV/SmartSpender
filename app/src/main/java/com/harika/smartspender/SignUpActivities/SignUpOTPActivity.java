package com.harika.smartspender.SignUpActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.harika.smartspender.R;

import java.util.Random;

public class SignUpOTPActivity extends AppCompatActivity {

    private MaterialButton nextButton;
    private TextInputLayout otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_otp);

        nextButton = findViewById(R.id.nextButton);
        otp = findViewById(R.id.otp);
        String otpVal = randomgOTP();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otp.getEditText() != null && otp.getEditText().getText().toString().equals(otpVal)) {
                    startActivity(new Intent(SignUpOTPActivity.this, SignUpUserDetailsActivity.class));
                }
                else {
                    Toast.makeText(SignUpOTPActivity.this,
                            "Please enter the OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public String randomgOTP() {
        // Create a Random object
        Random random = new Random();

        // Generate a random 4-digit OTP
        int otp = random.nextInt(9000) + 1000;
        Toast.makeText(SignUpOTPActivity.this,
                " " + otp, Toast.LENGTH_SHORT).show();

        return String.valueOf(otp);
    }
}