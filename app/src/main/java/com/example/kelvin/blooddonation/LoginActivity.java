package com.example.kelvin.blooddonation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";



    private Button mLogin;
    private FirebaseAuth mAuth;

    private EditText mEmail;
    private EditText mPassword;

    private TextView  mSiginUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Intilalizing  widgets
        mLogin = findViewById(R.id.btn_login);
        mPassword = findViewById(R.id.input_password);
        mEmail = findViewById(R.id.input_email);
        mSiginUp = findViewById(R.id.link_signup);


        // Login in user

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginEmail = mEmail.getText().toString();
                String loginPass = mPassword.getText().toString();

                if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass)){


                    mAuth.signInWithEmailAndPassword(loginEmail, loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                Intent mainIntent = new Intent(LoginActivity.this , MainActivity.class);
                                startActivity(mainIntent);
                                finish();

                            } else {

                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();


                            }



                        }
                    });

                }


            }
        });


        // On link Sign up click
        mSiginUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntet = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntet);
                finish();
            }
        });
    }
}
