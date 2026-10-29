package com.example.webspiders.network.client

import com.example.webspiders.network.dtos.UserDTO
import retrofit2.http.GET

interface UserService {

    @GET("users")
    suspend fun getUsers() : List<UserDTO>
}

