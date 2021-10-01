package com.github.daggerok.openfeign.userjsonfeignclientspringcloud.autoconfigure

import com.github.daggerok.openfeign.userjsonfeignclientspringcloud.UserClient
import feign.Logger
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnMissingClass
@EnableFeignClients(clients = [UserClient::class])
class UserJsonFeignClientSpringCloudAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun feignLoggerLevel(): Logger.Level =
        Logger.Level.FULL;
}
