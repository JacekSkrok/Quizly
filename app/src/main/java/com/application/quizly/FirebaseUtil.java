package com.application.quizly;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {
    public static FirebaseDatabase aFirebaseDatabase;
    public static DatabaseReference aDatabaseReference;
    private static FirebaseUtil firebaseUtil;
    public static FirebaseAuth aFirebaseAuth;
    public static FirebaseAuth.AuthStateListener aAuthListener;
    public static ArrayList<Quiz> aQuizes;

    private static final int RC_SIGN_IN = 123;
    private static ListQuizActivity caller;

    public static boolean isAdmin;

    private FirebaseUtil() {}

    public static void openFbReference(String ref, final ListQuizActivity callerActivity) {
        if( firebaseUtil == null ) {
            firebaseUtil = new FirebaseUtil();
            aFirebaseDatabase = FirebaseDatabase.getInstance();
            aFirebaseAuth = FirebaseAuth.getInstance();
            caller = callerActivity;
            aAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if( firebaseAuth.getCurrentUser() == null) {
                        FirebaseUtil.signIn();
                    }
                    else {
                        String userId = firebaseAuth.getUid();
                        checkAdmin(userId);
                    }
                    Toast.makeText(callerActivity.getBaseContext(), "Welcome back", Toast.LENGTH_SHORT).show();
                }
            };
        }
        aQuizes = new ArrayList<Quiz>();
        aDatabaseReference = aFirebaseDatabase.getReference().child(ref);
    }

    private static void checkAdmin(String uId) {
        FirebaseUtil.isAdmin = false;
        DatabaseReference ref = aFirebaseDatabase.getReference().child("administrators")
                .child(uId);
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FirebaseUtil.isAdmin = true;
                caller.showMenu();
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
        ref.addChildEventListener(listener);
    }

    private static void signIn() {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            caller.startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(), RC_SIGN_IN);

    }

    public static void attachListener() {
        aFirebaseAuth.addAuthStateListener(aAuthListener);
    }

    public static void detachListener() {
        aFirebaseAuth.removeAuthStateListener(aAuthListener);
    }

}
