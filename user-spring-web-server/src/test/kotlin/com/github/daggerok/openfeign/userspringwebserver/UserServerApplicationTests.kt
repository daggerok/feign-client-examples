package com.github.daggerok.openfeign.userspringwebserver

import com.github.daggerok.openfeign.userprotoapi.UserDTO
import com.github.daggerok.openfeign.userprotoapi.UserDocument
import com.github.daggerok.openfeign.userprotoapi.UsersDocument
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
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
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter.PROTOBUF

@TestInstance(PER_CLASS)
@DisplayName("User spring server tests")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayNameGeneration(ReplaceUnderscores::class)
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class UserServerApplicationTests @Autowired constructor(
    @Value("\${server.servlet.context-path:/}") val contextPath: String,
    val usersRepository: UsersRepository,
    val restTemplate: TestRestTemplate,
    @LocalServerPort val port: Int,
) {

    @BeforeEach
    fun setUp() {
        usersRepository.deleteAll()
    }

    @Test
    fun `should create json user`() {
        // given
        val request = HttpEntity(
            mapOf(
                "userDTO" to mapOf(
                    "name" to "Baby",
                    "age" to 1,
                )
            ),
            HttpHeaders().apply {
                accept = listOf(MediaType.APPLICATION_JSON)
                contentType = MediaType.APPLICATION_JSON
            }
        )

        // when
        val response = restTemplate.exchange<UserDocument>(
            "http://127.0.0.1:$port/user-spring-web-server/api/v1", POST, request
        )

        // then
        assertThat(response.statusCode).isEqualTo(CREATED)
        assertThat(response.headers.contentType.toString()).contains(PROTOBUF.type)
        assertThat(response.headers.contentType.toString()).contains(MediaType.APPLICATION_JSON.type)

        // and
        val body = response.body ?: fail("body may not be null")
        assertThat(body.userDTO.name).isEqualTo("Baby")
        assertThat(body.userDTO.age).isEqualTo(1)
    }

    @Test
    fun `should create protobuf user`() {
        // given
        val request = HttpEntity(
            mapOf(
                "userDTO" to mapOf(
                    "name" to "Maksim",
                    "age" to 38,
                )
            ),
            HttpHeaders().apply {
                accept = listOf(PROTOBUF)
                contentType = MediaType.APPLICATION_JSON
            }
        )

        // when
        val response = restTemplate.exchange<String>(
            "http://127.0.0.1:$port$contextPath/api/v1", POST, request
        )

        // then
        assertThat(response.statusCode).isEqualTo(CREATED)
        assertThat(response.headers.contentType.toString()).contains(PROTOBUF.type)

        // and
        val string = response.body ?: fail("body may not be null")
        assertThat(string).contains("Maksim")
    }

    @Test
    fun `should search 38 years old users json document`() {
        // given
        usersRepository.save(
            UserDTO.newBuilder().run {
                name = "Maksimko"
                age = 38
                build()
            }
        )

        // and
        val headers = HttpHeaders().apply {
            accept = listOf(MediaType.APPLICATION_JSON)
        }

        // when
        val response = restTemplate.exchange<UsersDocument>(
            "http://127.0.0.1:${port}${contextPath}/api/v1/{age}",
            GET, HttpEntity(null, headers),
            "38"
        )

        // then
        assertThat(response.statusCode).isEqualTo(OK)
        assertThat(response.headers.contentType.toString()).contains("application/json")

        // and
        val document = response.body ?: fail("body is null")
        assertThat(document.usersList).hasSize(1)

        // and
        val dto = document.usersList.first()
        assertThat(dto.name).isEqualTo("Maksimko")
        assertThat(dto.age).isEqualTo(38)
    }

    @Test
    fun `should search 38 years old users protobuf document`() {
        // given
        usersRepository.save(
            UserDTO.newBuilder().run {
                name = "Maksim"
                age = 33
                build()
            }
        )

        // and
        val headers = HttpHeaders().apply {
            accept = listOf(PROTOBUF)
        }

        // when
        val response = restTemplate.exchange<String>(
            "http://127.0.0.1:${port}${contextPath}/api/v1/{age}",
            GET, HttpEntity(null, headers),
            "33"
        )

        // then
        assertThat(response.statusCode).isEqualTo(OK)
        assertThat(response.headers.contentType.toString()).contains("application/x-protobuf")

        // and
        val string = response.body ?: fail("body is null")
        assertThat(string).contains("Maksim")
    }

    @Test
    fun `should get users json document`() {
        // given
        mapOf(1 to "First", 2 to "Second").forEach { (age, name) ->
            usersRepository.save(
                UserDTO.newBuilder().let {
                    it.name = name
                    it.age = age
                    it.build()
                }
            )
        }

        // and
        val requestEntity = HttpEntity(
            null,
            HttpHeaders().apply {
                accept = listOf(MediaType.APPLICATION_JSON)
            }
        )

        // when
        val response = restTemplate.exchange<UsersDocument>(
            "http://127.0.0.1:$port/user-spring-web-server/api/v1", GET, requestEntity
        )

        // then
        assertThat(response.statusCode).isEqualTo(OK)
        assertThat(response.headers.contentType).isEqualTo(MediaType.APPLICATION_JSON)

        // and
        val document = response.body ?: fail("body may not be null")
        assertThat(document.usersList).hasSize(2)

        // and
        val firstDTO = document.usersList.first()
        assertThat(firstDTO.age).isEqualTo(1)
        assertThat(firstDTO.name).isEqualTo("First")

        // and
        val secondDTO = document.usersList.last()
        assertThat(secondDTO.age).isEqualTo(2)
        assertThat(secondDTO.name).isEqualTo("Second")
    }

    @Test
    fun `should get users protobuf document`() {
        // given
        mapOf(1 to "First", 2 to "Second").forEach { (age, name) ->
            usersRepository.save(
                UserDTO.newBuilder().let {
                    it.name = name
                    it.age = age
                    it.build()
                }
            )
        }

        // and
        val requestEntity = HttpEntity(
            null,
            HttpHeaders().apply {
                accept = listOf(PROTOBUF)
            }
        )

        // when
        val response = restTemplate.exchange<String>(
            "http://127.0.0.1:$port/user-spring-web-server/api/v1", GET, requestEntity
        )

        // then
        assertThat(response.statusCode).isEqualTo(OK)
        assertThat(response.headers.contentType?.type).contains(PROTOBUF.type)

        // and
        val string = response.body ?: fail("body may not be null")
        assertThat(string).contains("First", "Second")
    }
}
