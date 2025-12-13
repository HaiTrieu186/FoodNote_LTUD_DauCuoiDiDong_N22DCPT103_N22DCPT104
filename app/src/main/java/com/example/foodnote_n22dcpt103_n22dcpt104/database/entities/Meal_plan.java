package com.example.foodnote_n22dcpt103_n22dcpt104.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "meal_plan",
        foreignKeys = @ForeignKey(
                entity = Shopping_list.class,
                parentColumns = "id",
                childColumns = "shopping_list_id"),
        indices = {@Index(value = {"plan_date"}, unique = true)})

public class Meal_plan {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "plan_date")
    private String planDate;
    private String note;
    @ColumnInfo(name = "shopping_list_id")
    private Integer shoppingListId;

    public Meal_plan(Integer shoppingListId, String note, String planDate) {
        this.shoppingListId = shoppingListId;
        this.note = note;
        this.planDate = planDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(Integer shoppingListId) {
        this.shoppingListId = shoppingListId;
    }
};
