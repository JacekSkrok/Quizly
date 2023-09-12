package com.application.quizly;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QuizActivity extends AppCompatActivity {

    private FirebaseDatabase aFirebaseDatabase;
    private DatabaseReference aDatabaseReference;

    EditText txtTitle;
    EditText txtCategory;
    EditText txtDescription;

    Quiz quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_quiz);
        aFirebaseDatabase = FirebaseUtil.aFirebaseDatabase;
        aDatabaseReference = FirebaseUtil.aDatabaseReference;

        txtTitle = findViewById(R.id.txtTitle);
        txtCategory = findViewById(R.id.txtCategory);
        txtDescription = findViewById(R.id.txtDescription);

        Intent intent = getIntent();
        Quiz quiz = (Quiz) intent.getSerializableExtra("Quiz");
        if (quiz == null) {
            quiz = new Quiz();
        }
        this.quiz = quiz;
        txtTitle.setText(quiz.getTitle());
        txtCategory.setText(quiz.getCategory());
        txtDescription.setText(quiz.getDescription());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu:
                saveQuiz();
                Toast.makeText(this, "Quiz was saved",Toast.LENGTH_LONG).show();
                clean();
                return true;
            case R.id.delete_menu:
                deleteQuiz();
                Toast.makeText(this, "Quiz was deleted", Toast.LENGTH_LONG).show();
                clean();
                backToList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        if (FirebaseUtil.isAdmin == true) {
            menu.findItem(R.id.delete_menu).setVisible(true);
            menu.findItem(R.id.save_menu).setVisible(true);
            enableEditTexts(true);
        } else {
            menu.findItem(R.id.delete_menu).setVisible(false);
            menu.findItem(R.id.save_menu).setVisible(false);
            enableEditTexts(false);
        }
        return true;
    }
    private void saveQuiz() {
        quiz.setTitle(txtTitle.getText().toString());
        quiz.setCategory(txtCategory.getText().toString());
        quiz.setDescription(txtDescription.getText().toString());
        if(quiz.getId() == null) {
            aDatabaseReference.push().setValue(quiz);
        }
        else {
            aDatabaseReference.child(quiz.getId()).setValue(quiz);
        }
    }
    private void deleteQuiz() {
        if (quiz == null) {
            Toast.makeText(this, "Please save the quiz first", Toast.LENGTH_SHORT).show();
            return;
        }
        aDatabaseReference.child(quiz.getId()).removeValue();
    }
    private void backToList() {
        Intent intent = new Intent(this, ListQuizActivity.class);
        startActivity(intent);
    }
    private void clean() {
        txtTitle.setText("");
        txtDescription.setText("");
        txtCategory.setText("");
        txtTitle.requestFocus();
    }
    private void enableEditTexts(boolean isEnabled) {
        txtTitle.setEnabled(isEnabled);
        txtCategory.setEnabled(isEnabled);
        txtDescription.setEnabled(isEnabled);

    }
}
