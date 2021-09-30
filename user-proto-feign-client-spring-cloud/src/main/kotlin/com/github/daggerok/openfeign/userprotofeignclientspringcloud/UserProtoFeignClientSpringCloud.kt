package com.github.daggerok.openfeign.userprotofeignclientspringcloud

import com.github.daggerok.openfeign.userprotoapi.CreateUserCommand
import com.github.daggerok.openfeign.userprotoapi.UserDocument
import com.github.daggerok.openfeign.userprotoapi.UsersDocument
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "user-proto-feign-client-spring-cloud",
    url = "\${user-proto-feign-client-spring-cloud.url}",
)
interface UserProtoFeignClientSpringCloud {

    @PostMapping(
        path = ["/api/v1/create-user"],
        consumes = ["application/x-protobuf"],
    )
    fun createUser(@RequestBody request: CreateUserCommand): UserDocument

    @GetMapping(
        path = ["/api/v1/get-users/{age}"],
        consumes = ["application/x-protobuf"],
    )
    fun getUsersByAge(@PathVariable("age") age: Int): UsersDocument
}
