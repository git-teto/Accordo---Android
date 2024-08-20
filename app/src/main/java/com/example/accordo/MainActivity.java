package com.example.accordo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
   private static final String TAG=MainActivity.class.getName();
    public static String USER_SID;
    public static String PREFS_NAME="miePreferenze";
    public static RequestQueue mRequestQueue;
    Button profilo;
    Button scrivi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //prende il layout dalla cartella layout
        ClickManager clickmanager=new ClickManager(this);
        profilo=findViewById(R.id.profilo);
        profilo.setOnClickListener(clickmanager);
        scrivi=findViewById(R.id.scrivi);
        scrivi.setOnClickListener(clickmanager);
        /*USER_SID=Utils.getPersonalSid(this);
        Log.d("testprova","main activity il mio user sid preso con utils "+USER_SID);*/
        if(Model.getInstance()==null){
            Log.d(TAG,"non va il model");
        }
        else{
            //Model.getInstance().addFakedata();
            Log.d(TAG," numero di canali: "+Model.getInstance().getSize());
        }
        if(controlloprimoaccesso()){
            Log.d("testprova","primo accesso");
            profilo.setEnabled(false);
            scrivi.setEnabled(false);
           // impostaPrimoAccesso();
            register();
        }
        else{
            Log.d("testprova","non primo accesso");
            String uid=Utils.getPersonalSid(this);
            Log.d("testprova","personal uid "+uid);
        }

    }
    public boolean controlloprimoaccesso(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean primoaccesso = settings.getBoolean("mioprimoaccesso", true);
        return primoaccesso;
    }
    public void impostaPrimoAccesso(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("mioprimoaccesso", false);
        editor.commit();
    }

    public void salvaPersonalSid(String uid){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        // settings.getString("personal_uid", "");
        SharedPreferences.Editor editor = settings.edit();
        Log.d("test salva uid",""+uid);
        editor.putString("personal_sid", uid);
        editor.commit();
    }

    public String getPersonalSid(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String personalSid = settings.getString("personal_sid", "valore default");
        return personalSid;
    }

    public void register(){
        Log.d("testprova","register ");
        mRequestQueue= Volley.newRequestQueue(this);
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/register.php";
        final JSONObject jsonBody = new JSONObject();

        JsonObjectRequest request = new JsonObjectRequest(
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("testprova","personal sid scaricato "+response.getString("sid"));
                            String sid=response.getString("sid");
                            impostaPrimoAccesso();
                            salvaPersonalSid(sid);
                            profilo.setEnabled(true);
                            scrivi.setEnabled(true);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley", "Error: " + error.toString());
            }
        });
        Log.d("Volley", "Sending request");
        mRequestQueue.add(request);
    }


    //esercizio 1 sposta dati tra activity
    /*public void onButtonClick(View v) {
        Log.d("Sono nella Main Activity", "myTap");
        Intent intent = new Intent(getApplicationContext(), ActivityProfilo.class);
        //Intent intent = new Intent(Intent.ACTION_DIAL);
        //intent.setData(Uri.parse("tel:123" ));
        intent.putExtra("valueToShow", "pippo");
        startActivity(intent);
    }*/
}