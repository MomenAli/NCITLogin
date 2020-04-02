package com.eng.momen.ncitlogin.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user where 1")
    LiveData<User> loadUser();

    @Insert
    void insertUser(User user);

    @Delete
    void deleteUser(User user);

}
