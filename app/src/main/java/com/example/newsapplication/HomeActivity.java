package com.example.newsapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    // Variables
    Button signOut;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    EditText filter;
    ArrayList<Model.Data> newsdata ;
    NewsAdapter adapter;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    // Google SignIn
    private GoogleSignInClient googleSignInClient;

    //Api key
    String API_KEY = "b3bdfa00c8b84bc4b7fd62259067830c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Binding Views
        recyclerView = findViewById(R.id.recyclerview);
        filter = findViewById(R.id.filter);
        signOut = findViewById(R.id.sign_out);

        //Initializing views
        newsdata = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        // Google sign In
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        // Setting up Adapter
        adapter= new NewsAdapter(HomeActivity.this,newsdata);
        layoutManager= new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Calling getNews() Function
        getNews();

        filter.setFocusableInTouchMode(true);
        filter.requestFocus();

        // Filter Button for filtering cards
        filter.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    // Perform action on key press

                    if(filter.getText().toString() != null){
                        String keyWord = filter.getText().toString().toLowerCase();
                        ArrayList<Model.Data> temp = new ArrayList<>();
                        for(Model.Data item : newsdata){
                            if(item.description != null){
                                String description = item.description.toLowerCase();
                                if (description.contains(keyWord)) {
                                    continue;
                                } else {
                                    temp.add(item);
                                }
                            }
                        }
                        newsdata.removeAll(temp);
                        adapter.notifyDataSetChanged();
                    }
                    return true;
                }
                return false;
            }
        });


        // Listener for SignIn
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                // Signing Out from firebase
                firebaseAuth.signOut();
                googleSignInClient.signOut();

                // Heading towards HomeActivity
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
                finishAffinity();

            }
        });



    }

    // getNews() Function
    void getNews(){

        ApiUtilites.getApiInterface().getNews("in",100,API_KEY).enqueue(new Callback<MainNews>() {
            @Override
            public void onResponse(Call<MainNews> call, Response<MainNews> response) {
                if(response.isSuccessful()){
                    newsdata.addAll(response.body().getArticles());
                    Log.e("Response", response.body().getArticles().toString());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MainNews> call, Throwable t) {

            }
        });

    }

}