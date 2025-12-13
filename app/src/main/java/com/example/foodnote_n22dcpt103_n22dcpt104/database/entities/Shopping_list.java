package com.example.foodnote_n22dcpt103_n22dcpt104.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shopping_list")
public class Shopping_list {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    @ColumnInfo(defaultValue = "false")
    private boolean status;
    @ColumnInfo(name = "created_at")
    private long createdAt = System.currentTimeMillis();

    public Shopping_list(String name, boolean status, long createdAt) {
        this.name = name;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
