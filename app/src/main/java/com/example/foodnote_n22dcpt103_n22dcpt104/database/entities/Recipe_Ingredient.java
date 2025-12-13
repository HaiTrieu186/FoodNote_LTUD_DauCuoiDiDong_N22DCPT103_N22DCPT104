package com.example.foodnote_n22dcpt103_n22dcpt104.database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe_ingredient",
        foreignKeys ={
        @ForeignKey(entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipe_id",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Ingredient.class,
                parentColumns = "id",
                childColumns = "ingredient_id",
                onDelete = ForeignKey.CASCADE),
                },
        indices = {@Index(value = {"ingredient_id","recipe_id"}, unique = true)}
)
public class Recipe_Ingredient {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int recipe_id;
    private int ingredient_id;
    private int quantity;
    private String note;

    public Recipe_Ingredient(String note, int quantity, int ingredient_id, int recipe_id) {
        this.note = note;
        this.quantity = quantity;
        this.ingredient_id = ingredient_id;
        this.recipe_id = recipe_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public int getIngredient_id() {
        return ingredient_id;
    }

    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
