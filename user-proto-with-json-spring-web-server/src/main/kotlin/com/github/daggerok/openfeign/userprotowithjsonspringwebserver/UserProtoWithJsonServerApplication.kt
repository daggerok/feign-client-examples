package com.github.daggerok.openfeign.userprotowithjsonspringwebserver

import java.util.Optional
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

private typealias JsonCreateUserCommand = com.github.daggerok.openfeign.jsonuserapi.CreateUserCommand
private typealias JsonUserDocument = com.github.daggerok.openfeign.jsonuserapi.UserDocument
private typealias JsonUsersDocument = com.github.daggerok.openfeign.jsonuserapi.UsersDocument
private typealias JsonUserDTO = com.github.daggerok.openfeign.jsonuserapi.UserDTO

@RestController
class UserJsonResource {

    @GetMapping("/api/v1/json/get-users/{age}")
    fun getUsers(@PathVariable("age") age: Optional<Int>): JsonUsersDocument =
        JsonUsersDocument(
            listOf(
                JsonUserDTO(
                    name = "Maksimko",
                    age = age.orElse(38),
                )
            )
        )

    @ResponseStatus(CREATED)
    @PostMapping("/api/v1/json/create-user")
    fun createUser(@RequestBody request: JsonCreateUserCommand): JsonUserDocument =
        JsonUserDocument(
            com.github.daggerok.openfeign.jsonuserapi.UserDTO(
                name = request.name,
                age = request.age,
            )
        )
}

private typealias ProtoCreateUserCommand = com.github.daggerok.openfeign.userprotoapi.CreateUserCommand
private typealias ProtoUsersDocument = com.github.daggerok.openfeign.userprotoapi.UsersDocument
private typealias ProtoUserDocument = com.github.daggerok.openfeign.userprotoapi.UserDocument
private typealias ProtoUserDTO = com.github.daggerok.openfeign.userprotoapi.UserDTO

@RestController
class UserProtoResource {

    @GetMapping("/api/v1/proto/get-users/{age}")
    fun getUsers(@PathVariable("age") maybeAge: Optional<Int>): ProtoUsersDocument =
        ProtoUsersDocument.newBuilder()
            .addUsers(
                ProtoUserDTO.newBuilder()
                    .setName("Maksimko")
                    .setAge(maybeAge.orElse(38))
            )
            .build()

    @ResponseStatus(CREATED)
    @PostMapping("/api/v1/proto/create-user")
    fun createUser(@RequestBody request: ProtoCreateUserCommand): ProtoUserDocument =
        ProtoUserDocument.newBuilder()
            .setUserDTO(
                ProtoUserDTO.newBuilder()
                    .setName(request.userDTO.name)
                    .setAge(request.userDTO.age)
            )
            .build()
}

@Configuration
class ProtobufConfig {

    @Bean
    fun protobufMessageConverter(): ProtobufHttpMessageConverter =
        ProtobufJsonFormatHttpMessageConverter() // protobuf-java-util
}

@SpringBootApplication
class UserProtoWithJsonServerApplication

fun main(args: Array<String>) {
    runApplication<UserProtoWithJsonServerApplication>(*args)
}
