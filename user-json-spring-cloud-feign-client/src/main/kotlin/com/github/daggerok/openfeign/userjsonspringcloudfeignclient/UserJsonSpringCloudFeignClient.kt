package com.github.daggerok.openfeign.userjsonspringcloudfeignclient

import com.github.daggerok.openfeign.jsonuserapi.CreateUserCommand
import com.github.daggerok.openfeign.jsonuserapi.UserDocument
import com.github.daggerok.openfeign.jsonuserapi.UsersDocument
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "user-json-spring-cloud-feign-client",
    url = "\${user-json-spring-cloud-feign-client.url}",
)
interface UserJsonSpringCloudFeignClient {

    @PostMapping("/api/v1/create-user")
    fun createUser(@RequestBody request: CreateUserCommand): UserDocument

    @GetMapping(
        path = ["/api/v1/get-users/{age}"],
        consumes = [MimeTypeUtils.APPLICATION_JSON_VALUE],
    )
    fun getUsersByAge(@PathVariable("age") age: Int): UsersDocument
}
