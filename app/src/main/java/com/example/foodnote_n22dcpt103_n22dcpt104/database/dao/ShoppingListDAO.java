package com.example.foodnote_n22dcpt103_n22dcpt104.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Meal_plan;
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
            "where ml.shopping_list_id = :list_id " +
            "GROUP BY ri.ingredient_id")
    void insertShoppingListItems(int list_id);

    // Lấy danh sách item kèm theo
    @Query("Select " +
            "sli.id as item_id, " +
            "sli.shopping_list_id as shopping_list_id, " +
            "i.id as ingredient_id, " +
            "i.name as ingredient_name," +
            "i.img as ingredient_img, " +
            "i.unit as ingredient_unit, " +
            "sli.total_quantity as total_quantity, " +
            "sli.is_bought as is_bought " +
            "from shopping_list_item sli " +
            "inner join ingredient i on sli.ingredient_id = i.id " +
            "where sli.shopping_list_id = :list_id ")
    List<ShoppingListIngredient> getShoppingListWithItems(int list_id);

    // Lấy danh sách các list đi chợ
    @Query("SELECT * FROM shopping_list ORDER BY created_at DESC")
    List<Shopping_list> getAllShoppingLists();

    // Cập nhật trạng thái đã mua của 1 item
    @Query("UPDATE shopping_list_item SET is_bought = :isBought WHERE id = :itemId")
    void updateItemStatus(int itemId, boolean isBought);

    // Cập nhật trạng thái hoàn thành của cả Shopping List
    @Query("UPDATE shopping_list SET status = :status WHERE id = :listId")
    void updateShoppingListStatus(int listId, boolean status);

    //  Kiểm tra xem List đã mua hết chưa (Trả về số lượng item chưa mua)
    @Query("SELECT COUNT(*) FROM shopping_list_item WHERE shopping_list_id = :listId AND is_bought = 0")
    int countUnboughtItems(int listId);

    // Lấy các MealPlan chưa thuộc về ShoppingList nào (để hiện dialog tạo)
    @Query("SELECT * FROM meal_plan WHERE shopping_list_id IS NULL ORDER BY plan_date ASC")
    List<Meal_plan> getAvailableMealPlans();

    // -xóa list
    @Query("DELETE FROM shopping_list WHERE id = :listId")
    void deleteShoppingList(int listId);
}
