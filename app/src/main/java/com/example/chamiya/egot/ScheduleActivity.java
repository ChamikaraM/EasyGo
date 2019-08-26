package com.example.chamiya.egot;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.chamiya.egot.models.BusSchedule;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class ScheduleActivity extends Activity {

    DatabaseReference BusSchedule;

    ListView listViewBusSchedule;
    List<com.example.chamiya.egot.models.BusSchedule> busScheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shedule_view);

        listViewBusSchedule = (ListView) findViewById(R.id.listViewBusSchedule);

        busScheduleList = new ArrayList<>();

        //onStart();

    }

    @Override
    protected void onStart(){
        super.onStart();

            BusSchedule.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                busScheduleList.clear();

                for(DataSnapshot busScheduleSnapshot : dataSnapshot.getChildren()){
                    BusSchedule busSchedule = busScheduleSnapshot.getValue(BusSchedule.class);

                    busScheduleList.add(busSchedule);
                }

                BusScheduleList adapter = new BusScheduleList(ScheduleActivity.this, busScheduleList);
                listViewBusSchedule.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


