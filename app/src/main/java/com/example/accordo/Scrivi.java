package com.example.accordo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Scrivi extends AppCompatActivity implements OnListClickListener {

    private static  String USER_SID ;
    public static RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrivi);
        ClickManager clickmanager=new ClickManager(this);
        Button tornahome=findViewById(R.id.tornaHomedaScrivi);
        tornahome.setOnClickListener(clickmanager);
        Button vaiacreacanale=findViewById(R.id.creacanale);
        vaiacreacanale.setOnClickListener(clickmanager);
       // Button vaiacanale=findViewById(R.id.vaiaCanale);
       // vaiacanale.setOnClickListener(clickmanager);
        //caricaGui();
        USER_SID=Utils.getPersonalSid(this);
        Log.d("testprova","scrivi activity il mio user sid preso con utils "+USER_SID);
        scaricaWall();

    }
    public void OnListClick(int position,String nomecanale){
        Log.d("scriviActivity","richiamo metodo dal click su cella "+position);
        Intent myintent = new Intent(this, CanaleActivity.class);
        myintent.putExtra("nomecanalecliccato", nomecanale);
        startActivity(myintent);
    }

    public void caricaGui(){
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPost);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdapterCanale adaptercanale = new AdapterCanale(this,this);
        recyclerView.setAdapter(adaptercanale);
    }

    public void scaricaWall(){
        mRequestQueue= Volley.newRequestQueue(this);
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/getWall.php";
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
                        Log.d("Volley", "Correct: " + response.toString());
                        //trasformo il json object nell'array usanso utils

                          final ArrayList<Canale> arrcanali=Utils.getArrayCanaliFromJsonObject(response);
                        Model.getInstance().addCanali(arrcanali);
                        caricaGui();

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
}