package com.application.quizly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListQuizActivity extends AppCompatActivity {

    ArrayList<Quiz> quizzes;
    private FirebaseDatabase aFirebaseDatabase;
    private DatabaseReference aDatabaseReference;
    private ChildEventListener aChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_quiz);

        FirebaseUtil.openFbReference("quizgames", this);

        aFirebaseDatabase = FirebaseUtil.aFirebaseDatabase;
        aDatabaseReference = FirebaseUtil.aDatabaseReference;
        aChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TextView tvQuizes = findViewById(R.id.tvQuizes);
                Quiz quiz = snapshot.getValue(Quiz.class);
                tvQuizes.setText(tvQuizes.getText() + " \n " + quiz.getCategory());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        aDatabaseReference.addChildEventListener(aChildEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUtil.attachListener();
    }
}