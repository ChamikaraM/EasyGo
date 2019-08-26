package com.example.chamiya.egot;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import com.example.chamiya.egot.models.Bus;
import com.example.chamiya.egot.models.Tracking;
import com.example.chamiya.egot.models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class BusScheduleViewActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,LocationListener {

        //Firebase
        DatabaseReference onlineRef, currentUserRef, counterRef, locations, databaseUser, databaseBus, postRef, permission, permission1;
        FirebaseRecyclerAdapter<Bus, ListOnlineViewHolder> adapter;

        // Define a String ArrayList for the buses
        private ArrayList<String> Bus = new ArrayList<>();

        // Define an ArrayAdapter for the list
        private ArrayAdapter<String> arrayAdapter;

        //Firebase.setAndroidContext(this);
        public boolean isABus = false;
        public String uid;

        //View
        RecyclerView listOnline;
        RecyclerView.LayoutManager layoutManager;

        //Location
        private static final int MY_PERMISSION_REQUEST_CODE = 7171;
        private static final int PLAY_SERVICES_RES_REQUEST = 7172;
        private LocationRequest mLocationRequest;
        private GoogleApiClient mGoogleApiClient;
        private Location mLastLocation;

        private static int UPDATE_INTERVAL = 5000;
        private static int FASTEST_INTERVAL = 3000;
        private static int DISTANCE = 10;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_list_online);



            //Init view
            listOnline = (RecyclerView) findViewById(R.id.listOnline);
            listOnline.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            listOnline.setLayoutManager(layoutManager);


            //Set toolbar and logout / Join menu
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
//        toolbar.setTitle("Bus Tracking App");
//        setSupportActionBar(toolbar);

            //Firebase
            permission1 = FirebaseDatabase.getInstance().getReference("Permission");
            permission = FirebaseDatabase.getInstance().getReference("Bus").child("email");
            databaseBus = FirebaseDatabase.getInstance().getReference("Bus");
            databaseUser = FirebaseDatabase.getInstance().getReference("User");
            locations = FirebaseDatabase.getInstance().getReference("Locations");
            onlineRef = FirebaseDatabase.getInstance().getReference().child(".info/connected");
            counterRef = FirebaseDatabase.getInstance().getReference("lastOnline"); // Create new child name last online
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

         //   if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(databaseBus.child("email"))){
              currentUserRef = FirebaseDatabase.getInstance().getReference("lastOnline")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());  //Create new child in lastOnline with key is uid
           // }


            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION

                },MY_PERMISSION_REQUEST_CODE);
            }
            else
            {
                if(checkPlayServices())
                {
                    buildGoogleApiClient();
                    createLocationRequest();
                    displayLocation();
                }
            }
            Toast.makeText(getApplicationContext(), "before getting the bus details", Toast.LENGTH_SHORT).show();
            getBusDetails(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            Toast.makeText(getApplicationContext(), "after getting bus details", Toast.LENGTH_SHORT).show();
            //After setup system , we just load all users from counterRef and display on RecyclerView
            //This is online list


        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch(requestCode)
            {
                case MY_PERMISSION_REQUEST_CODE:
                {
                    if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    {
                        if(checkPlayServices())
                        {
                            buildGoogleApiClient();
                            createLocationRequest();
                            displayLocation();
                        }
                    }
                }
            }
        }

        private void displayLocation() {
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLastLocation != null)
            {
                //Update to Firebase
                locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(new Tracking(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                String.valueOf(mLastLocation.getLatitude()),
                                String.valueOf(mLastLocation.getLongitude())));
            }
            else
            {
                //Toast.makeText(this, "Couldn't get the location", Toast.LENGTH_SHORT).show();
                Log.d("TEST", "Could n't load location");
            }
        }

        private void createLocationRequest() {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
            mLocationRequest.setSmallestDisplacement(DISTANCE);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        private void buildGoogleApiClient() {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
            mGoogleApiClient.connect();
        }

        private boolean checkPlayServices() {
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
            if(resultCode != ConnectionResult.SUCCESS)
            {
                if(GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                {
                    GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICES_RES_REQUEST).show();
                }
                else
                {
                    Toast.makeText(this, "This device is not supported", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return false;
            }
            return true;

        }

        private void updateList() {
            adapter = new FirebaseRecyclerAdapter<Bus, ListOnlineViewHolder>(
                    Bus.class,
                    R.layout.user_layout,
                    ListOnlineViewHolder.class,
                    databaseBus
            ) {
                @Override
                protected void populateViewHolder(ListOnlineViewHolder viewHolder, final Bus model, int position) {

                    viewHolder.txtEmail.setText(model.getEmail());

                    //we need implement item click of recycler view
                    viewHolder.itemClickListener = new com.example.chamiya.egot.itemClickListener(){
                        @Override
                        public void onClick(View view, int position){
                            //If model is current user , not set click event
                            if(!model.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                            {
                                Intent map = new Intent(BusScheduleViewActivity.this, MapTracking.class);
                                map.putExtra("email",model.getEmail());
                                map.putExtra("lat",mLastLocation.getLatitude());
                                map.putExtra("lng",mLastLocation.getLongitude());
                                startActivity(map);
                            }
                        }
                    };
                }
            };
            adapter.notifyDataSetChanged();
            listOnline.setAdapter(adapter);
        }

        private void setupSystem() {

            onlineRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                       // if(isABus){
                            currentUserRef.onDisconnect().removeValue(); //Delete old value
                       //     Toast.makeText(getApplicationContext(), "is a bus true", Toast.LENGTH_SHORT).show();
                            //Set Online user in list
                            counterRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(new User(FirebaseAuth.getInstance().getCurrentUser().getUid(), "", FirebaseAuth.getInstance().getCurrentUser().getEmail(), "", "", "Online"));
                            adapter.notifyDataSetChanged();

                      //  }else {

                            Toast.makeText(getApplicationContext(), "finished setting up", Toast.LENGTH_SHORT).show();
                 //   }



                }
                    @Override
                    public void onCancelled (DatabaseError databaseError){

                    }
                }

                );

                counterRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Toast.makeText(getApplicationContext(), "2n for loop", Toast.LENGTH_SHORT).show();

                            //     User user = null;
                            //  if (postSnapshot.getValue(User.class).getUserRole().equals("bus")) {
                            //      user = postSnapshot.getValue(User.class);
                            // }

                            // Log.d("LOG", "" + user.getEmail() + " is " + user.getStatus());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        }



        public void getBusDetails(final String email) {
            final List<com.example.chamiya.egot.models.Bus> busUsers = new ArrayList<Bus>();
            Toast.makeText(getApplicationContext(), email, Toast.LENGTH_SHORT).show();
            permission = FirebaseDatabase.getInstance().getReference("Bus");
            permission.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for(DataSnapshot child : children){
                        Bus userList = child.getValue(Bus.class);
                        busUsers.add(userList);
                        String myString = userList.getEmail();
                        if (myString.equals(email)) {
                            Toast.makeText(getApplicationContext(), "Permission allow", Toast.LENGTH_SHORT).show();
                            setupSystem();
                            updateList();
                        } else {
                            Toast.makeText(getApplicationContext(), "Permission deny", Toast.LENGTH_SHORT).show();

                        }


                    }




                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }




    @Override
        public boolean onCreateOptionsMenu(Menu menu){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item){
            switch(item.getItemId()){
                case R.id.action_join:
                    counterRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(new User(FirebaseAuth.getInstance().getCurrentUser().getUid(),"",FirebaseAuth.getInstance().getCurrentUser().getEmail(),"","","Online"));
                    break;
                case R.id.action_logout:
                    currentUserRef.removeValue();
                    break;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            displayLocation();
            startLocationUpdates();
        }

        private void startLocationUpdates() {
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
        }

        @Override
        public void onConnectionSuspended(int i) {
            mGoogleApiClient.connect();
        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }

        @Override
        public void onLocationChanged(Location location) {
            mLastLocation = location;
            displayLocation();
        }

        @Override
        protected void onStart(){
            super.onStart();
            if(mGoogleApiClient != null)
                mGoogleApiClient.connect();
        }

        @Override
        protected void onStop(){
            if(mGoogleApiClient != null)
                mGoogleApiClient.disconnect();
            super.onStop();
        }

        @Override
        protected void onResume(){
            super.onResume();
            checkPlayServices();
        }

    }


