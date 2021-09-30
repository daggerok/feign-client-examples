package com.github.daggerok.openfeign.userprotospringwebserver

import com.github.daggerok.openfeign.userprotoapi.CreateUserCommand
import com.github.daggerok.openfeign.userprotoapi.UserDTO
import com.github.daggerok.openfeign.userprotoapi.UserDocument
import com.github.daggerok.openfeign.userprotoapi.UsersDocument
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
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter.PROTOBUF

@TestInstance(PER_CLASS)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("User proto spring server tests")
@DisplayNameGeneration(ReplaceUnderscores::class)
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class UserProtoServerApplicationTests @Autowired constructor(
    @Value("\${server.servlet.context-path:/}") val contextPath: String,
    val restTemplate: TestRestTemplate,
    @LocalServerPort val port: Int,
) {

    @Test
    fun `should create user`() {
        // given
        val request = HttpEntity(
            CreateUserCommand.newBuilder()
                .setUserDTO(
                    UserDTO.newBuilder()
                        .setName("Baby")
                        .setAge(1)
                )
                .build()
        )

        // when
        val response = restTemplate.exchange<UserDocument>(
            "http://127.0.0.1:$port/user-proto-spring-web-server/api/v1/create-user", POST, request
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
    fun `should get user document`() {
        // when
        val response = restTemplate.exchange<UsersDocument>(
            "http://127.0.0.1:${port}${contextPath}/api/v1/get-users/{age}", GET, null,
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
}
