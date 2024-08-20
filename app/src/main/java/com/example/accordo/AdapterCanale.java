package com.example.accordo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterCanale extends RecyclerView.Adapter<ViewHolderCanale> {
    private LayoutInflater mInflater;
    private OnListClickListener myOnLIstClickListener;

    public AdapterCanale(Context c, OnListClickListener olcl){

        this.mInflater=LayoutInflater.from(c);
        this.myOnLIstClickListener=olcl;
    }

    @Override
    public ViewHolderCanale onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_row, parent, false);
        return new ViewHolderCanale(view,myOnLIstClickListener);
    }
    @Override
    public void onBindViewHolder(ViewHolderCanale holder, int position) {
        Canale ch = Model.getInstance().getCanaleByIndex(position);
        holder.generaCella(ch);
    }
    @Override
    public int getItemCount() {
        return Model.getInstance().getSize();
    }
}
