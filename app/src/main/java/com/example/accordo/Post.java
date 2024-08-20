package com.example.accordo;

public class Post {
    private int myuid;
    private String myname;
    private int mypversion;
    private int mypid;
    private String mytype;
    private String mycontent;
    private double mylatitudine;
    private double mylongitudine;
    public Post(String uid,String name,String pversion,String pid,String type,String content,String latitudine,String longitudine){
        myuid=Integer.parseInt(uid);
        myname=name;
        mypversion=Integer.parseInt(pversion);
        mypid=Integer.parseInt(pid);
        mytype=type;
        mycontent=content;
        mylatitudine=Double.parseDouble(latitudine);
        mylongitudine=Double.parseDouble(longitudine);
    }
    //testo
    public Post(String uid,String name,String pversion,String pid,String type,String content){
        this(uid,name,pversion,pid,type,content,"0","0");
    }
    //immagine
    public Post(String uid,String name,String pversion,String pid,String type){
        this(uid,name,pversion,pid,type,null,"0","0");
    }
    //posizione
    public Post(String uid,String name,String pversion,String pid,String type,String latitudine,String longitudine){
        this(uid,name,pversion,pid,type,null,latitudine,longitudine);
    }

    public int getUid(){return myuid;}
    public String getName(){return myname;}
    public int getPversion(){return mypversion;}
    public int getPid(){ return mypid;}
    public String getType(){return mytype;}
    public String getContent(){ return mycontent;}
    public double getLatitudine(){ return mylatitudine;}
    public double getLongitudine(){ return mylongitudine;}
    public String toString(){
        return "il post id: "+mypid;
    }

}
