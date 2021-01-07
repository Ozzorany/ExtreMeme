package com.example.extrememe.services;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.extrememe.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginService {
    private static final String TAG = "LoginService";
    public static final int RC_SIGN_IN = 9001;
    private static LoginService loginService;
    private FirebaseAuth firebaseAuth;

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public void setFirebaseAuth(FirebaseAuth mAuth) {
        this.firebaseAuth = mAuth;
    }

    private GoogleSignInClient mGoogleSignInClient;

    public GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    public void setGoogleSignInClient(GoogleSignInClient mGoogleSignInClient) {
        this.mGoogleSignInClient = mGoogleSignInClient;
    }

    private GoogleSignInAccount googleAccount;

    public void setGoogleAccount(GoogleSignInAccount googleAccount) {
        this.googleAccount = googleAccount;
    }

    public GoogleSignInAccount getGoogleAccount() {
        return googleAccount;
    }

    private FirebaseUser firebaseUser;

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    private LoginService() {
    }

    public static LoginService getInstance(Context context) {
        if (loginService == null) {
            loginService = new LoginService();

            if (loginService.getGoogleSignInClient() == null) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(context.getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                loginService.setGoogleSignInClient(GoogleSignIn.getClient(context, gso));
                loginService.setGoogleAccount(GoogleSignIn.getLastSignedInAccount(context));
            }

            if (loginService.getFirebaseAuth() == null) {
                loginService.setFirebaseAuth(FirebaseAuth.getInstance());
            }

            if (loginService.getGoogleAccount() != null && loginService.getFirebaseUser() == null) {
                LoginService.getInstance(context).firebaseAuthWithGoogle(
                        loginService.getGoogleAccount().getIdToken(),
                        (Activity) context
                );
            }
        }

        return loginService;
    }

    public void firebaseAuthWithGoogle(String idToken, Activity activity) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        loginService.getFirebaseAuth().signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        loginService.setFirebaseUser(loginService.getFirebaseAuth().getCurrentUser());
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }
                });
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        loginService.setFirebaseUser(null);
        loginService.setGoogleAccount(null);
    }

    public boolean isLoggedIn() {
        return getGoogleAccount() != null && getFirebaseUser() != null;
    }
}
