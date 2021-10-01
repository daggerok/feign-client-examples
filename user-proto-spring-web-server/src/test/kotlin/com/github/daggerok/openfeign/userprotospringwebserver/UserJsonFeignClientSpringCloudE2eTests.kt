package com.github.daggerok.openfeign.userprotospringwebserver

import com.github.daggerok.openfeign.userprotoapi.CreateUserCommand
import com.github.daggerok.openfeign.userprotoapi.UserDTO
import com.github.daggerok.openfeign.userprotofeignclientspringcloud.UserClient
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
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@DisplayName("User proto feign client plain with spring config tests")
@SpringBootTest(
    webEnvironment = DEFINED_PORT,
    properties = [
        "server.port=8765",
        "user-client.host=127.0.0.1",
        "user-client.port=\${server.port}",
        "user-client.context-path=\${server.servlet.context-path}",
        "user-client.url=\${user-client.host}:\${user-client.port}\${user-client.context-path}",
    ],
)
class UserJsonFeignClientSpringCloudE2eTests(@Autowired val userClient: UserClient) {

    @Test
    fun `should create user`() {
        // given
        val createUserCommand =
            CreateUserCommand.newBuilder()
                .setUserDTO(
                    UserDTO.newBuilder()
                        .setName("Baby")
                        .setAge(1)
                )
                .build()

        // when
        val result = userClient.createUser(createUserCommand)

        // then
        val dto = result.userDTO
        assertThat(dto.name).isEqualTo("Baby")
        assertThat(dto.age).isEqualTo(1)
    }

    @Test
    fun `should get user document`() {
        // // given
        // userClient.createUser(
        //     CreateUserCommand.newBuilder()
        //         .setUserDTO(
        //             UserDTO.newBuilder()
        //                 .setName("Maksimko")
        //                 .setAge(38)
        //         )
        //         .build()
        // )

        // when
        val document = userClient.getUsersByAge(38)

        // then
        val dto = document.usersList.first()
        assertThat(dto.name).isEqualTo("Maksimko")
        assertThat(dto.age).isEqualTo(38)
    }
}
