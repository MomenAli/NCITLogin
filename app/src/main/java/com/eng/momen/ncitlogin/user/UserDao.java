package com.eng.momen.ncitlogin.user;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User")
    User loadUser();

    @Insert
    void insertUser(User user);

    @Delete
    void deleteUser(User user);

}
