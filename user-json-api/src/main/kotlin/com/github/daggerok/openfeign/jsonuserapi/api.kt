package com.github.daggerok.openfeign.jsonuserapi

// Commands:

data class CreateUserCommand(
    val name: String = "",
    val age: Int = -1,
)

// Queries:

data class GetUsersByAgeQuery(
    val age: Int = -1,
)

// Responses:

data class UserDocument(
    val userDTO: UserDTO = UserDTO(),
)

data class UsersDocument(
    val users: List<UserDTO> = listOf(),
)

// DTOs:

data class UserDTO(
    val name: String = "",
    val age: Int = -1,
)
