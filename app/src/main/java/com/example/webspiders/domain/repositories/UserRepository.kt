package com.example.webspiders.domain.repositories

import com.example.webspiders.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeUsers(searchQuery: String) : Flow<List<User>>
    fun observeUserById(userId: Int) : Flow<User?>
    suspend fun refreshUsers(): Result<Unit>
}