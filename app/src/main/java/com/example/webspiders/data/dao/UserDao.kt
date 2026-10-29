package com.example.webspiders.data.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.example.webspiders.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("""
        SELECT * FROM users 
        WHERE (:query = '' OR name LIKE '%' || :query || '%')
        ORDER BY name ASC
    """)
    fun observeUsers(query: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun observeUserById(userId: Int): Flow<UserEntity?>

    @Insert
    suspend fun insertUsers(users : List<UserEntity>)

}