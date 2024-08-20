package com.example.accordo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityProfilo extends AppCompatActivity {
    private static  String USER_SID;
    public static RequestQueue mRequestQueue;
    private TextView nomeutente;
    private ImageView fotoutente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);
        USER_SID=Utils.getPersonalSid(this);
        Log.d("testprova","main activity il mio user sid preso con utils "+USER_SID);
        Button tornahome=findViewById(R.id.homedaprofilo);
        Button cambianome=findViewById(R.id.cambianomeutente);
        Button cambiafoto=findViewById(R.id.cambiafotoutente);
        ClickManager clickmanager=new ClickManager(this);
        nomeutente=findViewById(R.id.nomeprofiloutenteprincipale);
        fotoutente=findViewById(R.id.fotoprofiloutenteprincipale);

        tornahome.setOnClickListener(clickmanager);
        cambianome.setOnClickListener(clickmanager);
        cambiafoto.setOnClickListener(clickmanager);
        getProfile();

        //esercizio 1 lez 1 codice che prendi dati da altra activity
        /*Intent myIntent = getIntent();
        if (myIntent.hasExtra("valueToShow")) {
            String value=myIntent.getStringExtra("valueToShow");
            Log.d("ActivityProfiloLog","ciao");
            Log.d("ActivityProfiloLog",value);
        }*/
    }

    public void getProfile(){
        mRequestQueue= Volley.newRequestQueue(this);
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/getProfile.php";
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", USER_SID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volleygetprofilo", "Correct: " + response.toString());
                        try {
                            nomeutente.setText(response.getString("name"));
                            if(response.getString("picture").equals("null")){
                                Log.d("Volleygetprofilo", "foto null");
                                fotoutente.setImageResource(R.mipmap.iconuser);
                            }
                            else{
                                Bitmap foto=Utils.decodoficaBase64inImmagine(response.getString("picture"));
                                fotoutente.setImageBitmap(foto);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley", "Error message: ");

            }
        });
        Log.d("Volley", "Sending request");
        mRequestQueue.add(request);
    }
}