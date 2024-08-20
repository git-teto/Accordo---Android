package com.example.accordo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
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

public class CambiaNomeActivity extends AppCompatActivity implements View.OnClickListener{
    private static  String USER_SID;
    public static RequestQueue mRequestQueue;
    private EditText nuovonome;
    private TextView errorMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambia_nome);

        USER_SID=Utils.getPersonalSid(this);
        Log.d("testprova","main activity il mio user sid preso con utils "+USER_SID);

        ClickManager clickmanager=new ClickManager(this);
        Button profilo=findViewById(R.id.tornaaProfilodaCambiaNome);
        profilo.setOnClickListener(clickmanager);
        Button invia=findViewById(R.id.invianuovonome);
        nuovonome=findViewById(R.id.nuovonome);
        errorMessage=findViewById(R.id.errormessage);
        invia.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
       String nuovoNomedaTextView=nuovonome.getText().toString();
        Log.d("cambianomeactitvity","ho cliccato! nuovo nome: "+nuovoNomedaTextView);
        inviaNuovoNome(nuovoNomedaTextView);
    }

    public void inviaNuovoNome(String nuovoNome){
        mRequestQueue= Volley.newRequestQueue(this);
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/setProfile.php";
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", USER_SID);
            jsonBody.put("name",""+nuovoNome);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("VolleyCambioNome", "Correct: " + response.toString());
                        errorMessage.setText("Nome cambiato correttamente!");
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
                        errorMessage.setText("Nome troppo lungo!");
                        break;
                    case 400:
                        errorMessage.setText("Nome già esistente");
                        break;
                }
            }
        });
        Log.d("Volley", "Sending request");
        mRequestQueue.add(request);
    }
}