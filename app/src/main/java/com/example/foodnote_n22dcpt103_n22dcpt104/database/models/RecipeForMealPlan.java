package com.example.foodnote_n22dcpt103_n22dcpt104.database.models;

public class RecipeForMealPlan {
    // Dùng id này để xóa một công thức trong một buổi ăn cụ thể
    public int recipe_session_id;
    public String meal_plan_date;
    public int meal_plan_session;
    public int recipe_id;
    public String recipe_name;
    public String recipe_description;
    public String recipe_img;

    public RecipeForMealPlan(int recipe_session_id, String meal_plan_date, int meal_plan_session, int recipe_id, String recipe_name, String recipe_description, String recipe_img) {
        this.recipe_session_id = recipe_session_id;
        this.meal_plan_date = meal_plan_date;
        this.meal_plan_session = meal_plan_session;
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
        this.recipe_description = recipe_description;
        this.recipe_img = recipe_img;
    }

    public int getRecipe_session_id() {
        return recipe_session_id;
    }

    public void setRecipe_session_id(int recipe_session_id) {
        this.recipe_session_id = recipe_session_id;
    }

    public String getMeal_plan_date() {
        return meal_plan_date;
    }

    public void setMeal_plan_date(String meal_plan_date) {
        this.meal_plan_date = meal_plan_date;
    }

    public int getMeal_plan_session() {
        return meal_plan_session;
    }

    public void setMeal_plan_session(int meal_plan_session) {
        this.meal_plan_session = meal_plan_session;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getRecipe_description() {
        return recipe_description;
    }

    public void setRecipe_description(String recipe_description) {
        this.recipe_description = recipe_description;
    }

    public String getRecipe_img() {
        return recipe_img;
    }

    public void setRecipe_img(String recipe_img) {
        this.recipe_img = recipe_img;
    }
}
