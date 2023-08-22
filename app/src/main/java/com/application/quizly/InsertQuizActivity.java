package com.application.quizly;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertQuizActivity extends AppCompatActivity {

    private FirebaseDatabase aFirebaseDatabase;
    private DatabaseReference aDatabaseReference;

    EditText txtTitle;
    EditText txtCategory;
    EditText txtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_quiz);

        aFirebaseDatabase = FirebaseDatabase.getInstance();
        aDatabaseReference = aFirebaseDatabase.getReference().child("quizgames");

        txtTitle = findViewById(R.id.txtTitle);
        txtCategory = findViewById(R.id.txtCategory);
        txtDescription = findViewById(R.id.txtDescription);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu:
                saveQuiz();
                Toast.makeText(this, "Quiz was saved",Toast.LENGTH_LONG).show();
                clean();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveQuiz() {
        String title = txtTitle.getText().toString();
        String category = txtCategory.getText().toString();
        String description = txtDescription.getText().toString();
        Quiz quiz = new Quiz(title, category, description, "");
        aDatabaseReference.push().setValue(quiz);
    }

    private void clean() {
        txtTitle.setText("");
        txtDescription.setText("");
        txtCategory.setText("");
        txtTitle.requestFocus();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }
}
