package com.example.extrememe.services;

import android.app.Activity;
import android.content.Context;

import com.example.extrememe.R;
import com.example.extrememe.model.User;
import com.example.extrememe.model.user.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
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


    public FirebaseUser getFirebaseUser() {
        return getFirebaseAuth().getCurrentUser();
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

    public Task<AuthResult> firebaseAuthWithGoogle(String idToken, Activity activity) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        return loginService.getFirebaseAuth().signInWithCredential(credential);
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        loginService.setGoogleAccount(null);
    }

    public boolean isLoggedIn() {
        return getGoogleAccount() != null && getFirebaseUser() != null;
    }

    public void createNewUser(Task<AuthResult> authResultTask){
        if(authResultTask.getResult().getAdditionalUserInfo().isNewUser())
        {
            User user = new User();
            user.setId(authResultTask.getResult().getUser().getUid());
            user.setAdmin(false);
            user.setUsername(authResultTask.getResult().getUser().getDisplayName());

            UserModel.instance.createUser(user);
        }
    }
}
