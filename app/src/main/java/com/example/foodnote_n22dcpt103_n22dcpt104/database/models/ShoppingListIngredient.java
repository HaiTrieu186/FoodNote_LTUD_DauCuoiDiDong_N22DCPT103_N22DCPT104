package com.example.foodnote_n22dcpt103_n22dcpt104.database.models;

import androidx.room.ColumnInfo;

public class ShoppingListIngredient {
    public int item_id;
    public int shopping_list_id;
    public int ingredient_id;
    public String ingredient_name;
    public String ingredient_img;
    public float total_quantity;
    public boolean is_bought;
}
