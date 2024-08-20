package com.example.accordo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CanaleActivity extends AppCompatActivity implements OnListClickListenerPost, View.OnClickListener {

    private static  String USER_SID ;
    public static RequestQueue mRequestQueue;
    public static String nomecanaleString;
    public static final int IMGPRV =1; //serve nel codice che avvia l'intent per prendere la foto dalla galleria
    private static String imageString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canale);
        USER_SID=Utils.getPersonalSid(this);
        Log.d("testprova","canale activity il mio user sid preso con utils "+USER_SID);

        ClickManager clickmanager=new ClickManager(this);
        Button bottoneTornaaScrividaCanale=findViewById(R.id.tornaaScrividaCanale);
        bottoneTornaaScrividaCanale.setOnClickListener(clickmanager);

        Button inviapost=findViewById(R.id.preparamessaggio);
        inviapost.setOnClickListener(this);
        Button inviaimmagine=findViewById(R.id.preparaimmagine);
        inviaimmagine.setOnClickListener(this);
        Button inviaposizione=findViewById(R.id.inviaposizione);
        inviaposizione.setOnClickListener(this);

        Intent myIntent = getIntent();
        TextView nomecanale=findViewById(R.id.nomecanaleactivitycanale);
        if (myIntent.hasExtra("nomecanalecliccato")) {
            nomecanaleString=myIntent.getStringExtra("nomecanalecliccato");
            Log.d("CanaleActivityLog","nome canale: "+nomecanaleString);
            nomecanale.setText(nomecanaleString);
        }
        //Model.getInstance().addFakedata();
        //Log.d("CanaleActivityLog",""+Model.getInstance().getSizePost());


    }

    @Override
    protected void onStart() {
        super.onStart();
        downloadPostChannel();
       // startRecyclerView();
    }
    public void downloadPostChannel(){
        mRequestQueue= Volley.newRequestQueue(this);
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/getChannel.php";
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", USER_SID );
            //jsonBody.put("ctitle", "AltreProve");
            jsonBody.put("ctitle", ""+nomecanaleString);
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
                        final ArrayList<Post> arrposts=Utils.getArrayPostFromJsonObject(response);
                        Model.getInstance().addPosts(arrposts);
                        startRichiesteFotoUser();


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
   //position si riferisce all indice nell'elenco dei post, quindi al post che ho cliccato
    public void OnListClickPost(int position){
         Log.d("canaleActivity","richiamo metodo dal click su cella "+position);
       switch(Model.getInstance().getPost().get(position).getType()){
            case "t":
                Log.d("canaleActivity\"","clicco testo");
                break;
            case "i":
                Log.d("canaleActivity\"","clicco immagine  "+position);
                 Intent intentFotoinNuovaActivity = new Intent(this, ActivityFotoIntera.class);
                intentFotoinNuovaActivity.putExtra("position", ""+position);
                intentFotoinNuovaActivity.putExtra("canale", ""+nomecanaleString);
                startActivity(intentFotoinNuovaActivity);
                break;
            case "l":
                Log.d("canaleActivity\"","clicco posizione");
                Intent intentPosizioneSuMappa = new Intent(this, ActivityPosizioneSuMappa.class);
                intentPosizioneSuMappa.putExtra("position", ""+position);
                intentPosizioneSuMappa.putExtra("canale", ""+nomecanaleString);
                startActivity(intentPosizioneSuMappa);
                break;
        }

    }

    public void startRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPost);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdapterPost adapterpost = new AdapterPost(this,this);
        recyclerView.setAdapter(adapterpost);
    }
    public void startRichiesteFotoUser(){
        //richiedo foto profilo
        Log.d("VolleyFotoProfilo", "evoco il metodo");
        final String urlUserPicture = "https://ewserver.di.unimi.it/mobicomp/accordo/getUserPicture.php";
        final JSONObject jsonBodyUserPicture = new JSONObject();
        for(UserPicture userpicture: Model.getInstance().getElencoUserPictures()){
            Log.d("VolleyFotoProfilo", "entro nel for");
            try {
                jsonBodyUserPicture.put("sid", USER_SID );
                jsonBodyUserPicture.put("uid", userpicture.getUid()+"");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest requestUserPicture = new JsonObjectRequest(
                    urlUserPicture,
                    jsonBodyUserPicture,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Volley", "Correct: " + response.toString());
                            //trasformo il json object nell'oggetto UserPicture
                            UserPicture userPictureScaricata=Model.getInstance().deserializzaUserPicture(response);
                            Model.getInstance().aggiornaUserPicture(userPictureScaricata);
                            Log.d("Volley","userPicture: "+userPictureScaricata.toString());
                             /* if(userPictureScaricata.getPicture().equals("null")){
                                Log.d("Volley","picture is nulla");
                            }
                            else{
                                Log.d("Volley","picture non is nulla");
                            }*/
                            startRecyclerView();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Volley", "Error: " + error.toString());
                }
            });
            Log.d("Volley", "Sending request2 ");
            mRequestQueue.add(requestUserPicture);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.preparamessaggio:
                Log.d("OnClick","text");
                Intent intentInviaText = new Intent(this, ScriviTextActivity.class);
                intentInviaText.putExtra("canale", ""+nomecanaleString);
                startActivity(intentInviaText);
                break;
            case R.id.preparaimmagine:
                Log.d("OnClick","immagine");
                //codice che apre galleria
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), IMGPRV);
                break;
            case R.id.inviaposizione:
                Log.d("OnClick","posizione");
                Intent intentInviaPos = new Intent(this, ActivityPosizione.class);
                intentInviaPos.putExtra("canale", ""+nomecanaleString);
                startActivity(intentInviaPos);
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
                imageString=Utils.codificaUriInStringBase64(picturez,this);
                if(imageString==null){
                   Log.d("CanaleActivityInviaImmagine", "foto troppo grande!");
                   //imageString=null;
               }else{
                  // imageString=Utils.codificaUriInStringBase64(picturez,this);
                   Log.d("CanaleActivityInviaImmagine", "imagestring: "+imageString);
                  /* Intent intentInviaImage = new Intent(this, ActivityInviaImmaginePost.class);
                   intentInviaImage.putExtra("imageStringBase64", ""+imageString);
                   intentInviaImage.putExtra("canale", ""+nomecanaleString);
                   startActivity(intentInviaImage);*/
               }
                Intent intentInviaImage = new Intent(this, ActivityInviaImmaginePost.class);
                intentInviaImage.putExtra("imageStringBase64", ""+imageString);
                intentInviaImage.putExtra("canale", ""+nomecanaleString);
                startActivity(intentInviaImage);

               /* imageString=Utils.codificaUriInStringBase64(picturez,this);
                Log.d("CanaleActivityInviaImmagine", "imagestring: "+imageString);
                Intent intentInviaImage = new Intent(this, ActivityInviaImmaginePost.class);
                intentInviaImage.putExtra("imageStringBase64", ""+imageString);
                intentInviaImage.putExtra("canale", ""+nomecanaleString);
                startActivity(intentInviaImage);*/
            }
    }
}