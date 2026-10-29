package com.example.webspiders.network.dtos

import com.example.webspiders.domain.models.User

data class UserDTO(val id: Int, val name: String, val email : String, val companyName : String)

fun UserDTO.toDomain() : User {
    return User(
        id = id,
        name = this.name,
        email = this.email,
        companyName = this.companyName
    )
}


