package com.example.chamiya.egot.menuPages;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.chamiya.egot.BusScheduleViewActivity;
import com.example.chamiya.egot.R;
import com.example.chamiya.egot.SelectRouteActivity;
import com.example.chamiya.egot.authentication.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PMenuActivity extends AppCompatActivity {
    private Button btnBus,btnTrain,goBack,signOut;
    private TextView email;

   // private ProgressBar progressBar;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmenu);
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        email = (TextView) findViewById(R.id.myEmail);

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setDataToView(user);

        btnBus = (Button) findViewById(R.id.btnB);
        btnTrain = (Button) findViewById(R.id.btnT);
        goBack = (Button) findViewById(R.id.btnBack);
        signOut = (Button) findViewById(R.id.btnSignout);

        btnBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // progressBar.setVisibility(View.GONE);
                startActivity(new Intent(PMenuActivity.this, SelectRouteActivity.class));
                finish();


            }
        });

        btnTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PMenuActivity.this, BusScheduleViewActivity.class));
                finish();


            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PMenuActivity.this, LoginActivity.class));
                finish();


            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(PMenuActivity.this, LoginActivity.class));
                finish();


            }
        });
    }


    private void setDataToView(FirebaseUser user) {
        email.setText("User Email: " + user.getEmail());
    }


    // this listener will be called when there is change in firebase user session
    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(PMenuActivity.this, LoginActivity.class));
                finish();
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}

