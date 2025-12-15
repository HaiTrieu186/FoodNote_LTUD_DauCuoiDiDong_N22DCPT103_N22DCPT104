package com.example.foodnote_n22dcpt103_n22dcpt104.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Shopping_list;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.models.ShoppingListIngredient;

import java.util.List;

@Dao
public interface ShoppingListDAO {

    @Insert
    long insertShoppingList(Shopping_list shoppingList);

    // Chèn danh sách nguyên liệu cần mua (được gọi sau khi đã thêm list_id vào các meal_plan)
    @Query("Insert into shopping_list_item(shopping_list_id, ingredient_id, total_quantity)" +
            "Select " +
            " :list_id, " +
            " ri.ingredient_id, " +
            " sum(ri.quantity) " +
            "from meal_plan ml " +
            "inner join recipe_meal_plan rml on ml.id = rml.meal_id " +
            "inner join recipe_ingredient ri on rml.recipe_id = ri.recipe_id " +
            "where ml.shopping_list_id = :list_id")
    void insertShoppingListItems(int list_id);

    // Lấy danh sách item kèm theo
    @Query("Select " +
            "sli.id as item_id, " +
            "sli.shopping_list_id as shopping_list_id, " +
            "i.id as ingredient_id, " +
            "i.name as ingredient_name," +
            "i.img as ingredient_img, " +
            "sli.total_quantity as total_quantity, " +
            "sli.is_bought as is_bought " +
            "from shopping_list_item sli " +
            "inner join ingredient i on sli.ingredient_id = i.id " +
            "where sli.shopping_list_id = :list_id ")
    List<ShoppingListIngredient> getShoppingListWithItems(int list_id);

    // Lấy danh sách các list đi chợ
    @Query("SELECT * FROM shopping_list ORDER BY created_at DESC")
    List<Shopping_list> getAllShoppingLists();
}
