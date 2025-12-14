package com.example.foodnote_n22dcpt103_n22dcpt104.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredient")
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public int category; // 1. Thịt, 2.Rau, 3. Gia vị,.......
    public String img;
    public String unit; // gram, quả, thìa... (bọn em dùng đơn vị nhỏ nhất để lưu trữ)

    public Ingredient(String name, int category, String unit) {
        this.name = name;
        this.category = category;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
