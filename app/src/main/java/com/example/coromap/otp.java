package com.example.coromap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class otp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private ProgressBar pbar2;
    private String mAuthVerificationId;
    private TextView otpt;
    private EditText mOtpText;
    private Button mVerifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        otpt=findViewById(R.id.wrong_otp);
        mAuthVerificationId = getIntent().getStringExtra("AuthCredentials");
        pbar2=findViewById(R.id.progressBar2);
        otpt.setVisibility(View.INVISIBLE);
        mOtpText = findViewById(R.id.otp);
        pbar2.setVisibility(View.INVISIBLE);
        mVerifyBtn = findViewById(R.id.verify);

        mVerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otp = mOtpText.getText().toString();

                if(otp.isEmpty()){
                    otpt.setText("OTP cannot be empty");
                    otpt.setVisibility(View.VISIBLE);

                } else {
                    pbar2.setVisibility(View.VISIBLE);
                    mVerifyBtn.setEnabled(false);

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthVerificationId, otp);
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(otp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pbar2.setVisibility(View.INVISIBLE);
                            sendUserToHome();
                            // ...
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                otpt.setText("Entered OTP is invalid, please try again.");
                                pbar2.setVisibility(View.INVISIBLE);
                                otpt.setVisibility(View.VISIBLE);
                            }
                        }
                        pbar2.setVisibility(View.INVISIBLE);
                        mVerifyBtn.setEnabled(true);
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(mCurrentUser != null){

            sendUserToHome();
        }
    }

    public void sendUserToHome() {
        Intent homeIntent = new Intent(otp.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }
}
