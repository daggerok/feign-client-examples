package com.github.daggerok.openfeign.userjsonplainfeignclient.autoconfigure

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.github.daggerok.openfeign.userjsonplainfeignclient.UserJsonPlainFeignClient
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
@EnableConfigurationProperties(UserJsonPlainFeignClientProperties::class)
class UserJsonPlainFeignClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun userJsonPlainFeignClient(props: UserJsonPlainFeignClientProperties): UserJsonPlainFeignClient =
        Feign
            .builder()
            .logger(Slf4jLogger())
            .logLevel(Logger.Level.FULL)
            .encoder(JacksonEncoder(listOf(JavaTimeModule())))
            .decoder(JacksonDecoder(listOf(JavaTimeModule())))
            .target(UserJsonPlainFeignClient::class.java, props.url)
}
