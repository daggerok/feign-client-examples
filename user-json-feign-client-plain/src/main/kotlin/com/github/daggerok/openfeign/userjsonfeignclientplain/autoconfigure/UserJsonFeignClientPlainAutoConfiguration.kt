package com.github.daggerok.openfeign.userjsonfeignclientplain.autoconfigure

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.github.daggerok.openfeign.userjsonfeignclientplain.UserClient
import feign.Feign
import feign.Logger
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import feign.slf4j.Slf4jLogger
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnMissingClass
@EnableConfigurationProperties(UserJsonFeignClientPlainProperties::class)
class UserJsonFeignClientPlainAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun userJsonFeignClientPlain(props: UserJsonFeignClientPlainProperties): UserClient =
        Feign
            .builder()
            .logger(Slf4jLogger())
            .logLevel(Logger.Level.FULL)
            .encoder(JacksonEncoder(listOf(JavaTimeModule())))
            .decoder(JacksonDecoder(listOf(JavaTimeModule())))
            .target(UserClient::class.java, props.url)
}
