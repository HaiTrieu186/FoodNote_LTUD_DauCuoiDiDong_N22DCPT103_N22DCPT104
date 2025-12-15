package com.example.foodnote_n22dcpt103_n22dcpt104.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Meal_plan;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Recipe_MealPlan;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.models.RecipeForMealPlan;

import java.util.List;

@Dao
public interface MealPlanDAO {

 ///////////////////////INSERT////////////////////////////////
    // Tạo một meal_plan mới (ứng với 1 ngày - không được trùng)
    @Insert
    long insertMealPlan (Meal_plan mealPlan);

    // Thêm  một công thức cụ thể vào 1 buổi ăn
    @Insert
    void insertRecipeToMeal (Recipe_MealPlan recipeMealPlan);
//////////////////////////////////////////////////////////////



/////////////////////////////SELECT///////////////////////////
    // Lấy id của meal_plan bằng ngày
    @Query("Select id from meal_plan where plan_date= :plan_date ")
    long getIDMealPlanByDate(String plan_date);

    // Lấy plan_date bằng meal_id
    @Query("Select plan_date from meal_plan where id = :id ")
    String getDateMealPlanByID(int id);

    // Lấy thông tin của meal_plan bằng ngày.
    @Query("Select *" +
            "from meal_plan " +
            "where plan_date = :plan_date")
    Meal_plan getMealPlanByDate(String plan_date);

    // Lấy thông tin của meal_plan bằng id.
    @Query("Select *" +
            "from meal_plan " +
            "where id = :id ")
    Meal_plan getMealPlanByID(int id);

    // Lây danh sách toàn bộ công thức meal_plan băng id (tất cả các buổi)
    @Query("Select " +
            "rml.id as recipe_session_id, " +
            "ml.plan_date as meal_plan_date," +
            "rml.session as  meal_plan_session, " +
            "r.id as recipe_id," +
            "r.name as recipe_name," +
            "r.description as recipe_description," +
            "r.img as recipe_img " +
            "from meal_plan ml " +
            "inner join recipe_meal_plan rml on ml.id = rml.meal_id " +
            "inner join recipe r on rml.recipe_id = r.id " +
            "where ml.id = :id  ")
    List<RecipeForMealPlan> getRecipesFromMealPlan(int id);

    // Lây danh sách toàn bộ công thức meal_plan băng id theo buổi cụ thể
    @Query("Select " +
            "rml.id as recipe_session_id, " +
            "ml.plan_date as meal_plan_date," +
            "rml.session as  meal_plan_session, " +
            "r.id as recipe_id," +
            "r.name as recipe_name," +
            "r.description as recipe_description," +
            "r.img as recipe_img " +
            "from meal_plan ml " +
            "inner join recipe_meal_plan rml on ml.id = rml.meal_id " +
            "inner join recipe r on rml.recipe_id = r.id " +
            "where ml.id = :mealId AND rml.session = :session")
    List<RecipeForMealPlan> getRecipesFromMealPlanBySession(int mealId, int session);
/////////////////////////////////////////////////////////////



 /////////////////////////UPDATE/////////////////////////////
 // Thêm list_id vào nhiều meal_plan (do khi tạo list được phép chọn nhiều meal_plan
 @Query("UPDATE meal_plan " +
         "SET shopping_list_id = :shoppingListId " +
         "WHERE id IN (:mealPlanIds)")
 void addMealsToShoppingList(int shoppingListId, List<Integer> mealPlanIds);

// Khi xóa Shopping List (phải set hàng loạt mela tương ứng)
@Query("UPDATE meal_plan SET shopping_list_id = NULL WHERE shopping_list_id = :shoppingListId")
void removeShoppingListFromMeals(int shoppingListId);
 ////////////////////////////////////////////////////////////





///////////////////////////DELETE////////////////////////////
    // Xóa bằng cách truyền object
    @Delete
    void deleteMealPlan(Meal_plan mealPlan);

    // Xóa bằng ID
    @Query("Delete from meal_plan where id = :id ")
    void deleteMealPlanByID(int id);

    // Xóa một công thức trong một buổi ăn cụ thể (xài id bang trung gian)
    @Query("Delete from recipe_meal_plan where id = :recipe_session_id ")
    void deleteRecipeFromMealPlan(int recipe_session_id);
/// ///////////////////////////////////////////////////////////////





}
