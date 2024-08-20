package com.example.accordo;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

public class ClickManager implements View.OnClickListener{

    Activity myactivity;
    public ClickManager(Activity activity){
        myactivity=activity;
    }
    public void onClick(View v){
        //Log.d("TestClick","immagine onclick");
        switch(v.getId()){
            case R.id.profilo:
            case R.id.tornaaProfilodaCambiaNome:
            case R.id.tornaaProfilodaCambiaImmagine:
                Log.d("ClickManager","profilo onclick");
                Intent intent = new Intent(myactivity, ActivityProfilo.class);
                myactivity.startActivity(intent);
                break;
            case R.id.tornaHomedaScrivi:

                case R.id.homedaprofilo:
                Log.d("ClickManager","torna home");
                Intent intent2 = new Intent(myactivity, MainActivity.class);
                myactivity.startActivity(intent2);
                break;
            case R.id.scrivi:
                 case R.id.tornaaScrividaCanale:
                 case R.id.tornaaScrividaCreaCanale:
                Log.d("ClickManager","scrivi cliccato");
                Intent intent3 = new Intent(myactivity, Scrivi.class);
                myactivity.startActivity(intent3);
                break;
            case R.id.creacanale:
                Log.d("ClickManager","creacanale cliccato");
                Intent intent4 = new Intent(myactivity, CreaCanaleActivity.class);
                myactivity.startActivity(intent4);
                break;
           /* case R.id.tornaacanaledabigimmagine:
                Log.d("ClickManager","vai a canale cliccato");
                Intent intent5 = new Intent(myactivity, CanaleActivity.class);
                myactivity.startActivity(intent5);
                break;*/
            case R.id.cambianomeutente:
                Log.d("ClickManager","vai a cambia nome cliccato");
                Intent intent6 = new Intent(myactivity, CambiaNomeActivity.class);
                myactivity.startActivity(intent6);
                break;
            case R.id.cambiafotoutente:
                Log.d("ClickManager","vai a cambia immagine cliccato");
                Intent intent7 = new Intent(myactivity, CambiaImmagineActivity.class);
                myactivity.startActivity(intent7);
                break;
            case R.id.preparamessaggio:
                Log.d("ClickManager","vai a scrivitextactivity cliccato");
                Intent intent8 = new Intent(myactivity, ScriviTextActivity.class);
                myactivity.startActivity(intent8);
                break;

        }
    }
}
