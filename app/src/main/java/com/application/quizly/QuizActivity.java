package com.application.quizly;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class QuizActivity extends AppCompatActivity {

    private FirebaseDatabase aFirebaseDatabase;
    private DatabaseReference aDatabaseReference;

    private static final int PICTURE_RESULT = 42;
    EditText txtTitle;
    EditText txtCategory;
    EditText txtDescription;
    ImageView imageView;
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
        imageView = findViewById(R.id.image);

        Intent intent = getIntent();
        Quiz quiz = (Quiz) intent.getSerializableExtra("Quiz");
        if (quiz == null) {
            quiz = new Quiz();
        }
        this.quiz = quiz;
        txtTitle.setText(quiz.getTitle());
        txtCategory.setText(quiz.getCategory());
        txtDescription.setText(quiz.getDescription());
        showImage(quiz.getImageUrl());
        Button btnImage = findViewById(R.id.btnImage);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent, "Insert Picture"), PICTURE_RESULT);
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            final StorageReference ref = FirebaseUtil.aStorageReference.child(imageUri.getLastPathSegment());
            ref.putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String url = ref.getDownloadUrl().toString();
                    //String url = taskSnapshot.getDownloadUrl().toString();
                    String pictureName = taskSnapshot.getStorage().getPath();
                    quiz.setImageUrl(url);
                    quiz.setImageName(pictureName);
                    Log.d("Url: ", url);
                    Log.d("Name", pictureName);
                    showImage(url);
                }
            });

        }
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

    private void showImage(String url) {
        if (url != null && url.isEmpty() == false) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get()
                    .load(url)
                    .resize(width, width*2/3)
                    .centerCrop()
                    .into(imageView);
        }
    }

}
