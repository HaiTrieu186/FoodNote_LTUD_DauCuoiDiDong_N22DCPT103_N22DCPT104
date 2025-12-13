package com.example.foodnote_n22dcpt103_n22dcpt104.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe_meal_plan",
        foreignKeys ={
                @ForeignKey(entity = Meal_plan.class,
                        parentColumns = "id",
                        childColumns = "meal_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Recipe.class,
                        parentColumns = "id",
                        childColumns = "recipe_id",
                        onDelete = ForeignKey.CASCADE),
        },
        indices = {@Index(value = {"meal_id","recipe_id","session"}, unique = true)}

)
public class Recipe_MealPlan {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int meal_id;
    private int recipe_id;
    private int session;

    public Recipe_MealPlan(int session, int recipe_id, int meal_id) {
        this.session = session;
        this.recipe_id = recipe_id;
        this.meal_id = meal_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMeal_id() {
        return meal_id;
    }

    public void setMeal_id(int meal_id) {
        this.meal_id = meal_id;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }
}
