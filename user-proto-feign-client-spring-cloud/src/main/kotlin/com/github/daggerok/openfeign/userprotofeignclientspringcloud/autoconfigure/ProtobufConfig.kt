package com.github.daggerok.openfeign.userprotofeignclientspringcloud.autoconfigure

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter

@Configuration
@ConditionalOnMissingClass
class ProtobufConfig {

    @Bean
    @ConditionalOnMissingBean
    fun protobufMessageConverter(): ProtobufHttpMessageConverter =
        ProtobufHttpMessageConverter()
}
