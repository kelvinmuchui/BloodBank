package com.example.kelvin.blooddonation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kelvin.blooddonation.Model.Responds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RespondActivity extends AppCompatActivity {

    private Toolbar respondToolbar;

    private EditText responds_field;
    private ImageView responds_post_btn;

    private RecyclerView respond_list;
    private RespondRecyclerAdapter respondRecyclerAdapter;
    private List<Responds> respondsList;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String blog_post_id;
    private String current_user_id;
    private Button btnLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respond);

        respondToolbar = findViewById(R.id.respond_toolbar);
        setSupportActionBar(respondToolbar);
        getSupportActionBar().setTitle("Respond");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();
        blog_post_id = getIntent().getStringExtra("blog_post_id");

        responds_field = findViewById(R.id.comment_field);
        responds_post_btn = findViewById(R.id.respond_post_btn);
        respond_list = findViewById(R.id.respond_list);
        btnLocation = findViewById(R.id.btn_map);

        //RecyclerView Firebase List
        respondsList = new ArrayList<>();
        respondRecyclerAdapter = new RespondRecyclerAdapter(respondsList);
        respond_list.setHasFixedSize(true);
        respond_list.setLayoutManager(new LinearLayoutManager(this));
        respond_list.setAdapter(respondRecyclerAdapter);

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(RespondActivity.this, MapActivity.class);
                startActivity(mapIntent);

            }
        });


        firebaseFirestore.collection("Posts/" + blog_post_id + "/Responds")
                .addSnapshotListener(RespondActivity.this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                        if (!documentSnapshots.isEmpty()) {

                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                                if (doc.getType() == DocumentChange.Type.ADDED) {

                                    String commentId = doc.getDocument().getId();
                                    Responds comments = doc.getDocument().toObject(Responds.class);
                                    respondsList.add(comments);
                                    respondRecyclerAdapter.notifyDataSetChanged();


                                }
                            }

                        }

                    }
                });

        responds_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment_message = responds_field.getText().toString();


                Map<String, Object> commentsMap = new HashMap<>();
                commentsMap.put("message", comment_message);
                commentsMap.put("user_id", current_user_id);
                commentsMap.put("timestamp", FieldValue.serverTimestamp());

                firebaseFirestore.collection("Posts/" + blog_post_id + "/Responds").add(commentsMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        if(!task.isSuccessful()){

                            Toast.makeText(RespondActivity.this, "Error Posting Responds : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        } else {

                            responds_field.setText("");

                        }

                    }
                });

            }
        });


    }
}