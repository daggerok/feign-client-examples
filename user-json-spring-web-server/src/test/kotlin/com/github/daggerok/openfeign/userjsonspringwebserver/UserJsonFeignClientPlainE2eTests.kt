package com.github.daggerok.openfeign.userjsonspringwebserver

import com.github.daggerok.openfeign.jsonuserapi.CreateUserCommand
import com.github.daggerok.openfeign.userjsonfeignclientplain.UserClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT

@TestInstance(PER_CLASS)
@DisplayNameGeneration(ReplaceUnderscores::class)
@DisplayName("User json feign client plain tests")
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest(
    webEnvironment = DEFINED_PORT,
    properties = [
        "server.port=8763",
        "user-client.host=127.0.0.1",
        "user-client.port=\${server.port}",
        "user-client.contextPath=\${server.servlet.context-path}",
    ],
)
class UserJsonFeignClientPlainE2eTests(@Autowired val userClient: UserClient) {

    @Test
    fun `should create user`() {
        // given
        val createUserCommand = CreateUserCommand(
            name = "Baby",
            age = 1,
        )

        // when
        val response = userClient.createUser(createUserCommand)

        // then
        val (name, age) = response.userDTO
        assertThat(name).isEqualTo("Baby")
        assertThat(age).isEqualTo(1)
    }

    @Test
    fun `should get user documents`() {
        // given (useless in our case, because of hard-code, but...)
        userClient.createUser(
            CreateUserCommand(
                name = "Maksimko",
                age = 38,
            )
        )

        // when
        val result = userClient.getUsersByAge(38)

        // then
        val (username, age) = result.users.first()
        assertThat(username).isEqualTo("Maksimko")
        assertThat(age).isEqualTo(38)
    }
}
