package com.github.daggerok.openfeign.userprotofeignclientspringcloud.autoconfigure

import com.github.daggerok.openfeign.userprotofeignclientspringcloud.UserClient
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
@Import(UserProtoFeignClientSpringCloudProtobufConfig::class)
class UserProtoFeignClientSpringCloudAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun feignLoggerLevel(): Logger.Level =
        Logger.Level.FULL;
}
