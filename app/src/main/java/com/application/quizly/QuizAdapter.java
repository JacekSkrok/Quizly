package com.application.quizly;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {
    ArrayList<Quiz> quizzes;
    private FirebaseDatabase aFirebaseDatabase;
    private DatabaseReference aDatabaseReference;
    private ChildEventListener aChildEventListener;

    private ImageView imageQuiz;

    public QuizAdapter() {
        //FirebaseUtil.openFbReference("quizgames");
        aFirebaseDatabase = FirebaseUtil.aFirebaseDatabase;
        aDatabaseReference = FirebaseUtil.aDatabaseReference;
        quizzes = FirebaseUtil.aQuizes;
        aChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Quiz quiz = snapshot.getValue(Quiz.class);
                //Log.d("Quiz game", quiz.getTitle());
                quiz.setId(snapshot.getKey());
                quizzes.add(quiz);
                notifyItemInserted(quizzes.size() - 1);
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

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                        .inflate(R.layout.rv_row, parent, false);
        return new QuizViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizzes.get(position);
        holder.bind(quiz);
    }

    @Override
    public int getItemCount() {
        return quizzes.size();
    }

    public class QuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTitle;
        TextView tvCategory;
        TextView tvDescription;
        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            imageQuiz = itemView.findViewById(R.id.imageQuiz);
            itemView.setOnClickListener(this);
        }

        public void bind(Quiz quiz) {
            tvTitle.setText(quiz.getTitle());
            tvCategory.setText(quiz.getCategory());
            tvDescription.setText(quiz.getDescription());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Log.d("Click", String.valueOf(position));
            Quiz selectedQuiz = quizzes.get(position);
            Intent intent = new Intent(view.getContext(), QuizActivity.class);
            intent.putExtra("Quiz", selectedQuiz);
            view.getContext().startActivity(intent);

        }

        private void showImage(String url) {
            if (url != null && url.isEmpty()==false) {
                Picasso.get()
                        .load(url)
                        .resize(160, 160)
                        .centerCrop()
                        .into(imageQuiz);
            }
        }
    }
}
