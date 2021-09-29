package com.github.daggerok.openfeign.userjsonfeignclientplain

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.daggerok.openfeign.jsonuserapi.CreateUserCommand
import com.github.daggerok.openfeign.jsonuserapi.UserDTO
import com.github.daggerok.openfeign.jsonuserapi.UserDocument
import com.github.daggerok.openfeign.jsonuserapi.UsersDocument
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.containing
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
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
internal class UserJsonFeignClientPlainTestsApp

@TestInstance(PER_CLASS)
@AutoConfigureWireMock(port = 0)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayNameGeneration(ReplaceUnderscores::class)
@DisplayName("User json feign client plain tests")
@ContextConfiguration(classes = [UserJsonFeignClientPlainTestsApp::class])
class UserJsonFeignClientPlainTests @Autowired constructor(
    val userJsonFeignClientPlain: UserJsonFeignClientPlain,
    val objectMapper: ObjectMapper,
) {

    @Test
    fun `should create user`() {
        // given
        val createUserCommand = CreateUserCommand(
            name = "Maksimko",
            age = 38,
        )

        // and setup
        WireMock.stubFor(
            WireMock.post(urlPathEqualTo("/api/v1/create-user"))
                .withHeader("Content-Type", containing("application/json"))
                .withRequestBody(
                    equalToJson( // equalTo(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(..))
                        objectMapper.writeValueAsString(
                            createUserCommand
                        )
                    )
                )
                .willReturn(
                    WireMock
                        .aResponse()
                        .withStatus(HttpStatus.CREATED.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            objectMapper.writeValueAsString(
                                UserDocument(
                                    UserDTO(
                                        name = "Maksimko",
                                        age = 38
                                    )
                                )
                            )
                        )
                )
        )

        // when
        val document = userJsonFeignClientPlain.createUser(createUserCommand)

        // then
        assertThat(document.userDTO.name).isEqualTo("Maksimko")
        assertThat(document.userDTO.age).isEqualTo(38)
    }

    @Test
    fun `should get users by age 38`() {
        // setup
        WireMock.stubFor(
            WireMock.get(urlPathEqualTo("/api/v1/get-users/38"))
                .withHeader("Content-Type", containing("application/json"))
                .willReturn(
                    WireMock
                        .aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            objectMapper.writeValueAsString(
                                UsersDocument(
                                    listOf(
                                        UserDTO(
                                            name = "Maksimko",
                                            age = 38
                                        )
                                    )
                                )
                            )
                        )
                )
        )

        // when
        val document = userJsonFeignClientPlain.getUsersByAge(38)

        // then
        val (name, age) = document.users.first()
        assertThat(name).isEqualTo("Maksimko")
        assertThat(age).isEqualTo(38)
    }
}
