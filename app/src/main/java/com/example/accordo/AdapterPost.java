package com.example.accordo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class AdapterPost extends RecyclerView.Adapter<ViewHolderPost> {

    private LayoutInflater mInflater;
    private OnListClickListenerPost myOnLIstClickListenerpost;

    public AdapterPost(Context c, OnListClickListenerPost olclp){

        this.mInflater=LayoutInflater.from(c);
        this.myOnLIstClickListenerpost=olclp;
    }

    @Override
    public ViewHolderPost onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_rowpost, parent, false);
        return new ViewHolderPost(view,myOnLIstClickListenerpost);
    }
    @Override
    public void onBindViewHolder(ViewHolderPost holder, int position) {
        Post p = Model.getInstance().getPostByIndex(position);
        String picturestring=Utils.getPictureFromElencoUserPictureByUid(p.getUid());
        holder.generaCella(p,picturestring);
    }
    @Override
    public int getItemCount() {
        return Model.getInstance().getSizePost();
    }
}
