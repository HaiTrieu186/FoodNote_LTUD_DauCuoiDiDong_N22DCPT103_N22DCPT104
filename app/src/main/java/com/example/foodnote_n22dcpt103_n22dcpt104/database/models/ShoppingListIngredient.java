package com.example.foodnote_n22dcpt103_n22dcpt104.database.models;

import androidx.room.ColumnInfo;

public class ShoppingListIngredient {
    private int item_id;
    private int shopping_list_id;
    private int ingredient_id;
    private String ingredient_name;
    private String ingredient_img;
    private String ingredient_unit;
    private float total_quantity;
    private boolean is_bought;

    public ShoppingListIngredient() { }

    public ShoppingListIngredient(int item_id, int shopping_list_id, int ingredient_id, String ingredient_name, String ingredient_img, String ingredient_unit, float total_quantity, boolean is_bought) {
        this.item_id = item_id;
        this.shopping_list_id = shopping_list_id;
        this.ingredient_id = ingredient_id;
        this.ingredient_name = ingredient_name;
        this.ingredient_img = ingredient_img;
        this.ingredient_unit = ingredient_unit;
        this.total_quantity = total_quantity;
        this.is_bought = is_bought;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getShopping_list_id() {
        return shopping_list_id;
    }

    public void setShopping_list_id(int shopping_list_id) {
        this.shopping_list_id = shopping_list_id;
    }

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

    public String getIngredient_img() {
        return ingredient_img;
    }

    public void setIngredient_img(String ingredient_img) {
        this.ingredient_img = ingredient_img;
    }

    public String getIngredient_unit() {
        return ingredient_unit;
    }

    public void setIngredient_unit(String ingredient_unit) {
        this.ingredient_unit = ingredient_unit;
    }

    public float getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(float total_quantity) {
        this.total_quantity = total_quantity;
    }

    public boolean isIs_bought() {
        return is_bought;
    }

    public void setIs_bought(boolean is_bought) {
        this.is_bought = is_bought;
    }
}
