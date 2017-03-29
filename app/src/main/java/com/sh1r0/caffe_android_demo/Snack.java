package com.sh1r0.caffe_android_demo;

/**
 * Created by Even on 2017/3/26.
 */

public class Snack {


    private String name;
    private String calorie;
    private int imgResId;

    public Snack(String name,String calorie,int imgResId){
        this.name = name;
        this.calorie = calorie;
        this.imgResId = imgResId;
    }


    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
