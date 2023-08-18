package com.example.atlantatest.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.atlantatest.response.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllUsers(List<User> userList);

    @Query("SELECT * FROM User ORDER BY LOWER(name) ASC")
    List<User> getAllUsers();
}
