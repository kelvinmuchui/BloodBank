package com.example.kelvin.blooddonation;

import android.content.Context;
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

import com.example.kelvin.blooddonation.Model.User;
import com.example.kelvin.blooddonation.Model.UserAccountSetting;
import com.example.kelvin.blooddonation.Utills.FirebaseMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class RegisterActivity extends AppCompatActivity {


    private static final String TAG = "RegisterActivity";


    private Context mContext;
    private EditText mUsername;
    private EditText mLocation;
    private EditText mEmail;
    private EditText mPhonenumber;
    private EditText mPassword;
    private EditText mRePassword;
    private Button mReg;

    private TextView mLogin;
    private  String append = "";
    private String email, username, password;





    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        firebaseMethods = new FirebaseMethods(mContext);
        



        initWidgets();
        setupFirebaseAuth();
        init();


    }

    private void init() {
        mReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString();
                username = mUsername.getText().toString();
                password = mPassword.getText().toString();

                if(checkInputs(email, username, password)){


                    firebaseMethods.registerNewEmail(email, password, username);
                }
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent loginIntent =new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);

            }
        });
    }

    //Check if the flieds are empty
    private boolean checkInputs(String email, String username, String password){
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if(email.equals("") || username.equals("") || password.equals("")){
            Toast.makeText(mContext, "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean isStringNull(String string){
        Log.d(TAG, "isStringNull: checking string if null.");

        if(string.equals("")){
            return true;
        }
        else{
            return false;
        }
    }


    /**
     * Initialize the activity widgets
     */
    private void initWidgets(){
        Log.d(TAG, "initWidgets: Initializing Widgets.");
        mContext = RegisterActivity.this;
        mUsername = findViewById(R.id.username);
        mEmail = findViewById(R.id.email);
        mLocation = findViewById(R.id.location);
        mPhonenumber = findViewById(R.id.mobile);
        mPassword = findViewById(R.id.password);
        mRePassword = findViewById(R.id.reEnterPassword);
        mReg = findViewById(R.id.btn_signup);
        mLogin = findViewById(R.id.link_login);

    }

    /*
    ------------------------------------ Firebase ---------------------------------------------
     */ /**
     * Check is @param username already exists in the database
     * @param username
     */

    private void checkIfUsernameExists(final String username) {
        Log.d(TAG, "checkIfUsernameExists: checking if " + username +"already Exists");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    if(singleSnapshot.exists()){

                        append = mRef.push().getKey().substring(3,10);
                        Log.d(TAG, "onDataChange:  username arleady exists . Appending a random string to name" + append);

                    }
                }
                String mUsername = "";
                mUsername= username + append;


                //add new user to database
                firebaseMethods.addNewUser(email,mUsername,1,"");

                Toast.makeText(mContext, "Signup successfull sending verification email " , Toast.LENGTH_SHORT).show();


                mAuth.signOut();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            checkIfUsernameExists(username);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    finish();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }





}
