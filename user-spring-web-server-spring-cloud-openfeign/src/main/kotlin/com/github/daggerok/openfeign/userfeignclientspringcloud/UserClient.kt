package com.github.daggerok.openfeign.userfeignclientspringcloud

import com.github.daggerok.openfeign.userprotoapi.CreateUserCommand
import com.github.daggerok.openfeign.userprotoapi.UserDocument
import com.github.daggerok.openfeign.userprotoapi.UsersDocument
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "user-client",
    url = "\${user-client.url}",
)
interface UserClient {

    @PostMapping(
        path = ["/api/v1"],
        consumes = ["application/x-protobuf"],
    )
    fun createUser(@RequestBody request: CreateUserCommand): UserDocument

    @GetMapping(
        path = ["/api/v1"],
        consumes = ["application/x-protobuf"],
    )
    fun getAllUsers(): UsersDocument

    @GetMapping(
        path = ["/api/v1/{age}"],
        consumes = ["application/x-protobuf"],
    )
    fun searchUsersByAge(@PathVariable("age") age: Int): UsersDocument
}
