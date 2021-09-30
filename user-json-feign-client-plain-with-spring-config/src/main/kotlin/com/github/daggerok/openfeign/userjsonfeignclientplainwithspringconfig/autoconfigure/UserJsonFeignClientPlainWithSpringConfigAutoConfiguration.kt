package com.github.daggerok.openfeign.userjsonfeignclientplainwithspringconfig.autoconfigure

import com.github.daggerok.openfeign.userjsonfeignclientplainwithspringconfig.UserClient
import feign.Feign
import feign.Logger
import feign.codec.Decoder
import feign.codec.Encoder
import feign.slf4j.Slf4jLogger
import org.springframework.beans.factory.ObjectFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

private typealias Props = UserJsonFeignClientPlainWithSpringConfigProperties
private typealias Client = UserClient

@Configuration
@ConditionalOnMissingClass
@EnableConfigurationProperties(UserJsonFeignClientPlainWithSpringConfigProperties::class)
class UserJsonFeignClientPlainWithSpringConfigAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun userJsonFeignClientPlainWithSpringConfigEncoder(httpMessageConverters: ObjectFactory<HttpMessageConverters>): Encoder =
        SpringEncoder(httpMessageConverters)

    @Bean
    @ConditionalOnMissingBean
    fun userJsonFeignClientPlainWithSpringConfigDecoder(httpMessageConverters: ObjectFactory<HttpMessageConverters>): Decoder =
        ResponseEntityDecoder(SpringDecoder(httpMessageConverters))

    @Bean
    @ConditionalOnMissingBean
    fun userJsonFeignClientPlainWithSpringConfig(
        props: Props,
        userJsonFeignClientPlainWithSpringConfigEncoder: Encoder,
        userJsonFeignClientPlainWithSpringConfigDecoder: Decoder
    ): Client =
        Feign
            .builder()
            .logger(Slf4jLogger())
            .logLevel(Logger.Level.FULL)
            .encoder(userJsonFeignClientPlainWithSpringConfigEncoder)
            .decoder(userJsonFeignClientPlainWithSpringConfigDecoder)
            .target(Client::class.java, props.url)
}
