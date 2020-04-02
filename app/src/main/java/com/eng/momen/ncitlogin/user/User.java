package com.eng.momen.ncitlogin.user;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int dbID;


    @ColumnInfo(name = "user_id")
    public String userID;

    @ColumnInfo(name = "user_name")
    public String userName;

    @Ignore
    public User(String userName, String userID) {
        this.userName = userName;
        this.userID = userID;
    }

    public User(int dbID,String userName, String userID) {
        this.dbID = dbID;
        this.userName = userName;
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
