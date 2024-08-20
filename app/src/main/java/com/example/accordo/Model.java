package com.example.accordo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Model {
    private ArrayList<Canale> elencoCanali=null;
    private ArrayList<Post> elencoPost=null;
    private ArrayList<UserPicture> elencoUserPictures=null;
    private static Model ourInstance=null;

    private Model(){
        elencoPost=new ArrayList<Post>();
        elencoUserPictures= new ArrayList<UserPicture>();
        elencoCanali=new ArrayList<Canale>();
    }

    public static synchronized Model getInstance(){
        if(ourInstance==null){
            ourInstance=new Model();
        }
        return ourInstance;
    }

   /* public void addFakedata(){
        for(int i=0;i<100;i++){
            elencoCanali.add(new Canale("canale "+i,"t"));
        }
    }*/
    //meyodi gestione array canali
    public ArrayList<Canale> getCanali(){
        /*if(elencoCanali.size()==0){
        }*/
        return elencoCanali;
    }
    public int getSize(){
       return  Model.getInstance().getCanali().size();
    }

    public Canale getCanaleByIndex(int index){
        return Model.getInstance().getCanali().get(index);
    }

    public void addCanali(ArrayList<Canale> arrcanali) {
        elencoCanali.clear();
        elencoCanali.addAll(arrcanali);
    }

    //metodi gestione array post
   public void addFakedata(){
        for(int i=0;i<15;i++){
            if(i<5){
                elencoPost.add(new Post(i+"","sergio "+i,""+i,i+"","t","hh"));
            }
            if(i>=5&&i<10){
                elencoPost.add(new Post(i+"","sergio "+i,""+i,i+"","i"));
            }
            if(i>=10){
                elencoPost.add(new Post(i+"","sergio "+i,""+i,i+"","l","hh"));
            }
        }

    }

    public ArrayList<Post> getPost(){
        /*if(elencoCanali.size()==0){
        }*/
        return elencoPost;
    }
    public int getSizePost(){
        return  Model.getInstance().getPost().size();
    }
    public Post getPostByIndex(int index){
        return Model.getInstance().getPost().get(index);
    }

    // dai post scaricati creo un elenco di foto utenti che richiedo
    public void addPosts(ArrayList<Post> arrposts) {
        elencoPost.clear();
        elencoUserPictures.clear();
        elencoPost.addAll(arrposts);
        for(Post p : arrposts){
            // Log.d("Model",p.toString());
            UserPicture userpicture=new UserPicture(p.getUid(),p.getPversion());
            if(elencoUserPictureContiene(userpicture)){
                Log.d("Model",userpicture.getUid()+" utente già presente");
            }
            else{
                Log.d("Model",userpicture.getUid()+" utente non presente");
                elencoUserPictures.add(userpicture);
            }
        }
        Log.d("Model","size elenco foto da richiedere: "+elencoUserPictures.size());
    }

    //metodi gestione array userPicture
    public ArrayList<UserPicture> getElencoUserPictures() {
        return elencoUserPictures;
    }

    public void aggiornaUserPicture(UserPicture userpicture){
        for(int i=0;i<elencoUserPictures.size();i++){
            if(userpicture.getUid()==elencoUserPictures.get(i).getUid()){
                elencoUserPictures.remove(i);
                elencoUserPictures.add(userpicture);
            }
        }
    }

    public boolean elencoUserPictureContiene(UserPicture userpicture){
        for(UserPicture myuserpicture : elencoUserPictures){
            if(myuserpicture.getUid()==userpicture.getUid()){
                return true;
            }
        }
        return false;
    }

    public UserPicture deserializzaUserPicture(JSONObject object){
        UserPicture downloadUserPicture=new UserPicture();
        try {
            downloadUserPicture.setUid(Integer.parseInt(object.getString("uid")));
            if(object.getString("picture")==null){}
            else{
                downloadUserPicture.setPicture(Utils.aggiustaStringPicture(object.getString("picture")));
            }
            downloadUserPicture.setPversion(Integer.parseInt(object.getString("pversion")));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return downloadUserPicture;
    }

    public String getUserPictureByUid(int uid){
        for(UserPicture userpicture: elencoUserPictures){
            if(userpicture.getUid()==uid){
                return userpicture.getPicture();
            }
        }
        return "";
    }
}
