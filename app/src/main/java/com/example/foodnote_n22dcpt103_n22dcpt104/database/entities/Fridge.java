package com.example.foodnote_n22dcpt103_n22dcpt104.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "fridge",
        foreignKeys=@ForeignKey(
                entity = Ingredient.class,
                parentColumns = "id",
                childColumns ="ingredient_id" ))
public class Fridge {
    @PrimaryKey(autoGenerate = false)
    private int ingredient_id;

    public Fridge(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public int getIngredient_id() {
        return ingredient_id;
    }

    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }
}
