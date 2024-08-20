package com.example.accordo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

public class ActivityPosizioneSuMappa extends AppCompatActivity {
    private MapView mapView;
    private MapboxMap mMapboxMap;
    private static int millisecondSpeed = 1000;
    public static Post p;
    public static String canale;
    double longitudine;
    double latitudine;
    Style mystyle;
    private SymbolManager symbolManager;
    public static final String SYMBOL_IMAGE = "ic_add_location_black_24dp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_posizione_su_mappa);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mMapboxMap=mapboxMap;
                mMapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mystyle=style;
                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments

                        //questo metodo va qua perchè deve fare lo sposstamento di pos solo quando dal server mi arriva la mappa
                        visualizzaposizione(latitudine,longitudine);
                    }
                });
            }
        });

        Intent myIntent = getIntent();
        if (myIntent.hasExtra("position")) {
            String value=myIntent.getStringExtra("position");
            Log.d("ActivityPosizioneSuMappa","il valore passato e: "+value);
            p=Model.getInstance().getPostByIndex(Integer.parseInt(value));
             latitudine=p.getLatitudine();
             longitudine=p.getLongitudine();
            Log.d("ActivityPosizioneSuMappa","latitudine: "+latitudine+" longitudine: "+longitudine);
            //if(latitudine==null&&longitudine==null){}
        }


        if (myIntent.hasExtra("canale")) {
            canale=myIntent.getStringExtra("canale");
            Log.d("ActivityPosizioneSuMappa","il canale passato e: "+canale);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    public void visualizzaposizione(double latitudine,double longitudine){
        Log.d("ActivityPosizioneSuMappa","metodo vispos latitudine: "+latitudine+" longitudine: "+longitudine);
         CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(latitudine, longitudine))
                .zoom(10)
                .tilt(20)
                .build();
        mMapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), millisecondSpeed);
        mystyle.addImage(SYMBOL_IMAGE, BitmapFactory.decodeResource(ActivityPosizioneSuMappa.this.getResources(),R.drawable.position));
        symbolManager = new SymbolManager(mapView, mMapboxMap, mystyle);
        symbolManager.setIconAllowOverlap(true);
        symbolManager.setTextAllowOverlap(true);
        symbolManager.create(new SymbolOptions()
                .withLatLng(new LatLng(latitudine, longitudine))
                .withIconImage(SYMBOL_IMAGE)
                .withIconSize(0.12f));
    }
}