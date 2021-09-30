package com.github.daggerok.openfeign.userprotofeignclientspringcloud.autoconfigure

import com.github.daggerok.openfeign.userprotofeignclientspringcloud.UserProtoFeignClientSpringCloud
import feign.Logger
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ConditionalOnMissingClass
@Import(ProtobufConfig::class)
@EnableFeignClients(clients = [UserProtoFeignClientSpringCloud::class])
@EnableConfigurationProperties(UserProtoFeignClientSpringCloudProperties::class)
class UserProtoFeignClientSpringCloudAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun feignLoggerLevel(): Logger.Level =
        Logger.Level.FULL;
}
