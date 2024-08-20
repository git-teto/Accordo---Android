package com.example.accordo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Utils {

    public static String PREFS_NAME="miePreferenze";
    public static String getPersonalSid(Activity a){
        SharedPreferences settings = a.getSharedPreferences(PREFS_NAME, 0);
        String personalSid = settings.getString("personal_sid", "valore default");
        return personalSid;
    }

    public static ArrayList<Canale> getArrayCanaliFromJsonObject(JSONObject jsonObject) {
        JSONArray JSONarrayCanali= null;
        try {
            JSONarrayCanali = jsonObject.getJSONArray("channels");
            final ArrayList<Canale> arrayCanali=new ArrayList<>();
            for(int i=0;i<JSONarrayCanali.length();i++){
                JSONObject obj=JSONarrayCanali.getJSONObject(i);
                arrayCanali.add(new Canale(obj.getString("ctitle"),obj.getString("mine")));
            }
            return arrayCanali;
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }

    }

    public static String aggiustaStringPicture(String pictureRotta) {
        String aggiustata=pictureRotta.replaceAll("\\/","/");
        return aggiustata;
    }

    public static String getPictureFromElencoUserPictureByUid(int uid) {
        return Model.getInstance().getUserPictureByUid(uid);
    }
    public static ArrayList<Post> getArrayPostFromJsonObject(JSONObject jsonObject){
        try {
            JSONArray JSONarrayPosts=jsonObject.getJSONArray("posts");
            final ArrayList<Post> arrayposts=new ArrayList<>();
            for(int i=0;i<JSONarrayPosts.length();i++){
                JSONObject obj=JSONarrayPosts.getJSONObject(i);

                switch(obj.getString("type")){
                    case "t":
                        arrayposts.add(new Post(obj.getString("uid"),obj.getString("name"),
                                obj.getString("pversion"),obj.getString("pid"),obj.getString("type"),obj.getString("content")));
                        //{"uid":"2986","name":null,"pversion":"0","pid":"8506","type":"t","content":"zio"}]
                        break;
                    case "i":
                        arrayposts.add(new Post(obj.getString("uid"),obj.getString("name"),
                                obj.getString("pversion"),obj.getString("pid"),obj.getString("type")));
                        break;
                    case "l":
                        arrayposts.add(new Post(obj.getString("uid"),obj.getString("name"),
                                obj.getString("pversion"),obj.getString("pid"),obj.getString("type"),
                                obj.getString("lat"),obj.getString("lon")));
                        break;
                }
            }
            return arrayposts;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }




    public static Bitmap decodoficaBase64inImmagine(String immagine){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes;
        imageBytes = Base64.decode(immagine, Base64.DEFAULT);
        Log.d("dimimmagine","dim: "+imageBytes.length);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }

    public static String codificaUriInStringBase64(Uri picturez, Activity a){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = null;
        try {
            //uri image convertito in bitmap
            bitmap = MediaStore.Images.Media.getBitmap(a.getContentResolver(), picturez);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //bitmap diventa bytearrayoutputstrem
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        //ottengo un byte array da bytearrayoutputstream
        byte[] imageBytes = baos.toByteArray();
        if(imageBytes.length<137000){
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
        else{
            return null;
        }
        //converto bytearray in base64 string e lo ritorno
       // return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
