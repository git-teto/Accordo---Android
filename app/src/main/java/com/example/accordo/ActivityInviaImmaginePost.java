package com.example.accordo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
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

public class ActivityInviaImmaginePost extends AppCompatActivity implements View.OnClickListener {
    private static  String USER_SID;
    public static RequestQueue mRequestQueue;
    public static final int IMGPRV =1;
    public static String fotoBase64;
    ImageView fotodaInviare;
    Button inviaFoto;
    Button back;
    public static String canale;
    public TextView errorMessaggio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invia_immagine_post);
        USER_SID=Utils.getPersonalSid(this);
        Log.d("testprova","canale activity il mio user sid preso con utils "+USER_SID);

        fotodaInviare=findViewById(R.id.immaginesceltadagalleria);
        inviaFoto=findViewById(R.id.bottoneinviaimmaginedefinitivo);
        back=findViewById(R.id.tornaacanaledainviaimmagine);
        back.setOnClickListener(this);
        fotodaInviare.setOnClickListener(this);
        inviaFoto.setOnClickListener(this);
        errorMessaggio=findViewById(R.id.errormessageinviaimmaginepost);
        Intent myIntent = getIntent();
        if (myIntent.hasExtra("imageStringBase64")) {
            fotoBase64=myIntent.getStringExtra("imageStringBase64");
            Log.d("ActivityFotoIntera","get intent imageStringBase64 e: "+fotoBase64);
           if(fotoBase64.equals("null")){
               Log.d("ActivityFotoIntera","get intent imageStringBase64 e null: "+fotoBase64);
               errorMessaggio.setText("Client La foto ha dimensioni eccessive!");
               errorMessaggio.setTextColor(Color.RED);
               fotodaInviare.setImageResource(R.mipmap.iconuser);
           }
           else{
               Log.d("ActivityFotoIntera","get intent imageStringBase64 non e null: "+fotoBase64);
               Bitmap fotoBitmap=Utils.decodoficaBase64inImmagine(fotoBase64);
               fotodaInviare.setImageBitmap(fotoBitmap);
               errorMessaggio.setText(" client foto ha dimensioni correttamente!");
               errorMessaggio.setTextColor(Color.GREEN);
           }
           /* Bitmap fotoBitmap=Utils.decodoficaBase64inImmagine(fotoBase64);
            fotodaInviare.setImageBitmap(fotoBitmap);*/
        }
        if (myIntent.hasExtra("canale")) {
            canale=myIntent.getStringExtra("canale");
            Log.d("ActivityFotoIntera","il canale passato e: "+canale);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tornaacanaledainviaimmagine:
                Intent myintent = new Intent(this, CanaleActivity.class);
                myintent.putExtra("nomecanalecliccato", canale);
                startActivity(myintent);
                break;
            case R.id.bottoneinviaimmaginedefinitivo:
                Log.d("ActivityFotoIntera", "onclick inizio foto: "+fotoBase64);
                if(fotoBase64==null||fotoBase64.equals("null")){
                    Log.d("ActivityFotoIntera", "onclick foto e' nulla non si invia nulla! "+" lunghezza string: "+fotoBase64.length());
                    errorMessaggio.setText("Client La foto ha dimensioni eccessive!");
                    errorMessaggio.setTextColor(Color.RED);
                }else{
                    errorMessaggio.setText("client foto ha dimensioni giuste");
                    errorMessaggio.setTextColor(Color.GREEN);
                    Log.d("ActivityFotoIntera", "onclick foto non  e' nulla, si invia!n"+fotoBase64+" lunghezza string: "+fotoBase64.length());
                    inviaImmagine(fotoBase64);
                }
                //inviaImmagine(fotoBase64);
                break;
            case R.id.immaginesceltadagalleria:
                Log.d("ActivityFotoIntera", "onclick clicco su foto");
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), IMGPRV);
                break;
        }
    }

    public void inviaImmagine(String immagine){
        mRequestQueue= Volley.newRequestQueue(this);
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/addPost.php";
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", USER_SID);
            jsonBody.put("ctitle",""+canale);
            jsonBody.put("type","i");
            jsonBody.put("content",""+immagine);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ActivityFotoIntera", "volleyCorrect: " + response.toString());
                        errorMessaggio.setText("server foto inviata correttamente!");
                        errorMessaggio.setTextColor(Color.GREEN);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ActivityFotoIntera", "volley Error message: " + error.networkResponse.statusCode+"");
                errorMessaggio.setTextColor(Color.RED);
                //if(error.networkResponse.statusCode==413){}
                switch(error.networkResponse.statusCode){
                    case 413:
                        errorMessaggio.setText(" server La foto ha dimensioni eccessive!");
                        errorMessaggio.setTextColor(Color.RED);
                        break;
                }
            }
        });
        Log.d("ActivityFotoIntera", "volley Sending request");
        mRequestQueue.add(request);
    }
    //codice che si attiva quando arriva la risposta dalla galleria
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMGPRV)
            if (resultCode == Activity.RESULT_OK) {
                Uri picturez = data.getData();
                // Operiamo sulla picture

                fotoBase64=Utils.codificaUriInStringBase64(picturez,this);
                if(fotoBase64==null){
                    fotodaInviare.setImageResource(R.mipmap.iconuser);
                    Log.d("ActivityFotoIntera", "on activityresul foto troppo grande dopo selezione onActivityResult  foto! "+fotoBase64+" lunghezza string: ");
                    fotoBase64="null";
                }
                else{
                    fotodaInviare.setImageURI(picturez);
                    //imageString=Utils.codificaUriInStringBase64(picturez,this);
                    Log.d("ActivityFotoIntera", "on activity result foto giusta dopo selezione foto, onActivityResult  imagestring: "+fotoBase64+" lunghezza string: ");
                }
            }
    }
}