package com.github.daggerok.openfeign.userjsonspringwebserver

import com.github.daggerok.openfeign.jsonuserapi.CreateUserCommand
import com.github.daggerok.openfeign.jsonuserapi.UserDocument
import com.github.daggerok.openfeign.jsonuserapi.UsersDocument
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
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@TestInstance(PER_CLASS)
@DisplayName("User json spring server tests")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayNameGeneration(ReplaceUnderscores::class)
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class JsonServerApplicationTests @Autowired constructor(
    @Value("\${server.servlet.context-path:/}") val contextPath: String,
    val restTemplate: TestRestTemplate,
    @LocalServerPort val port: Int,
) {

    @Test
    fun `should create user`() {
        // given
        val request = HttpEntity(
            CreateUserCommand(
                name = "Baby",
                age = 1,
            )
        )

        // when
        val response = restTemplate.exchange<UserDocument>(
            "http://127.0.0.1:$port/user-json-server/api/v1/create-user", POST, request
        )

        // then
        assertThat(response.statusCode).isEqualTo(CREATED)
        assertThat(response.headers.contentType).isEqualTo(APPLICATION_JSON)
        assertThat(response.headers.contentType.toString()).isEqualTo(APPLICATION_JSON_VALUE)

        // and
        val body = response.body ?: fail("body may not be null")
        val (name, age) = body.userDTO
        assertThat(name).isEqualTo("Baby")
        assertThat(age).isEqualTo(1)
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
        assertThat(response.headers.contentType).isEqualTo(APPLICATION_JSON)
        assertThat(response.headers.contentType.toString()).isEqualTo(APPLICATION_JSON_VALUE)

        // and
        val document = response.body ?: fail("body is null")
        val (username, age) = document.users.first()
        assertThat(username).isEqualTo("Maksimko")
        assertThat(age).isEqualTo(38)
    }
}
