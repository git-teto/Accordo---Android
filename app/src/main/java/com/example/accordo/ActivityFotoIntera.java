package com.example.accordo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityFotoIntera extends AppCompatActivity implements View.OnClickListener{
    private static  String USER_SID;
    public static RequestQueue mRequestQueue;
    public static Post p;
    public static String canale;
    public static TextView testoView;
    public static ImageView fotoInviataview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_intera);
        testoView=findViewById(R.id.UtenteInviatoFoto);
        fotoInviataview=findViewById(R.id.FotoInviata);
        Button tornaaCanale=findViewById(R.id.tornaaProfilodaCambiaImmagine);
        USER_SID=Utils.getPersonalSid(this);
        Log.d("testprova","main activity il mio user sid preso con utils "+USER_SID);
        //tornaaCanale.setOnClickListener(this); questo set on click listener non funziona e ho collegato il bottone al onclick da xml
        Intent myIntent = getIntent();

        if (myIntent.hasExtra("position")) {
            String value=myIntent.getStringExtra("position");
            Log.d("ActivityFotoIntera","il valore passato e: "+value);
            p=Model.getInstance().getPostByIndex(Integer.parseInt(value));
            String fotoInviata=p.getContent();

            Log.d("ActivityFotoIntera",fotoInviata+"");
            if(p.getName().equals("null")){
                testoView.setText("Foto di Anonimo ( "+p.getUid()+" )");
            }
            else{
                testoView.setText("Foto di: "+p.getName());
            }
            //testoView.setText("Foto di:" );
        }


        if (myIntent.hasExtra("canale")) {
             canale=myIntent.getStringExtra("canale");
            Log.d("ActivityFotoIntera","il canale passato e: "+canale);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        scaricaFotoPost();
    }
    public void scaricaFotoPost(){
        Log.d("VolleyActivityFotoIntera",p.getPid()+"");
        mRequestQueue= Volley.newRequestQueue(this);
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/getPostImage.php";
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", USER_SID);
            jsonBody.put("pid", p.getPid()+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("VolleyActivityFotoIntera", "Correct: " + response.toString());
                        //trasformo il json object nell'array usanso utils
                        // Model.getInstance().addPosts(arrposts);
                        try {
                            String postImageContent=response.getString("content");
                            Bitmap immagineBitmap=Utils.decodoficaBase64inImmagine(postImageContent);
                            //immagineBitmap.
                            fotoInviataview.setImageBitmap(immagineBitmap);
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

    @Override
    public void onClick(View v) {
        Log.d("onClick", "clicco back");
         Intent intent5 = new Intent(getApplicationContext(), CanaleActivity.class);
         intent5.putExtra("nomecanalecliccato", canale+"");
         startActivity(intent5);
    }
}