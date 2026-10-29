package com.example.webspiders.data.repository

import com.example.webspiders.data.dao.UserDao
import com.example.webspiders.data.entity.toDomain
import com.example.webspiders.data.entity.toEntity
import com.example.webspiders.domain.models.User
import com.example.webspiders.domain.repositories.UserRepository
import com.example.webspiders.network.service.UserNetworkService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val networkDataSource: UserNetworkService,
    private val userDao: UserDao
) : UserRepository {

    override fun observeUsers(searchQuery: String): Flow<List<User>> {
        return userDao.observeUsers(searchQuery.trim()).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeUserById(userId: Int): Flow<User?> {
        return userDao.observeUserById(userId).map { it?.toDomain() }
    }

    override suspend fun refreshUsers(): Result<Unit> {
        return runCatching {
            val remoteUsers: List<User> = networkDataSource.fetchUsers()
            userDao.insertUsers(remoteUsers.map { it.toEntity() })
        }
    }
}