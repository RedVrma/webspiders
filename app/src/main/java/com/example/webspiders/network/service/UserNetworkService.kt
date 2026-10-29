package com.example.webspiders.network.service

import com.example.webspiders.domain.models.User
import com.example.webspiders.network.client.UserService
import com.example.webspiders.network.dtos.toDomain

class UserNetworkService(private val userService: UserService) {
    suspend fun fetchUsers() : List<User>{
        return userService.getUsers().map { it.toDomain()}
    }
}