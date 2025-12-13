package com.example.foodnote_n22dcpt103_n22dcpt104.database.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "shopping_list_item",
        foreignKeys ={
        @ForeignKey(entity = Shopping_list.class,
                parentColumns = "id",
                childColumns = "shopping_list_id",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Ingredient.class,
                parentColumns = "id",
                childColumns = "ingredient_id",
                onDelete = ForeignKey.CASCADE),
                },
        indices = {@Index(value = {"ingredient_id","shopping_list_id"}, unique = true)}
)
public class Shopping_list_item {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int shopping_list_id;
    private int ingredient_id;
    private int total_quantity;
    @ColumnInfo(defaultValue = "0")
    private boolean is_bought=false;

    public Shopping_list_item(boolean is_bought, int total_quantity, int ingredient_id, int shopping_list_id) {
        this.is_bought = is_bought;
        this.total_quantity = total_quantity;
        this.ingredient_id = ingredient_id;
        this.shopping_list_id = shopping_list_id;
    }

    public boolean isIs_bought() {
        return is_bought;
    }

    public void setIs_bought(boolean is_bought) {
        this.is_bought = is_bought;
    }

    public int getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(int total_quantity) {
        this.total_quantity = total_quantity;
    }

    public int getIngredient_id() {
        return ingredient_id;
    }

    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public int getShopping_list_id() {
        return shopping_list_id;
    }

    public void setShopping_list_id(int shopping_list_id) {
        this.shopping_list_id = shopping_list_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
