package com.example.accordo;

import androidx.appcompat.app.AppCompatActivity;

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

public class CreaCanaleActivity extends AppCompatActivity  implements View.OnClickListener{

    private static  String USER_SID ;
    public static RequestQueue mRequestQueue;
    public static String canale;
    public EditText nomeCanaleEditext;
    public TextView errorMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_canale);
        USER_SID=Utils.getPersonalSid(this);
        Log.d("testprova","main activity il mio user sid preso con utils "+USER_SID);

        ClickManager clickmanager=new ClickManager(this);
        Button bottoneTornaaScrividaCreaCanale=findViewById(R.id.tornaaScrividaCreaCanale);
        bottoneTornaaScrividaCreaCanale.setOnClickListener(clickmanager);
        errorMessage=findViewById(R.id.errormessagecreacanale);
        nomeCanaleEditext=findViewById(R.id.nomeCanale);
        Button inviaNuovoCanale=findViewById(R.id.inviacanale);
        inviaNuovoCanale.setOnClickListener(this);


    }

    public void creaCanale(){
        canale=nomeCanaleEditext.getText().toString();
        mRequestQueue= Volley.newRequestQueue(this);
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/addChannel.php";
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", USER_SID);
            jsonBody.put("ctitle",""+canale);
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
                        errorMessage.setText("canale creato correttamente!");
                        errorMessage.setTextColor(Color.GREEN);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley", "Error message: " + error.networkResponse.statusCode+"");
                errorMessage.setTextColor(Color.RED);
                //if(error.networkResponse.statusCode==413){}
                switch(error.networkResponse.statusCode){
                    case 413:
                        errorMessage.setText("Nome Canale  troppo lungo!");
                        break;
                    case 400:
                        errorMessage.setText("Nome Canale  già esistente!");
                        break;
                }
            }
        });
        Log.d("Volley", "Sending request");
        mRequestQueue.add(request);
    }

    @Override
    public void onClick(View v) {
           creaCanale();
    }
}