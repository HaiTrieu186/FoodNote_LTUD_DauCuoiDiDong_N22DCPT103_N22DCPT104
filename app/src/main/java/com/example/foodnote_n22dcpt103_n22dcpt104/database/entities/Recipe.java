package com.example.foodnote_n22dcpt103_n22dcpt104.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private String instruction;
    private String img;
    private String cuisine;
    private String category;
    @ColumnInfo(name = "ready_in_minutes")
    private int readyInMinutes;
    private int servings;
    @ColumnInfo(name = "is_favorite", defaultValue = "0")
    private boolean isFavorite=false;

    public Recipe(String name, String description, String instruction, String img, String cuisine, String category, int readyInMinutes, int servings, boolean isFavorite) {
        this.name = name;
        this.description = description;
        this.instruction = instruction;
        this.img = img;
        this.cuisine = cuisine;
        this.category = category;
        this.readyInMinutes = readyInMinutes;
        this.servings = servings;
        this.isFavorite = isFavorite;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
};
