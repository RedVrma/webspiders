package com.example.webspiders.data.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.webspiders.domain.models.User

@Entity(tableName = "users")
data class UserEntity(@PrimaryKey val id: Int, val name: String, val email : String, val companyName : String)

fun UserEntity.toDomain() : User = User(
    id = id,
    name = name,
    email = email,
    companyName = companyName
)

fun User.toEntity() : UserEntity = UserEntity(
    id = id,
    name = name,
    email = email,
    companyName = companyName
)