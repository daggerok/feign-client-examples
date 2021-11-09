package com.github.daggerok.openfeign.userfeignclientspringcloud

import com.github.daggerok.openfeign.userprotoapi.CreateUserCommand
import com.github.daggerok.openfeign.userprotoapi.UserDTO
import com.github.daggerok.openfeign.userprotoapi.UserDocument
import com.github.daggerok.openfeign.userprotoapi.UsersDocument
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.binaryEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.containing
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration

@SpringBootApplication
internal class UserSpringCloudOpenFeignClientTestsApp

@TestInstance(PER_CLASS)
@AutoConfigureWireMock(port = 0)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayNameGeneration(ReplaceUnderscores::class)
@DisplayName("User spring-cloud openfeign client tests")
@ContextConfiguration(classes = [UserSpringCloudOpenFeignClientTestsApp::class])
class UserClientTests(@Autowired val userClient: UserClient) {

    @Test
    fun `should create user`() {
        // given
        val createUserCommand = CreateUserCommand.newBuilder()
            .setUserDTO(
                UserDTO.newBuilder()
                    .setName("Maksim")
                    .setAge(38)
            )
            .build()

        // and setup
        WireMock.stubFor(
            WireMock.post(urlPathEqualTo("/api/v1"))
                .withHeader("Content-Type", containing("application/x-protobuf"))
                .withRequestBody(
                    binaryEqualTo( // equalToJson( // equalTo(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(..))
                        createUserCommand.toByteArray()
                    )
                )
                .willReturn(
                    WireMock
                        .aResponse()
                        .withStatus(HttpStatus.CREATED.value())
                        .withHeader("Content-Type", "application/x-protobuf")
                        .withBody(
                            UserDocument.newBuilder()
                                .setUserDTO(
                                    UserDTO.newBuilder()
                                        .setName("Maksim")
                                        .setAge(38)
                                )
                                .build()
                                .toByteArray()
                        )
                )
        )

        // when
        val document = userClient.createUser(createUserCommand)

        // then
        assertThat(document.userDTO.name).isEqualTo("Maksim")
        assertThat(document.userDTO.age).isEqualTo(38)
    }

    @Test
    fun `should get all users`() {
        // setup
        WireMock.stubFor(
            WireMock.get(urlPathEqualTo("/api/v1"))
                .withHeader("Content-Type", containing("application/x-protobuf"))
                .willReturn(
                    WireMock
                        .aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/x-protobuf")
                        .withBody(
                            UsersDocument.newBuilder()
                                .addUsers(
                                    UserDTO.newBuilder()
                                        .setName("Maksim")
                                        .setAge(38)
                                )
                                .addUsers(
                                    UserDTO.newBuilder()
                                        .setName("Maksimko")
                                        .setAge(2)
                                )
                                .build()
                                .toByteArray()
                        )
                )
        )

        // when
        val document = userClient.getAllUsers()
        assertThat(document.usersList).hasSize(2)

        // then
        val first = document.usersList.first()
        assertThat(first.name).isEqualTo("Maksim")
        assertThat(first.age).isEqualTo(38)

        // and
        val second = document.usersList.last()
        assertThat(second.name).isEqualTo("Maksimko")
        assertThat(second.age).isEqualTo(2)
    }

    @Test
    fun `should search users by age 38`() {
        // setup
        WireMock.stubFor(
            WireMock.get(urlPathEqualTo("/api/v1/38"))
                .withHeader("Content-Type", containing("application/x-protobuf"))
                .willReturn(
                    WireMock
                        .aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/x-protobuf")
                        .withBody(
                            UsersDocument.newBuilder()
                                .addUsers(
                                    UserDTO.newBuilder()
                                        .setName("Maksim")
                                        .setAge(38)
                                )
                                .build()
                                .toByteArray()
                        )
                )
        )

        // when
        val document = userClient.searchUsersByAge(38)

        // then
        val dto = document.usersList.first()
        assertThat(dto.name).isEqualTo("Maksim")
        assertThat(dto.age).isEqualTo(38)
    }
}
