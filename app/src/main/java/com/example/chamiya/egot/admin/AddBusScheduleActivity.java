package com.example.chamiya.egot.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chamiya.egot.MainActivity;
import com.example.chamiya.egot.R;
import com.example.chamiya.egot.models.BusSchedule;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddBusScheduleActivity extends AppCompatActivity {

    private EditText inputFrom, inputTo, inputRouteNo;
    private DatabaseReference databaseBusSchedule;
    private Button add_bus,go_back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus_schedule);

        databaseBusSchedule = FirebaseDatabase.getInstance().getReference("BusSchedule");

        add_bus = (Button) findViewById(R.id.add_bus_button);
        go_back = (Button) findViewById(R.id.back_button);
        inputFrom = (EditText) findViewById(R.id.dst1);
        inputTo = (EditText) findViewById(R.id.dst2);
        inputRouteNo = (EditText) findViewById(R.id.routeNo);




        add_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String from = inputFrom.getText().toString().trim();
                String to = inputTo.getText().toString().trim();
                String routeNo = inputRouteNo.getText().toString().trim();



                if (TextUtils.isEmpty(from)||TextUtils.isEmpty(to)||TextUtils.isEmpty(routeNo)) {
                    Toast.makeText(getApplicationContext(), "All Fields are required!", Toast.LENGTH_SHORT).show();
                    return;
                }

                addBusSchedule();
                startActivity(new Intent(AddBusScheduleActivity.this, MainActivity.class));


            }
        });

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddBusScheduleActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    public void addBusSchedule(){

        String from = inputFrom.getText().toString().trim();
        String to = inputTo.getText().toString().trim();
        String routeNo = inputRouteNo.getText().toString().trim();



        if(!TextUtils.isEmpty(routeNo)){
            String bus_schedule_id = databaseBusSchedule.push().getKey();
            BusSchedule busSchedule = new BusSchedule(bus_schedule_id,routeNo,from,to);
            databaseBusSchedule.child(bus_schedule_id).setValue(busSchedule);
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
