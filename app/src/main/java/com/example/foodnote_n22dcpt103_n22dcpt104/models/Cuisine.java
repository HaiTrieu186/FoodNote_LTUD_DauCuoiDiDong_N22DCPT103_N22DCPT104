package com.example.foodnote_n22dcpt103_n22dcpt104.models;

public class Cuisine {
    private String nameDisplay; // Tên hiển thị (VD: "Ẩm thực Việt")
    private String imgName;     // Tên ảnh trong assets (VD: "cuisine_vietnam")
    private String cuisine;     // Từ khóa để query trong DB (VD: "Vietnamese")

    public Cuisine(String nameDisplay, String imgName, String cuisine) {
        this.nameDisplay = nameDisplay;
        this.imgName = imgName;
        this.cuisine = cuisine;
    }

    public String getNameDisplay() {
        return nameDisplay;
    }

    public void setNameDisplay(String nameDisplay) {
        this.nameDisplay = nameDisplay;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }
}
