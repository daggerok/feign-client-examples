package com.github.daggerok.openfeign.userjsonspringwebserver

import com.github.daggerok.openfeign.jsonuserapi.CreateUserCommand
import com.github.daggerok.openfeign.jsonuserapi.UserDTO
import com.github.daggerok.openfeign.jsonuserapi.UserDocument
import com.github.daggerok.openfeign.jsonuserapi.UsersDocument
import java.util.Optional
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class UserJsonResource {

    @GetMapping("/api/v1/get-users/{age}")
    fun getUsers(@PathVariable("age") age: Optional<Int>): UsersDocument =
        UsersDocument(
            listOf(
                UserDTO(
                    name = "Maksimko",
                    age = age.orElse(38),
                )
            )
        )

    @ResponseStatus(CREATED)
    @PostMapping("/api/v1/create-user")
    fun createUser(@RequestBody request: CreateUserCommand): UserDocument =
        UserDocument(
            UserDTO(
                name = request.name,
                age = request.age,
            )
        )
}

@SpringBootApplication
class UserJsonServerApplication

fun main(args: Array<String>) {
    runApplication<UserJsonServerApplication>(*args)
}
