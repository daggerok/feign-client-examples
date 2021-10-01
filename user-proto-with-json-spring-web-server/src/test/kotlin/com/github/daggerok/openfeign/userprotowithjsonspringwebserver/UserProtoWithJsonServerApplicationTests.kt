package com.github.daggerok.openfeign.userprotowithjsonspringwebserver

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter.PROTOBUF

@TestInstance(PER_CLASS)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayNameGeneration(ReplaceUnderscores::class)
@DisplayName("User proto with json spring server tests")
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class UserProtoWithJsonServerApplicationTests @Autowired constructor(
    @Value("\${server.servlet.context-path:/}") val contextPath: String,
    val restTemplate: TestRestTemplate,
    @LocalServerPort val port: Int,
) {

    @Test
    fun `should create proto user`() {
        // given
        val request = HttpEntity(
            com.github.daggerok.openfeign.userprotoapi.CreateUserCommand.newBuilder()
                .setUserDTO(
                    com.github.daggerok.openfeign.userprotoapi.UserDTO.newBuilder()
                        .setName("Baby")
                        .setAge(1)
                )
                .build()
        )

        // when
        val response = restTemplate.exchange<com.github.daggerok.openfeign.userprotoapi.UserDocument>(
            "http://127.0.0.1:$port/user-proto-with-json-spring-web-server/api/v1/proto/create-user", POST, request
        )

        // then
        assertThat(response.statusCode).isEqualTo(CREATED)
        assertThat(response.headers.contentType).isEqualTo(PROTOBUF)
        assertThat(response.headers.contentType.toString()).isEqualTo(PROTOBUF.toString())

        // and
        val body = response.body ?: fail("body may not be null")
        assertThat(body.userDTO.name).isEqualTo("Baby")
        assertThat(body.userDTO.age).isEqualTo(1)
    }

    @Test
    fun `should get proto user documents`() {
        // when
        val response = restTemplate.exchange<com.github.daggerok.openfeign.userprotoapi.UsersDocument>(
            "http://127.0.0.1:${port}${contextPath}/api/v1/proto/get-users/{age}", GET, null,
            "38"
        )

        // then
        assertThat(response.statusCode).isEqualTo(OK)
        assertThat(response.headers.contentType).isEqualTo(PROTOBUF)
        assertThat(response.headers.contentType.toString()).contains("application/x-protobuf")

        // and
        val document = response.body ?: fail("body is null")
        val dto = document.usersList.first()
        assertThat(dto.name).isEqualTo("Maksimko")
        assertThat(dto.age).isEqualTo(38)
    }

    @Test
    fun `should create json user`() {
        // given
        val request = HttpEntity(
            com.github.daggerok.openfeign.jsonuserapi.CreateUserCommand(
                name = "Baby",
                age = 1,
            )
        )

        // when
        val response = restTemplate.exchange<com.github.daggerok.openfeign.jsonuserapi.UserDocument>(
            "http://127.0.0.1:$port/user-proto-with-json-spring-web-server/api/v1/json/create-user", POST, request
        )

        // then
        assertThat(response.statusCode).isEqualTo(CREATED)
        assertThat(response.headers.contentType).isEqualTo(MediaType.APPLICATION_JSON)
        assertThat(response.headers.contentType.toString()).isEqualTo(MediaType.APPLICATION_JSON_VALUE)

        // and
        val body = response.body ?: fail("body may not be null")
        val (name, age) = body.userDTO
        assertThat(name).isEqualTo("Baby")
        assertThat(age).isEqualTo(1)
    }

    @Test
    fun `should get json user documents`() {
        // when
        val response = restTemplate.exchange<com.github.daggerok.openfeign.jsonuserapi.UsersDocument>(
            "http://127.0.0.1:${port}${contextPath}/api/v1/json/get-users/{age}", GET, null,
            "38"
        )

        // then
        assertThat(response.statusCode).isEqualTo(OK)
        assertThat(response.headers.contentType).isEqualTo(MediaType.APPLICATION_JSON)
        assertThat(response.headers.contentType.toString()).isEqualTo(MediaType.APPLICATION_JSON_VALUE)

        // and
        val document = response.body ?: fail("body is null")
        val (username, age) = document.users.first()
        assertThat(username).isEqualTo("Maksimko")
        assertThat(age).isEqualTo(38)
    }
}
