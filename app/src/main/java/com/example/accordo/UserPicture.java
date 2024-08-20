package com.example.accordo;

public class UserPicture {
    private int myuid,mypversion;
    private String mypicture;

    public UserPicture(int uid,int pversion,String picture){
        this.mypicture=picture;
        this.mypversion=pversion;
        this.myuid=uid;
    }
    public UserPicture(int uid,int pversion){
        this(uid,pversion,null);
    }
    public UserPicture(){
        this(0,0,null);
    }

    public int getUid(){return myuid;}
    public int getPversion(){return mypversion;}
    public String getPicture(){return  mypicture;}

    public void setPicture(String picture){ this.mypicture=picture;}
    public void setPversion(int pversion){this.mypversion=pversion;}
    public void setUid(int uid){ this.myuid=uid;}
    public String toString(){
        return "UserPicture uid: "+myuid+" picture: "+mypicture+" pversion: "+mypversion;
    }
}
