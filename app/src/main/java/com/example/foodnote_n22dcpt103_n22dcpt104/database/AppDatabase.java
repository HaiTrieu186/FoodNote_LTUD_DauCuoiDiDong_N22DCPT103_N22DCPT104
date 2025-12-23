package com.example.foodnote_n22dcpt103_n22dcpt104.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.foodnote_n22dcpt103_n22dcpt104.database.dao.IngredientDAO;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.dao.MealPlanDAO;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.dao.RecipeDAO;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.dao.ShoppingListDAO;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Fridge;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Ingredient;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Meal_plan;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Recipe;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Recipe_Ingredient;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Recipe_MealPlan;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Shopping_list;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Shopping_list_item;

@Database(entities = {
        Recipe.class,
        Ingredient.class,
        Recipe_Ingredient.class,
        Fridge.class,
        Meal_plan.class,
        Recipe_MealPlan.class,
        Shopping_list.class,
        Shopping_list_item.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "foodnote.db";
    // Biến instance để đảm bảo chỉ có 1 kết nối
    private static AppDatabase instance;

    // KHAI BÁO CÁC DAO
    public abstract RecipeDAO recipeDAO();
    public abstract IngredientDAO ingredientDao();
    public abstract MealPlanDAO mealPlanDao();
    public abstract ShoppingListDAO shoppingListDao();

    //  HÀM KHỞI TẠO INSTANCE
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                            .createFromAsset("foodnote.db")
                            .build();
        }
        return instance;
    }
}
