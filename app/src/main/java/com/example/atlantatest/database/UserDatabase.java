package com.example.atlantatest.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.atlantatest.response.User;

@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    private static volatile UserDatabase INSTANCE;
    public abstract UserDao UserDao();

    public static UserDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    UserDatabase.class, "User_Database").build();
        }
        return INSTANCE;
    }
}
