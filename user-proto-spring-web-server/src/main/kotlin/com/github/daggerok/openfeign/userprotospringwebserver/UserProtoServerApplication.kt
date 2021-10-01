package com.github.daggerok.openfeign.userprotospringwebserver

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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class UserProtoResource {

    @GetMapping("/api/v1/get-users/{age}")
    fun getUsers(@PathVariable("age") maybeAge: Optional<Int>): UsersDocument =
        UsersDocument.newBuilder()
            .addUsers(
                UserDTO.newBuilder()
                    .setName("Maksimko")
                    .setAge(maybeAge.orElse(38))
            )
            .build()

    @ResponseStatus(CREATED)
    @PostMapping("/api/v1/create-user")
    fun createUser(@RequestBody request: CreateUserCommand): UserDocument =
        UserDocument.newBuilder()
            .setUserDTO(
                UserDTO.newBuilder()
                    .setName(request.userDTO.name)
                    .setAge(request.userDTO.age)
            )
            .build()
}

@Configuration
class ProtobufConfig {

    @Bean
    fun protobufMessageConverter(): ProtobufHttpMessageConverter =
        ProtobufHttpMessageConverter()
}

@SpringBootApplication
class UserProtoServerApplication

fun main(args: Array<String>) {
    runApplication<UserProtoServerApplication>(*args)
}
