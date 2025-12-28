package com.example.foodnote_n22dcpt103_n22dcpt104.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Ingredient;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.models.RecipeSuggestion;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.models.RecipesByIngredient;

import java.util.List;

@Dao
public interface IngredientDAO {

///////////////////INSERT/////////////////////////
    // Thêm 1 nguyên liệu mới
    @Insert
    long insertIngredient(Ingredient ingredient);

    // Thêm nhiều nguyên liệu mới
    @Insert
    void insertIngredients(List<Ingredient> ingredientList);
/////////////////////////////////////////////////



////////////////////Update////////////////////////
    // Cập nhật thông tin nguyên liệu
    @Update
    void updateIngredient(Ingredient ingredient);
//////////////////////////////////////////////////



////////////////////Delete////////////////////////
    //Xóa nguyên liệu
    @Query("Delete from ingredient where id = :id ")
    void deleteIngredientById(int id);
//////////////////////////////////////////////////



////////////////////Select////////////////////////
    // Lấy tất cả nguyên liệu
    @Query("Select * from ingredient")
    List<Ingredient> getAllIngredients();

    // Lấy danh sách công thức có chứa nguyên liệu
    @Query("Select " +
            "r.id as recipe_id, " +
            "r.img as recipe_img, " +
            "r.ready_in_minutes as ready_in_minutes, " +
            "r.description as recipe_description " +
            "from recipe_ingredient ri inner join recipe r " +
            "on ri.recipe_id = r.id " +
            "where ri.ingredient_id = :ingredient_id" )
    List<RecipesByIngredient> getRecipesByIngredient(int ingredient_id);


    // TÌm kiếm nguyên liệu theo tên
    @Query("select * from ingredient " +
            "where name like '%' || :keyword || '%' ")
    List<Ingredient> searchIngredientsByName(String keyword);

    // Tìm kiếm nguyên liệu theo phân loại
    @Query("select * from ingredient " +
            "where category = :category ")
    List<Ingredient> searchIngredientsByCategory(int category);


    // Tìm kiếm nguyên liệu theo Tên VÀ Phân loại
    @Query("SELECT * FROM ingredient WHERE " +
            "(LOWER(name) LIKE '%' || LOWER(:keyword) || '%') " +
            "AND (:category = 0 OR category = :category)")
    List<Ingredient> searchIngredients(String keyword, int category);
//////////////////////////////////////////////////





////////////////////// Frigde (Thực thể yếu của Ingredients)//////////////////

    // Thêm món vào tủ lạnh
    @Query("Insert into fridge(ingredient_id) values (:ingredient_id) ")
    void addToFridge(int ingredient_id);

    // Xóa nguyên liệu khỏi tủ lạnh
    @Query("Delete from fridge where ingredient_id = :ingredient_id ")
    void removeFromFridge(int ingredient_id);

    // Check nguyên liệu đã có trong tủ lạnh chưa
    @Query("SELECT EXISTS(SELECT * FROM fridge WHERE ingredient_id = :ingId)")
    boolean checkIfInFridge(int ingId);

    // Lấy nguyên liệu hiện có trong tủ lạnh
    @Query("Select * " +
            "from fridge f inner join ingredient i " +
            "on f.ingredient_id = i.id")
    List<Ingredient> getAllFridgeItems();

    // Gợi ý món ăn dựa trên Tủ lạnh (Sắp xếp theo độ ưu tiên: Thiếu ít nhất -> Thiếu nhiều)
    @Query("SELECT " +
            "r.*, " +
            // Đếm tổng nguyên liệu món đó cần
            "(SELECT COUNT(*) FROM recipe_ingredient WHERE recipe_id = r.id) as totalIngredients, " +
            // Đếm số nguyên liệu TRÙNG KHỚP với tủ lạnh
            "COUNT(f.ingredient_id) as matchingIngredients, " +
            // Tính số lượng thiếu = Tổng - Có sẵn
            "((SELECT COUNT(*) FROM recipe_ingredient WHERE recipe_id = r.id) - COUNT(f.ingredient_id)) as missingIngredients " +

            "FROM recipe r " +
            "INNER JOIN recipe_ingredient ri ON r.id = ri.recipe_id " +
            "INNER JOIN fridge f ON ri.ingredient_id = f.ingredient_id " +
            "GROUP BY r.id " +
            "ORDER BY missingIngredients ASC, matchingIngredients DESC")
    List<RecipeSuggestion> getSuggestedRecipes();
//////////////////////////////////////////////////////////////////////////////
}
