package com.example.newsapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.BoringLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;


public class SignUp extends Fragment {
    EditText Name , Email , Mobile , Password, UId;
    String sName,sEmail,sMobile,sPassword,sUId;
    Button registerButton;

    CheckBox checkbox ;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        // Binding Views
        Name = (EditText) view.findViewById(R.id.editTextName);
        Email = (EditText) view.findViewById(R.id.editTextEmailAddress);
        Mobile = (EditText) view.findViewById(R.id.editTextNumber);
        Password = (EditText) view.findViewById(R.id.editTextPassword);
        checkbox = (CheckBox) view.findViewById(R.id.checkBox);
        registerButton = (Button) view.findViewById(R.id.register);

        // Initializing It
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        // Click Listener for Register Button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sName = Name.getText().toString();
                sEmail = Email.getText().toString();
                sMobile = Mobile.getText().toString();
                sPassword = Password.getText().toString();
                boolean checkedTerms = checkbox.isChecked();

                // Checking If any field is empty
                if (sName.isEmpty()) {
                    Name.setError("Name is required");
                    return;
                }
                if (sEmail.isEmpty()) {
                    Email.setError("Email Address is required");
                    return;
                }
                if (sMobile.isEmpty()) {
                    Mobile.setError("Mobile Number is required");
                    return;
                }
                if (sPassword.isEmpty()) {
                    Password.setError("Not a valid Password");
                    return;
                }
                if(checkedTerms == false){
                    checkbox.setError("Please accept Terms and Conditions");
                    return;
                }

            /*     if want to know user already exist uncomment
                if (currentUser != null) {
                    Intent sendIntent = new Intent(getContext(), HomeActivity.class);
                    Toast.makeText(getContext().getApplicationContext(), "Existing User ",Toast.LENGTH_SHORT).show();
                    startActivity(sendIntent);
                } else{

             */

                // Creating Account with email and password
                    firebaseAuth.createUserWithEmailAndPassword(sEmail,sPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(getContext().getApplicationContext(), "Created",Toast.LENGTH_SHORT).show();
                            Intent sendIntent = new Intent(getContext(), HomeActivity.class);
                            startActivity(sendIntent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("Error = ",e.getMessage());
                        }
                    });

//                } end of else
            }
        });

        return view;
    }


}