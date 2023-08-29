package com.application.quizly;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
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
    private static Activity caller;

    private FirebaseUtil() {}

    public static void openFbReference(String ref, final Activity callerActivity) {
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
                        Toast.makeText(callerActivity.getBaseContext(), "Welcome back", Toast.LENGTH_SHORT).show();
                }
            };
        }
        aQuizes = new ArrayList<Quiz>();
        aDatabaseReference = aFirebaseDatabase.getReference().child(ref);
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
