package com.example.newsapplication;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;


public class Login extends Fragment {
    Button loginButton;
    ImageView googleSignIn;
    LoginButton facebookSignIn;
    EditText Email, Password;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    String sEmail,sPassword, sUid;
    CallbackManager callbackManager;
    private static final String TAG1 = "FacebookAuthentication";
    private AccessTokenTracker accessTokenTracker;


    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN =100;
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_login, container, false);
       FacebookSdk.sdkInitialize(getContext());

        // Binding the views
       Email  = (EditText) view.findViewById(R.id.editTextEmailAddress);
       Password = (EditText) view.findViewById(R.id.editTextPassword);
       loginButton = (Button) view.findViewById(R.id.login);
       googleSignIn = (ImageView) view.findViewById(R.id.googleicon);
       facebookSignIn = (LoginButton) view.findViewById(R.id.facebookicon);

       // Setting permissions
       facebookSignIn.setReadPermissions("email", "public_profile");

       // Initializing varailbles
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        callbackManager = CallbackManager.Factory.create();

        // Google Sign-In
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(getContext(),googleSignInOptions);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sEmail = Email.getText().toString();
                sPassword = Password.getText().toString();

                if (sEmail.isEmpty()) {
                    Email.setError("Email Address is required");
                    return;
                }

                if (sPassword.isEmpty()) {
                    Password.setError("Not a valid Password");
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(sEmail,sPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getContext(),"Logged In",Toast.LENGTH_SHORT).show();
                        Intent sendIntent = new Intent(getContext(), HomeActivity.class);
                        startActivity(sendIntent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Error while signing=",e.getMessage());
                        Toast.makeText(getContext(),"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        // Listener for google signIn Button
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent,RC_SIGN_IN);

            }
        });


        // Listener for Facebook signIn Button
        facebookSignIn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG1,"onSuccess"+loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.e(TAG1,"onCancel");

            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.e(TAG1,"onError" + e.getMessage());

            }
        });

        authStateListener =new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    updateUI(user);
                }
                else{
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(@Nullable AccessToken accessToken, @Nullable AccessToken accessToken1) {
                if(accessToken1 == null){
                    firebaseAuth.signOut();
                    googleSignInClient.signOut();
                }
            }
        };

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode,resultCode,data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }

    }

    // firebase Auth WithGoogleAccount
    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d(TAG, "onSuccess: Loggedin");
                currentUser = firebaseAuth.getCurrentUser();

                sUid = currentUser.getUid();
                sEmail = currentUser.getEmail();

                Log.d(TAG, "onSuccess: Email" + sEmail);
                Log.d(TAG, "onSuccess: UID" + sUid);

                if(authResult.getAdditionalUserInfo().isNewUser()){
                    // user is new // account created
                    Log.d(TAG,"Account created");
                    Toast.makeText(getContext(), "Account Created...\n"+sEmail, Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.d(TAG, "onSuccess: Existing user...");
                    Toast.makeText(getContext(), "onSuccess: Existing user..\n"+sEmail, Toast.LENGTH_SHORT).show();
                }

                startActivity(new Intent(getContext(), HomeActivity.class));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Error while logging:",e.getMessage());
            }
        });
    }
    // Updating UI after signIn
    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(getContext(),HomeActivity.class);
        startActivity(intent);
    }

    // Handling Facebook Token
    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity() , new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("Error",task.getException().toString());
                            Toast.makeText(getContext(), ""+task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    // Adding AuthState Listener in onStart function
    @Override
    public void onStart(){
        super.onStart();

        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        if(authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}