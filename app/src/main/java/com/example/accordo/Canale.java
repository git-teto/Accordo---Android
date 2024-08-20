package com.example.accordo;

public class Canale {
    private String ctitle;
    private boolean mine;
    public Canale(String nome,String mio){
        ctitle=nome;
        if(mio.equals("t")){
            mine=true;
        }
        else{
            mine=false;
        }
    }
    public String getCtitle(){
        return ctitle;
    }
    public boolean isMine(){
        return mine;
    }
}
