package com.github.daggerok.openfeign.userspringwebserver

import com.github.daggerok.openfeign.userprotoapi.CreateUserCommand
import com.github.daggerok.openfeign.userprotoapi.UserDTO
import com.github.daggerok.openfeign.userspringcloudopenfeignclient.UserClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
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
@DisplayName("User spring-cloud openfeign tests")
@DisplayNameGeneration(ReplaceUnderscores::class)
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest(
    webEnvironment = DEFINED_PORT,
    properties = [
        "server.port=8765",
        "user-client.url=http://127.0.0.1:\${server.port}\${server.servlet.context-path}",
    ],
)
class UserOpenFeignClientTests @Autowired constructor(
    val usersRepository: UsersRepository,
    val userClient: UserClient,
) {

    @BeforeEach
    fun setUp() {
        usersRepository.deleteAll()
    }

    @Test
    fun `should create user`() {
        // when
        val result = userClient.createUser(
            CreateUserCommand.newBuilder().run {
                userDTO = UserDTO.newBuilder().run {
                    name = "Maksim"
                    age = 38
                    build()
                }
                build()
            }
        )

        // then
        assertThat(result.userDTO.age).isEqualTo(38)
        assertThat(result.userDTO.name).isEqualTo("Maksim")
    }

    @Test
    fun `should get all users`() {
        // given
        mapOf("1st" to 1, "2nd" to 2).forEach { name, age ->
            usersRepository.save(
                UserDTO.newBuilder().let {
                    it.name = name
                    it.age = age
                    it.build()
                }
            )
        }

        // when
        val result = userClient.getAllUsers()

        // then
        assertThat(result.usersList).hasSize(2)

        // and
        assertThat(result.usersList.first().age).isEqualTo(1)
        assertThat(result.usersList.first().name).isEqualTo("1st")

        // and
        assertThat(result.usersList.last().age).isEqualTo(2)
        assertThat(result.usersList.last().name).isEqualTo("2nd")
    }

    @Test
    fun `should search all 38 years old users`() {
        // given
        mapOf("37th" to 37, "38th" to 38, "39th" to 39).forEach { (name, age) ->
            usersRepository.save(
                UserDTO.newBuilder().let {
                    it.name = name
                    it.age = age
                    it.build()
                }
            )
        }

        // when
        val result = userClient.searchUsersByAge(38)

        // then
        assertThat(result.usersList).hasSize(1)

        // and
        assertThat(result.usersList.first().age).isEqualTo(38)
        assertThat(result.usersList.first().name).isEqualTo("38th")
    }
}
