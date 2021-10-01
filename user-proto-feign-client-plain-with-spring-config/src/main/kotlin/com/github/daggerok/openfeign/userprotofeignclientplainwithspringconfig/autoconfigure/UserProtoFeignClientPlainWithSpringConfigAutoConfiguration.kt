package com.github.daggerok.openfeign.userprotofeignclientplainwithspringconfig.autoconfigure

import com.github.daggerok.openfeign.userprotofeignclientplainwithspringconfig.UserClient
import feign.Feign
import feign.Logger
import feign.codec.Decoder
import feign.codec.Encoder
import feign.slf4j.Slf4jLogger
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ConditionalOnMissingClass
@Import(UserProtoFeignClientPlainWithSpringConfigProtobufConfig::class)
@EnableConfigurationProperties(UserProtoFeignClientPlainWithSpringConfigProperties::class)
class UserProtoFeignClientPlainWithSpringConfigAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun userProtoFeignClientPlainWithSpringConfig(
        protobufEncoder: Encoder,
        protobufDecoder: Decoder,
        props: UserProtoFeignClientPlainWithSpringConfigProperties,
    ): UserClient =
        Feign
            .builder()
            .logger(Slf4jLogger())
            .logLevel(Logger.Level.FULL)
            .encoder(protobufEncoder)
            .decoder(protobufDecoder)
            .target(UserClient::class.java, props.url)
}
