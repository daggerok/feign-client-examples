package com.github.daggerok.openfeign.userprotofeignclientplainwithspringconfig

import com.github.daggerok.openfeign.userprotoapi.CreateUserCommand
import com.github.daggerok.openfeign.userprotoapi.UserDocument
import com.github.daggerok.openfeign.userprotoapi.UsersDocument
import feign.Headers
import feign.Param
import feign.RequestLine

@Headers("Content-Type: application/x-protobuf")
interface UserClient {

    @RequestLine("POST /api/v1/create-user")
    fun createUser(request: CreateUserCommand): UserDocument

    @RequestLine("GET /api/v1/get-users/{age}")
    fun getUsersByAge(@Param("age") age: Int): UsersDocument
}
