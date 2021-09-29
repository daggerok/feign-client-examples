package com.github.daggerok.openfeign.userjsonspringcloudfeignclient.autoconfigure

import com.github.daggerok.openfeign.userjsonspringcloudfeignclient.UserJsonSpringCloudFeignClient
import feign.Logger
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnMissingClass
@EnableFeignClients(clients = [UserJsonSpringCloudFeignClient::class])
@EnableConfigurationProperties(UserJsonSpringCloudFeignClientProperties::class)
class UserJsonSpringCloudFeignClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun feignLoggerLevel(): Logger.Level =
        Logger.Level.FULL;
}
