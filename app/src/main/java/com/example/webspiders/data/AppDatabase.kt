package com.example.webspiders.data

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.webspiders.data.dao.UserDao
import com.example.webspiders.data.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}