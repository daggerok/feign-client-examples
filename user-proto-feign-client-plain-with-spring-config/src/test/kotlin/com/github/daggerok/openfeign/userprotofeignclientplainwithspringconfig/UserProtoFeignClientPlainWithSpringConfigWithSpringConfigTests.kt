package com.github.daggerok.openfeign.userprotofeignclientplainwithspringconfig

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
internal class UserProtoFeignClientPlainWithSpringConfigTestsApp

@TestInstance(PER_CLASS)
@AutoConfigureWireMock(port = 0)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayNameGeneration(ReplaceUnderscores::class)
@DisplayName("User proto feign client plain with spring config tests")
@ContextConfiguration(classes = [UserProtoFeignClientPlainWithSpringConfigTestsApp::class])
class UserProtoFeignClientPlainWithSpringConfigWithSpringConfigTests(
    @Autowired val userProtoFeignClientPlainWithSpringConfig: UserProtoFeignClientPlainWithSpringConfig,
) {

    @Test
    fun `should create user`() {
        // given
        val createUserCommand = CreateUserCommand.newBuilder()
            .setUserDTO(
                UserDTO.newBuilder()
                    .setName("Maksimko")
                    .setAge(38)
            )
            .build()

        // and setup
        WireMock.stubFor(
            WireMock
                .post(urlPathEqualTo("/api/v1/create-user"))
                .withHeader("Content-Type", containing("application/x-protobuf"))
                .withRequestBody(
                    binaryEqualTo( // equalToJson( // equalTo(...
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
                                        .setName("Maksimko")
                                        .setAge(38)
                                )
                                .build()
                                .toByteArray()
                        )
                )
        )

        // when
        val document = userProtoFeignClientPlainWithSpringConfig.createUser(createUserCommand)

        // then
        assertThat(document.userDTO.name).isEqualTo("Maksimko")
        assertThat(document.userDTO.age).isEqualTo(38)
    }

    @Test
    fun `should get users by age 38`() {
        // setup
        WireMock.stubFor(
            WireMock
                .get(urlPathEqualTo("/api/v1/get-users/38"))
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
                                        .setName("Maksimko")
                                        .setAge(38)
                                )
                                .build()
                                .toByteArray()
                        )
                )
        )

        // when
        val document = userProtoFeignClientPlainWithSpringConfig.getUsersByAge(38)

        // then
        val dto = document.usersList.first()
        assertThat(dto.name).isEqualTo("Maksimko")
        assertThat(dto.age).isEqualTo(38)
    }
}
