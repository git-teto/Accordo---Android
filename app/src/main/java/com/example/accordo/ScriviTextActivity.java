package com.example.accordo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ScriviTextActivity extends AppCompatActivity implements View.OnClickListener {
    private static  String USER_SID ;
    public static RequestQueue mRequestQueue;
    public static String canale;
    public EditText messaggioEditText;
    public TextView errorMessaggio;
    public static String messaggioDaInviare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrivi_text);
        USER_SID=Utils.getPersonalSid(this);
        Log.d("testprova","canale activity il mio user sid preso con utils "+USER_SID);
        messaggioEditText=findViewById(R.id.textdainviare);
        errorMessaggio=findViewById(R.id.alert);
        Button inviatext=findViewById(R.id.inviatextbutton);
        inviatext.setOnClickListener(this);
        Button back=findViewById(R.id.tornaacanaledascrivitext);
        back.setOnClickListener(this);
        Intent myIntent = getIntent();
        if (myIntent.hasExtra("canale")) {
            canale=myIntent.getStringExtra("canale");
            Log.d("ScriviTextActivity","il canale passato e: "+canale);
        }
    }
    public void inviaText(){
        messaggioDaInviare=messaggioEditText.getText().toString();
        mRequestQueue= Volley.newRequestQueue(this);
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/addPost.php";
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", USER_SID);
            jsonBody.put("ctitle",""+canale);
            jsonBody.put("type","t");
            jsonBody.put("content",""+messaggioDaInviare);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("VolleyInvioMessaggio", "Correct: " + response.toString());
                        errorMessaggio.setText("Messaggio inviato correttamente!");
                        errorMessaggio.setTextColor(Color.GREEN);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley", "Error message: " + error.networkResponse.statusCode+"");
                errorMessaggio.setTextColor(Color.RED);
                //if(error.networkResponse.statusCode==413){}
                switch(error.networkResponse.statusCode){
                    case 413:
                        errorMessaggio.setText("Il testo e' troppo lungo!");
                        break;
                }
            }
        });
        Log.d("Volley", "Sending request");
        mRequestQueue.add(request);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.inviatextbutton:
                inviaText();
                break;
            case R.id.tornaacanaledascrivitext:
                Intent myintent = new Intent(this, CanaleActivity.class);
                myintent.putExtra("nomecanalecliccato", canale);
                startActivity(myintent);
                break;
        }
    }
}