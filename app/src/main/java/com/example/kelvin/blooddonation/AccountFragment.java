package com.example.kelvin.blooddonation;


import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {


    private TextView mUsername, mLocation, mBloodgroup, mGender,mPhonenumber,mEmail;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private DocumentSnapshot lastVisible;



    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        // Inflate the layout for this fragment

         View view = inflater.inflate(R.layout.fragment_account, container, false);

         mUsername = view.findViewById(R.id.name);
         mLocation = view.findViewById(R.id.location);
         mBloodgroup = view.findViewById(R.id.blood_group);
         mGender = view.findViewById(R.id.gender);
         mPhonenumber = view.findViewById(R.id.phone_number);
         mEmail = view.findViewById(R.id.education);

         firebaseFirestore.collection("User").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
             @Override
             public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                 String name = documentSnapshot.getString("name");
                 String blood = documentSnapshot.getString("blood_group");
                 String gender = documentSnapshot.getString("phone_number");
                 String weight = documentSnapshot.getString("weight");

                 mUsername.setText(name);

                 mGender.setText(gender);


                 mLocation.setText(weight);






             }
         });
        return view;


    }

}
