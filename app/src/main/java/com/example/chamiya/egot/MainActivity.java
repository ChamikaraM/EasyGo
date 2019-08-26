package com.example.chamiya.egot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

// import com.firebaseloginapp.R;
import com.example.chamiya.egot.admin.AddBusActivity;
import com.example.chamiya.egot.authentication.LoginActivity;
import com.example.chamiya.egot.menuPages.PMenuActivity;
import com.example.chamiya.egot.models.Bus;
import com.example.chamiya.egot.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }

        };
        getBusDetails(FirebaseAuth.getInstance().getCurrentUser().getEmail());


    }
    public void getBusDetails(final String email) {
        final List<User> currentUsers = new ArrayList<User>();
        userType = FirebaseDatabase.getInstance().getReference("User");
        userType.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    User userList = child.getValue(User.class);
                    currentUsers.add(userList);
                    String myString = userList.getEmail();
                    String userType = userList.getUserRole();
                    if (myString.equals(email)) {
                        if (userType.equals("admin")) {
                            Toast.makeText(getApplicationContext(), "This is the admin", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, AddBusActivity.class));
                            finish();
                        } else if(userType.equals("bus")) {
                            Toast.makeText(getApplicationContext(), "This is a bus ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, BMenuActivity.class));
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "This is a passenger ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, PMenuActivity.class));
                            finish();
                        }

                    }


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // this listener will be called when there is change in firebase user session
    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            } else {
                getBusDetails(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            }
        }


    };
}





