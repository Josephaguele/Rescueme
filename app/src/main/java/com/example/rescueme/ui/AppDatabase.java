package com.example.rescueme.ui;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.rescueme.UserText;

@Database(entities = {UserText.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserTextDao userTextDao();
}
