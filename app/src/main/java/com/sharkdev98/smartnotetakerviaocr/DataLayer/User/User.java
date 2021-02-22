package com.sharkdev98.smartnotetakerviaocr.DataLayer.User;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static androidx.room.ColumnInfo.INTEGER;
import static androidx.room.ColumnInfo.TEXT;

@Entity(tableName = "User")
public class User {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "UserId" , typeAffinity = INTEGER)
    public int UserId;

    @ColumnInfo(name = "UserName" , typeAffinity = TEXT)
    public String UserName;

    @ColumnInfo(name = "EmailAddress" , typeAffinity = TEXT)
    public String EmailAddress;

    @ColumnInfo(name = "Password" , typeAffinity = TEXT)
    public String Password;

    @ColumnInfo(name = "LastLogin" , typeAffinity = INTEGER)
    public boolean LastLogin;
}
