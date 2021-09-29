package com.github.daggerok.openfeign.jsonuserapi

// Commands:

data class CreateUserCommand(
    val name: String = "",
    val age: Int = -1,
)

// Responses:

data class UsersDocument(
    val users: List<UserDTO> = listOf(),
)

data class UserDocument(
    val userDTO: UserDTO = UserDTO(),
)

// DTOs:

data class UserDTO(
    val name: String = "",
    val age: Int = -1,
)
