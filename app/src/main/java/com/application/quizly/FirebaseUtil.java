package com.application.quizly;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {
    private static FirebaseUtil firebaseUtil;
    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseAuth.AuthStateListener mAuthListener;

    private static final int RC_SIGN_IN = 123;
    private static Activity caller;

    private FirebaseUtil() {};

    public static void openFbReference(String ref, Activity callerActivity) {
        if( firebaseUtil == null ) {
            mFirebaseAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build());

                    caller = callerActivity;

                    caller.startActivityForResult(
                            AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(), RC_SIGN_IN);
                }
            };
        }
    }

    public static void attachListener() {
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    public static void detachListener() {
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }
}
