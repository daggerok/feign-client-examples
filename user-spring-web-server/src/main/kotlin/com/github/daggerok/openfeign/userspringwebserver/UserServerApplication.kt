package com.github.daggerok.openfeign.userspringwebserver

import com.github.daggerok.openfeign.userprotoapi.CreateUserCommand
import com.github.daggerok.openfeign.userprotoapi.UserDTO
import com.github.daggerok.openfeign.userprotoapi.UserDocument
import com.github.daggerok.openfeign.userprotoapi.UsersDocument
import java.util.Optional
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class UserServerApplication

fun main(args: Array<String>) {
    runApplication<UserServerApplication>(*args)
}

@Configuration
class ProtobufConfig {

    @Bean
    fun protobufMessageConverter(): ProtobufHttpMessageConverter =
        ProtobufHttpMessageConverter()
}

@Repository
class UsersRepository(private val db: MutableList<UserDTO> = mutableListOf()) {

    fun save(userDTO: UserDTO): UserDTO =
        Optional
            .ofNullable(
                findByAgeAndName(userDTO.age, userDTO.name)
                    .firstOrNull()
            )
            .orElseGet {
                db += userDTO
                db.last()
            }

    fun findAll() = db.toList()

    fun findByAge(age: Int) =
        db.filter { it.age == age }

    fun findByAgeAndName(age: Int, name: String) =
        findByAge(age).filter { it.name == name }

    fun deleteAll() = db.clear()
}

@RestController
class UserResource(private val usersRepository: UsersRepository) {

    @GetMapping("/api/v1")
    fun getAllUsers(): UsersDocument =
        UsersDocument.newBuilder()
            .addAllUsers(usersRepository.findAll())
            .build()

    @GetMapping("/api/v1/{age}")
    fun searchUsersByAge(@PathVariable("age") maybeAge: Optional<Int>): UsersDocument =
        UsersDocument.newBuilder()
            .addAllUsers(
                usersRepository.findByAge(
                    maybeAge.orElse(38)
                )
            )
            .build()

    @PostMapping(
        path = ["/api/v1"],
        consumes = ["application/x-protobuf", "application/json"],
    )
    @ResponseStatus(CREATED)
    fun createUser(@RequestBody request: CreateUserCommand): UserDocument =
        UserDocument.newBuilder()
            .setUserDTO(
                usersRepository.save(request.userDTO)
            )
            .build()
}
