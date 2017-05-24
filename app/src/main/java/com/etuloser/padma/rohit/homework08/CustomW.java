package com.etuloser.padma.rohit.homework08;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rohit on 4/8/2017.
 */

public class CustomW  implements Serializable {

    private  String condition;
    private String tempC;
    private String tempF;
    private String lupdat;
    private String img;
    private String name;
    private String coun;
    private Boolean IsFav;
    private String key;


    public CustomW(String condition, String tempC, String tempF, String lupdat, String img, String name, String coun, Boolean isFav, String key) {
        this.condition = condition;
        this.tempC = tempC;
        this.tempF = tempF;
        this.lupdat = lupdat;
        this.img = img;
        this.name = name;
        this.coun = coun;
        IsFav = isFav;
        this.key = key;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getTempC() {
        return tempC;
    }

    public void setTempC(String tempC) {
        this.tempC = tempC;
    }

    public String getTempF() {
        return tempF;
    }

    public void setTempF(String tempF) {
        this.tempF = tempF;
    }

    public String getLupdat() {
        return lupdat;
    }

    public void setLupdat(String lupdat) {
        this.lupdat = lupdat;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoun() {
        return coun;
    }

    public void setCoun(String coun) {
        this.coun = coun;
    }

    public Boolean getFav() {
        return IsFav;
    }

    public void setFav(Boolean fav) {
        IsFav = fav;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    Map<String,Object> toMap(CustomW cw)
    {

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("key",cw.key);
        map.put("name",cw.name);
        map.put("coun",cw.coun);
        map.put("tempC",cw.tempC);
        map.put("tempF",cw.tempF);
        map.put("IsFav",cw.IsFav);
        map.put("lupdat",cw.lupdat);


        return map;

    }

}
