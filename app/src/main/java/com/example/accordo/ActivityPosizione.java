package com.example.accordo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityPosizione extends AppCompatActivity implements View.OnClickListener{
    private static  String USER_SID;
    public static RequestQueue mRequestQueue;
    Button inviaPos;
    Button back;
    public static String canale;
    public TextView errorMessaggio;

    //variabili per richiedere la posizione
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=0;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private static boolean requestingLocationUpdates;
    public double latitudine;
    public double longitudine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posizione);
        USER_SID=Utils.getPersonalSid(this);
        Log.d("activityPosizione","pos activity il mio user sid preso con utils "+USER_SID);
        back=findViewById(R.id.tornaacanaledainviaposizione);
        back.setOnClickListener(this);
        inviaPos=findViewById(R.id.inviaposactivityposizione);
        inviaPos.setOnClickListener(this);
        errorMessaggio=findViewById(R.id.errormessageinviapos);
        Intent myIntent = getIntent();
        if (myIntent.hasExtra("canale")) {
            canale=myIntent.getStringExtra("canale");
            Log.d("activityPosizione","il canale passato e: "+canale);
        }

        //ultima pos nota && metodo di callback
        fusedLocationClient= LocationServices.getFusedLocationProviderClient(this);

        //metodo callback richiamato quando una nuova pos è disponibile
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                Location location=locationResult.getLastLocation();
                latitudine=location.getLatitude();
                longitudine=location.getLongitude();
                Log.d("activityPosizione", "latitudine " + latitudine+" Longitudine: "+longitudine);
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //I permessi non sono stati (ancora) concessi
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        else {
            //I permessi sono stati concessi
            startLocationUpdates();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Log.d("activityPosizione", "permessi garantiti ");
                    inviaPos.setEnabled(true);
                    startLocationUpdates();
                } else {
                    Log.d("activityPosizione", "permessi non ancora garantiti");
                    inviaPos.setEnabled(false);
                }
                return;
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000); //in ms.
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tornaacanaledainviaposizione:
                Intent myintent = new Intent(this, CanaleActivity.class);
                myintent.putExtra("nomecanalecliccato", canale);
                startActivity(myintent);
                break;
            case R.id.inviaposactivityposizione:
                Log.d("activityPosizione","ho cliccato invia pos!");
                inviaPosizione(latitudine,longitudine);
                break;
        }
    }
    /* per avere l'ultima pos nota richiesta da qualsiasi applicazione
    public void getLastLocation(View v){
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    Log.d("Location", "Last known location:" + location.toString());
                } else {
                    Log.d("Location", "Last Known location not available");
                }
            }
        });
    }*/
    @Override
    protected void onResume() {
        super.onResume();
        if(requestingLocationUpdates){
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback);
        requestingLocationUpdates=true;
    }

    public void inviaPosizione(double lat,double lon){
        mRequestQueue= Volley.newRequestQueue(this);
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/addPost.php";
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", USER_SID);
            jsonBody.put("ctitle",""+canale);
            jsonBody.put("type","l");
            jsonBody.put("lat",""+lat);
            jsonBody.put("lon",""+lon);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("activityPosizione", "volleyCorrect: " + response.toString());
                        errorMessaggio.setText("server posizione inviata correttamente!");
                        errorMessaggio.setTextColor(Color.GREEN);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("activityPosizione", "volley Error message: " + error.networkResponse.statusCode+"");
              //  errorMessaggio.setTextColor(Color.RED);
                //if(error.networkResponse.statusCode==413){}
                switch(error.networkResponse.statusCode){
                    case 400:
                     //   errorMessaggio.setText(" server posizione non inviata correttamente");
                     //   errorMessaggio.setTextColor(Color.RED);
                        break;
                }
            }
        });
        Log.d("activityPosizione", "volley Sending request");
        mRequestQueue.add(request);
    }
}