package com.example.coromap;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ownerLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private EditText mPhoneNumber;
    private  ProgressBar pbar;
    private Button mGenerateBtn;
    private TextView mLoginFeedbackText,mCountryCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login);
        mCountryCode = findViewById(R.id.code);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        pbar=findViewById(R.id.progressBar);
        pbar.setVisibility(View.INVISIBLE);

        mPhoneNumber = findViewById(R.id.number);
        mGenerateBtn = findViewById(R.id.get_started);

        mLoginFeedbackText = findViewById(R.id.warning);
        mLoginFeedbackText.setVisibility(View.INVISIBLE);
        mGenerateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String country_code = mCountryCode.getText().toString();
                String phone_number = mPhoneNumber.getText().toString();


                String complete_phone_number = country_code + phone_number;

                if(country_code.isEmpty()|| phone_number.isEmpty()||phone_number.length()<9){
                    mLoginFeedbackText.setTextColor(Color.parseColor("#ff7043"));
                    mLoginFeedbackText.setText("Please give a valid number");
                    mLoginFeedbackText.setVisibility(View.VISIBLE);
                } else {
                    pbar.setVisibility(View.VISIBLE);
                    mGenerateBtn.setEnabled(false);
                    mLoginFeedbackText.setTextColor(Color.parseColor("#00e676"));
                    mLoginFeedbackText.setText("Sending OTP, please wait.");
                    mLoginFeedbackText.setVisibility(View.VISIBLE);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            complete_phone_number,
                            60,
                            TimeUnit.SECONDS,
                            ownerLogin.this,
                            mCallbacks
                    );

                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
                pbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                String country_code = mCountryCode.getText().toString();
                String phone_number = mPhoneNumber.getText().toString();
                mLoginFeedbackText.setTextColor(Color.parseColor("#ff4081"));
                mLoginFeedbackText.setText(e.toString());
                mLoginFeedbackText.setVisibility(View.VISIBLE);
                pbar.setVisibility(View.INVISIBLE);
                mGenerateBtn.setEnabled(true);
            }

            @Override
            public void onCodeSent(final String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                mLoginFeedbackText.setTextColor(Color.parseColor("#00e676"));
                                mLoginFeedbackText.setText("Varifying OTP, please wait.");
                                mLoginFeedbackText.setVisibility(View.VISIBLE);
                                Intent otpIntent = new Intent(ownerLogin.this, otp.class);
                                otpIntent.putExtra("AuthCredentials", s);
                                pbar.setVisibility(View.INVISIBLE);
                                startActivity(otpIntent);

                            }
                        },
                        10000);
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mCurrentUser != null){
            sendUserToHome();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(ownerLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendUserToHome();
                            // ...
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                mLoginFeedbackText.setVisibility(View.VISIBLE);
                                mLoginFeedbackText.setText(task.getException().getMessage());

                            }
                        }
                        mGenerateBtn.setEnabled(true);
                        pbar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void sendUserToHome() {
        Intent homeIntent = new Intent(ownerLogin.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }


}
