package com.example.coromap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private Button register;
    TextView as;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        final Button register = findViewById(R.id.reg);
        final Button dregister = findViewById(R.id.doc);
        final Button profile = findViewById(R.id.button3);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId=firebaseAuth.getCurrentUser().getUid();
                firebaseFirestore.collection("userdetail").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            if(task.getResult().exists())
                            {

                                Intent intent = new Intent(MainActivity.this,registeredUser.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent = new Intent(MainActivity.this,registerUser.class);

                                startActivity(intent);
                            }
                        }
                    }
                });

            }
        });
        dregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,doctorRegister.class);

                startActivity(intent);
            }
        });


        if (user != null) {
            String userEmail = user.getPhoneNumber();

        } else {
            // No user is signed in
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.profile:
                Intent intent = new Intent(MainActivity.this, profile.class);
                startActivity(intent);
                return true;
            case R.id.FAQ:

                return true;
            case R.id.logout:

                  firebaseAuth.signOut();

                      Intent intent1=new Intent(MainActivity.this,login.class);
                     intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                      intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                      startActivity(intent1);
                      finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}