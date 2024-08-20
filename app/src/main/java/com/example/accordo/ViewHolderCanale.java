package com.example.accordo;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderCanale extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView nomecanale;
    private OnListClickListener myOnListClickListener;
    public ViewHolderCanale(View v, OnListClickListener olcl){
        super(v);
        myOnListClickListener=olcl;
        nomecanale=v.findViewById(R.id.nomeCanaleSingleRow);
        v.setOnClickListener(this);
    }

    public void generaCella(Canale ch){
        Log.d("ViewHolder","setetxt");
        nomecanale.setText(ch.getCtitle());
        //imposto colore cella per segnalare che e un mio canale
        if(ch.isMine()){
            nomecanale.setBackgroundColor(Color.YELLOW);
            //Log.d("ViewHolder"," name canale: "+ch.getCtitle()+" "+ch.isMine()+"");
        }
        else{
            nomecanale.setBackgroundColor(Color.WHITE);
            //  Log.d("ViewHolder","non mio"+ch.getCtitle()+" "+ch.isMine()+"");
        }
    }
    @Override
    public void onClick(View v){
        Log.d("ViewHolder", "OnClik on Element: " + nomecanale.getText().toString() + " with position: " + getAdapterPosition());
        myOnListClickListener.OnListClick(getAdapterPosition(),nomecanale.getText().toString());
    }
}
