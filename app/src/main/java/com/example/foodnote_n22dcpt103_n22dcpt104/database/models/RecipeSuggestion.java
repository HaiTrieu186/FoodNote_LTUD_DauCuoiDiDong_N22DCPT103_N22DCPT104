package com.example.foodnote_n22dcpt103_n22dcpt104.database.models;

import androidx.room.Embedded;

import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Recipe;

public class RecipeSuggestion {

    @Embedded
    public Recipe recipe;

    // 2. Các thông tin thống kê
    public int totalIngredients;      // Tổng số nguyên liệu món này cần
    public int missingIngredients;    // Số nguyên liệu bị thiếu
    public int matchingIngredients;   // Số nguyên liệu tủ lạnh ĐANG CÓ

    // Hàm tính toán % độ phù hợp
    public int getMatchPercentage() {
        if (totalIngredients == 0) return 0;
        return (matchingIngredients * 100) / totalIngredients;
    }


}
