package com.example.coromap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class profile extends AppCompatActivity implements View.OnClickListener {

    Button cancleBtn, editBtn,saveBtn;
    ImageView nameEditBtn, addressEditBtn, emailEditBtn;
    EditText name, address, email;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //firebase

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        userID=firebaseAuth.getCurrentUser().getUid();
        nameEditBtn = findViewById(R.id.nameEditButton);
        cancleBtn = findViewById(R.id.cancelBtn);
        editBtn = findViewById(R.id.editBtn);
        addressEditBtn = findViewById(R.id.addressEditBtn);
        emailEditBtn = findViewById(R.id.emailEditBtn);

        name = findViewById(R.id.userName);
        address = findViewById(R.id.addreesEdit);
        email = findViewById(R.id.emailEdit);

        nameEditBtn.setOnClickListener(this);
        addressEditBtn.setOnClickListener(this);
        emailEditBtn.setOnClickListener(this);
        cancleBtn.setOnClickListener(this);

        //setting number
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userNumber = user.getPhoneNumber();
            TextView number=findViewById(R.id.number);
            number.setText(userNumber);

        } else {
            // No user is signed in
        }
        firebaseFirestore.collection("user").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        String  userName=task.getResult().getString("userName");
                        String  addressUser=task.getResult().getString("userAddress");
                        String  userEmail=task.getResult().getString("userEmail");
                        name.setText(userName);
                        address.setText(addressUser);
                        email.setText(userEmail);

                    }
                    else
                    {
                        //no data exists
                    }

                }
                else{
                    //failed
                }
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancleBtn.setVisibility(View.GONE);
                editBtn.setVisibility(View.GONE);
                name.setEnabled(false);
                address.setEnabled(false);
                email.setEnabled(false);

                final Map<String,String> userData= new HashMap<>();
                userData.put("userName",name.getText().toString().trim());
                userData.put("userAddress",address.getText().toString().trim());
                userData.put("userEmail",email.getText().toString().trim());
                userData.put("userID",userID);
                if(name.getText().toString().trim().length()==0||address.getText().toString().trim().length()==0||email.getText().toString().trim().length()==0)
                {
                    Toast.makeText(profile.this,"Fields can not be empty",Toast.LENGTH_LONG).show();
                }
                else{
                    firebaseFirestore.collection("user").document(userID).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                firebaseFirestore.collection("user").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful())
                                        {
                                            if(task.getResult().exists())
                                            {
                                                String  userName=task.getResult().getString("userName");
                                                String  addressUser=task.getResult().getString("userAddress");
                                                String  userEmail=task.getResult().getString("userEmail");
                                                name.setText(userName);
                                                address.setText(addressUser);
                                                email.setText(userEmail);

                                            }
                                            else
                                            {
                                                //no data exists
                                            }

                                        }
                                        else{
                                            //failed
                                        }
                                    }
                                });
                            }
                            else{
                                //failed
                            }
                        }
                    });
                }

            }
        });

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.nameEditButton){
            cancleBtn.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.VISIBLE);
            name.setEnabled(true);
            address.setEnabled(false);
            email.setEnabled(false);
        }

        if(view.getId() == R.id.addressEditBtn){
            cancleBtn.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.VISIBLE);
            name.setEnabled(false);
            address.setEnabled(true);
            email.setEnabled(false);
        }

        if(view.getId() == R.id.emailEditBtn){
            cancleBtn.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.VISIBLE);
            name.setEnabled(false);
            address.setEnabled(false);
            email.setEnabled(true);
        }

        if(view.getId() == R.id.cancelBtn){
            cancleBtn.setVisibility(View.GONE);
            editBtn.setVisibility(View.GONE);
            name.setEnabled(false);
            address.setEnabled(false);
            email.setEnabled(false);
        }


    }
}
