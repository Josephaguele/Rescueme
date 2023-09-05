package com.example.rescueme;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_text")
public class UserText {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String text;

    // Getter for id
    public int getId() {
        return id;
    }

    // Setter for id
    public void setId(int id) {
        this.id = id;
    }

    // Getter for text
    public String getText() {
        return text;
    }

    // Setter for text
    public void setText(String text) {
        this.text = text;
    }
}
