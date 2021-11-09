package com.github.daggerok.openfeign.userfeignclientspringcloud.autoconfigure

import com.github.daggerok.openfeign.userfeignclientspringcloud.UserClient
import feign.Logger
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ConditionalOnMissingClass
@EnableFeignClients(clients = [UserClient::class])
@Import(UserSpringCloudOpenFeignClientProtobufConfig::class)
class UserSpringCloudOpenFeignClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun feignLoggerLevel(): Logger.Level =
        Logger.Level.FULL;
}
