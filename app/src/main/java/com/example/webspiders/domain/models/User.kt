package com.example.webspiders.domain.models

data class User(val id: Int, val name: String, val email : String, val companyName : String)

//Domain Models, data class User(val id: Int, val name: String, val email : String, val companyName : String)

//Network Layer - impls domain models and services., UserService, getUsers(){return from a fake json for now, but has Retrofit}

// Repostiory Layer - impls domain models, consumes network layer, add caching for User List and returns domain models.

// ui layer-  consumes repository.