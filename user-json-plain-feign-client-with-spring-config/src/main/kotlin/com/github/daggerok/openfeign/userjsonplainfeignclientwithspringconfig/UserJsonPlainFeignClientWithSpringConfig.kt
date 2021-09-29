package com.github.daggerok.openfeign.userjsonplainfeignclientwithspringconfig

import com.github.daggerok.openfeign.jsonuserapi.CreateUserCommand
import com.github.daggerok.openfeign.jsonuserapi.UserDocument
import com.github.daggerok.openfeign.jsonuserapi.UsersDocument
import feign.Headers
import feign.Param
import feign.RequestLine

@Headers("Content-Type: application/json")
interface UserJsonPlainFeignClientWithSpringConfig {

    @RequestLine("POST /api/v1/create-user")
    fun createUser(request: CreateUserCommand): UserDocument

    @RequestLine("GET /api/v1/get-users/{age}")
    fun getUsersByAge(@Param("age") age: Int): UsersDocument
}
