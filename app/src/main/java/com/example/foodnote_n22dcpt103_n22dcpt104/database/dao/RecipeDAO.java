package com.example.foodnote_n22dcpt103_n22dcpt104.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Recipe;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Recipe_Ingredient;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.models.IngredientsByRecipe;

import java.util.List;

@Dao
public interface RecipeDAO {

///////////////////INSERT/////////////////////////
    // Thêm 1 công thức vào bảng
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertRecipe(Recipe recipe);

    // Thêm nhiều công thức vào bảng
    @Insert
    void insertListRecipe(List<Recipe> recipeList);

    // Thêm danh sách nguyên liệu cho công thức
    @Insert
    void insertRecipeIngredients(List<Recipe_Ingredient> recipeIngredientList);
///////////////////////////////////////////////////



 ///////////////////Update/////////////////////////
    // Cập nhật 1 công thức cụ thể
    @Update
    void updateRecipe(Recipe recipe);

    // Cập nhật yêu thích công thức
    @Query("Update recipe " +
            "set is_favorite= :status " +
            "where id = :id ")
    void updateFavorite(int id,boolean status);
///////////////////////////////////////////////////



//////////////////////Delete//////////////////////
    // Xóa 1 công thức
    @Delete
    void deleteRecipe(Recipe recipe);

    // Xóa 1 công thức theo id
    @Query("Delete from recipe " +
            "where id= :id ")
    void deleteRecipeById(int id);
///////////////////////////////////////////////////



//////////////////////SELECT//////////////////////
    // Lấy 1 chi tiết 1 công thức theo id
    @Query("Select * from recipe where id = :id")
    Recipe getRecipeById(int id);

    // Lấy toàn bộ công thức hiện có
    @Query("Select * from recipe")
    List<Recipe> getAllRecipe();

    // Lấy danh sách công thức yêu thích
    @Query("Select * from recipe where is_favorite = true ")
    List<Recipe> getAllFavoritedRecipe();

    // Lấy danh sách nguyên liệu theo công thức
    @Query("Select " +
            "i.id as ingredient_id, " +
            "i.name as ingredient_name, " +
            "i.unit as ingredient_unit, " +
            "ri.quantity  as quantity_needed, " +
            "ri.note as ingredient_note," +
            "i.img as ingredient_img " +
            "from recipe r " +
            "inner join recipe_ingredient ri on r.id = ri.recipe_id " +
            "inner join ingredient i on i.id = ri.ingredient_id " +
            "where r.id = :id ")
    List<IngredientsByRecipe> getAllIngredientsByRecipe(int id);

    // Tìm công thức theo tên
    @Query("Select * " +
            "from recipe " +
            "where name like '%' || :keyword || '%' ")
    List<Recipe> searchRecipeByName(String keyword);

    // Tìm công thức theo phân loại
    @Query("Select * " +
            "from recipe " +
            "where category = :category ")
    List<Recipe> searchRecipeByCategory(int category);

    // Tìm công thức theo vùng ẩm thực
    @Query("Select * " +
            "from recipe " +
            "where cuisine = :cuisine ")
    List<Recipe> searchRecipeByCuisine(String cuisine);
//////////////////////////////////////////////////

}
