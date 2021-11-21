package com.nemowang.passwordmanager;

import androidx.annotation.NonNull;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_account")
public class Account {
    public Account(@NonNull String title, @NonNull String name, @NonNull String password) {
        this.title = title;
        this.name = name;
        this.password = password;
    }

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "password")
    private String password;


    @NonNull
    public String getTitle() { return this.title; }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public String getPassword() {
        return this.password;
    }

    @NonNull
    public String getAccount___() {
        return this.title + " " + this.name;
    }
}
