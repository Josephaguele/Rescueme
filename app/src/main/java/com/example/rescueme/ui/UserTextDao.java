package com.example.rescueme.ui;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.rescueme.UserText;

@Dao
public interface UserTextDao {
    @Insert
    void insert(UserText userText);

    // You can add other queries and operations here if needed
}
