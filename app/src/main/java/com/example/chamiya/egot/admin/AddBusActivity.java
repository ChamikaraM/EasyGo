package com.example.chamiya.egot.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chamiya.egot.models.Bus;
import com.example.chamiya.egot.MainActivity;
import com.example.chamiya.egot.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddBusActivity extends AppCompatActivity {

    private EditText inputRegisteredNo, inputFrom, inputTo, inputRouteNo, inputEmail;
    private DatabaseReference databaseUser;
    private Button add_bus,go_back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);

        databaseUser = FirebaseDatabase.getInstance().getReference("Bus");

        add_bus = (Button) findViewById(R.id.add_bus_button);
        go_back = (Button) findViewById(R.id.back_button);
        inputRegisteredNo = (EditText) findViewById(R.id.registeredNo);
        inputFrom = (EditText) findViewById(R.id.dst1);
        inputTo = (EditText) findViewById(R.id.dst2);
        inputRouteNo = (EditText) findViewById(R.id.routeNo);
        inputEmail = (EditText) findViewById(R.id.email);



        add_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registeredNo = inputRegisteredNo.getText().toString().trim();
                String from = inputFrom.getText().toString().trim();
                String to = inputTo.getText().toString().trim();
                String routeNo = inputRouteNo.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(registeredNo)||TextUtils.isEmpty(from)||TextUtils.isEmpty(to)||TextUtils.isEmpty(routeNo)) {
                    Toast.makeText(getApplicationContext(), "All Fields are required!", Toast.LENGTH_SHORT).show();
                    return;
                }

                addBus();
                startActivity(new Intent(AddBusActivity.this, MainActivity.class));


            }
        });

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddBusActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    public void addBus(){
        String registeredNo = inputRegisteredNo.getText().toString().trim();
        String from = inputFrom.getText().toString().trim();
        String to = inputTo.getText().toString().trim();
        String routeNo = inputRouteNo.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();


        if(!TextUtils.isEmpty(email)){
            String bus_id = databaseUser.push().getKey();
            Bus bus = new Bus(bus_id,registeredNo,from,to,routeNo,email);
            databaseUser.child(bus_id).setValue(bus);
            Toast.makeText(this, "Bus added", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(this, "You should enter all field", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
