package com.example.accordo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class CambiaImmagineActivity extends AppCompatActivity implements View.OnClickListener{
    private static String USER_SID ;
    public static RequestQueue mRequestQueue;
    ImageView immagineProfilo;
    public static final int IMGPRV =1;
    private TextView errorMessage;
    private static String imageString=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambia_immagine);
        ClickManager clickmanager=new ClickManager(this);
        Button profilo=findViewById(R.id.tornaaProfilodaCambiaImmagine);
        profilo.setOnClickListener(clickmanager);

        USER_SID=Utils.getPersonalSid(this);
        Log.d("testprova","main activity il mio user sid preso con utils "+USER_SID);

        immagineProfilo=findViewById(R.id.immagineprofilocambia);
        immagineProfilo.setImageResource(R.mipmap.iconuser);
        immagineProfilo.setOnClickListener(this);

        Button inviaNuovaImmagineProfilo=findViewById(R.id.invianuovaimmagineprofilo);
        inviaNuovaImmagineProfilo.setOnClickListener(this);

        errorMessage=findViewById(R.id.erroremessaggioinviaimmagine);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.immagineprofilocambia:
               //codice che fa partire un intent sulla galleria
                errorMessage.setText("");
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), IMGPRV);
                break;
            case R.id.invianuovaimmagineprofilo:
               // inviaNuovaImmagine();
               if(imageString==null){
                   Log.d("cambiaImmagineactivity", "ho cliccato invia ma la foto è null "+imageString);
                   errorMessage.setText("client Foto ha dimensioni eccessive!");
                   errorMessage.setTextColor(Color.RED);
               }
               else{
                   inviaNuovaImmagine(imageString);
                   Log.d("cambiaImmagineactivity", "ho cliccato invia e la foto non e' null ");
               }
               /* inviaNuovaImmagine(imageString);
                Log.d("cambiaimmagineactivity", "ho cliccato invia");*/
                break;
        }

    }
    //codice che si attiva quando arriva la risposta dalla galleria
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMGPRV)
            if (resultCode == Activity.RESULT_OK) {
                Uri picturez = data.getData();
                // Operiamo sulla picture
                immagineProfilo.setImageURI(picturez);

                /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picturez);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte[] imageBytes = baos.toByteArray();
                 imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);*/
               imageString=Utils.codificaUriInStringBase64(picturez,this);
                if(imageString==null){
                    Log.d("cambiaImmagineactivity", "foto troppo grande dopo selezione onActivityResult  foto! "+imageString);
                }
                else{
                    //imageString=Utils.codificaUriInStringBase64(picturez,this);
                    Log.d("cambiaImmagineactivity", "foto giusta dopo selezione foto, onActivityResult  imagestring: "+imageString);
                }
                /* imageString=Utils.codificaUriInStringBase64(picturez,this);
                Log.d("cambiaImmagineactivity", "imagestring: "+imageString);*/

            }
    }

    private void inviaNuovaImmagine(String immagine) {
        mRequestQueue= Volley.newRequestQueue(this);
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/setProfile.php";
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid",USER_SID );
           // jsonBody.put("picture",""+nuovoNome);
            jsonBody.put("picture",""+immagine);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("VolleyCambiaImmagine", "Correct: " + response.toString());
                        errorMessage.setText("Foto cambiata correttamente!");
                        errorMessage.setTextColor(Color.GREEN);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyCambiaImmagine", "Error message: " + error.networkResponse.statusCode+"");
                errorMessage.setText("server Foto non valida!");
                errorMessage.setTextColor(Color.RED);

            }
        });
        Log.d("VolleyCambiaImmagine", "Sending request");
        mRequestQueue.add(request);
    }


}

