package com.example.accordo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;

public class ViewHolderPost  extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView nomeutente;
    private TextView content;
    private ImageView fotoprofiloview;
    //private ImageView fotoinviata;
    private OnListClickListenerPost myOnListClickListenerpost;
    public ViewHolderPost(View v, OnListClickListenerPost olclp){
        super(v);
        myOnListClickListenerpost=olclp;
        nomeutente=v.findViewById(R.id.nomeprofilo);
        content=v.findViewById(R.id.contentscritta);
        fotoprofiloview=v.findViewById(R.id.fotoprofilo);
        // fotoinviata=v.findViewById(R.id.contentimmagine);
        v.setOnClickListener(this);
    }

    //genero la cella dell'elenco in base a che tipo di post ho, partendo dalla base single row che ha tutto
    public void generaCella(Post p,String picturestring){
        Log.d("ViewHolder","generacella");
        if(p.getName().equals("null")){
            nomeutente.setText("Anonimo ( "+p.getUid()+" )");}
        else{
            nomeutente.setText(p.getName());
        }
        //nomeutente.setText(p.getUid()+"");

        if(p.getType().equals("t")){
            //fotoinviata.setVisibility(0);
            content.setText(""+p.getContent());
        }
        if(p.getType().equals("i")){
            content.setText("ha inviato un immagine!");
        }
        if(p.getType().equals("l")){
            content.setText("ha inviato la sua posizione!");
        }
        //Log.d("ViewHolder", ""+p.getUid());
        //String picturestring=Utils.getPictureFromElencoUserPictureByUid(p.getUid());
        Log.d("ViewHolder", "string picture e' : "+picturestring);
       /* if(picturestring+"" instanceof String){
            Log.d("ViewHolder", "string picture e' un istanza di String : "+picturestring);
        }
        else{
            Log.d("ViewHolder", "string picture non e' un istanza di String : "+picturestring);
        }*/


        picturestring=picturestring+"";
        //crea immagine da base64
        if(picturestring.equals("null")){
          //  Log.d("ViewHolder", "string picture e' null : "+picturestring+" pversion: "+p.getPversion());
            Log.d("visualizzafoto", "string picture di "+p.getUid()+" nome "+p.getName()+" e' null:  pver "+p.getPversion());
            fotoprofiloview.setImageResource(R.mipmap.iconuser);
        }
        else{
           // Log.d("ViewHolder", "string picture non e' null: "+picturestring+" pversion: "+p.getPversion());
            Log.d("visualizzafoto", "string picture di "+p.getUid()+" nome "+p.getName()+" non e' null:  pver "+p.getPversion());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes;
            imageBytes = Base64.decode(picturestring, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            fotoprofiloview.setImageBitmap(decodedImage);
        }
    }
    @Override
    public void onClick(View v){
        Log.d("ViewHolder", "OnClik on Element: " + nomeutente.getText().toString() + " with position: " + getAdapterPosition());
        myOnListClickListenerpost.OnListClickPost(getAdapterPosition());
    }
}
