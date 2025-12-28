package com.example.foodnote_n22dcpt103_n22dcpt104.database.models;

public class IngredientsByRecipe {
    public int ingredient_id;
    public String ingredient_name;
    public String ingredient_unit;
    public float quantity_needed;
    public String ingredient_note;
    public String ingredient_img;

    public int getIngredient_id() {
        return ingredient_id;
    }

    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    public String getIngredient_unit() {
        return ingredient_unit;
    }

    public void setIngredient_unit(String ingredient_unit) {
        this.ingredient_unit = ingredient_unit;
    }

    public float getQuantity_needed() {
        return quantity_needed;
    }

    public void setQuantity_needed(float quantity_needed) {
        this.quantity_needed = quantity_needed;
    }

    public String getIngredient_note() {
        return ingredient_note;
    }

    public void setIngredient_note(String ingredient_note) {
        this.ingredient_note = ingredient_note;
    }

    public String getIngredient_img() {
        return ingredient_img;
    }

    public void setIngredient_img(String ingredient_img) {
        this.ingredient_img = ingredient_img;
    }
}
