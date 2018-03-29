package com.example.hospital_one.searchresult;

import android.widget.LinearLayout;

public class Doctor {
    
    private String name;
    private String keshi;
    private String zhicheng;
    private String special;
    private int imageid;

    public Doctor(String name,String keshi,String zhicheng,String special,int imageid){
        this.name = name;
        this.keshi = keshi;
        this.zhicheng = zhicheng;
        this.special = special;
        this.imageid = imageid;
    }
    public String getName(){
        return name;
    }

    public String getKeshi(){
        return keshi;
    }
    public String getZhicheng(){
        return zhicheng;
    }
    public String getSpecial(){
        return special;
    }
    public int getImageid(){
        return imageid;
    }

}
