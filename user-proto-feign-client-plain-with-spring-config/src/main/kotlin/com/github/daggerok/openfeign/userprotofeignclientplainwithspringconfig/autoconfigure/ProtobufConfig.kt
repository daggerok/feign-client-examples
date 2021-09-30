package com.github.daggerok.openfeign.userprotofeignclientplainwithspringconfig.autoconfigure

import feign.codec.Decoder
import feign.codec.Encoder
import org.springframework.beans.factory.ObjectFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter

@Configuration
@ConditionalOnMissingClass
class ProtobufConfig {

    @Bean
    @ConditionalOnMissingBean
    fun protobufMessageConverter(): ProtobufHttpMessageConverter =
        // ProtobufJsonFormatHttpMessageConverter(); // protobuf-java-util
        ProtobufHttpMessageConverter()

    @Bean
    @ConditionalOnMissingBean
    fun protobufDecoder(messageConverters: ObjectFactory<HttpMessageConverters>): Decoder =
        ResponseEntityDecoder(SpringDecoder(messageConverters))

    @Bean
    @ConditionalOnMissingBean
    fun protobufEncoder(messageConverters: ObjectFactory<HttpMessageConverters>): Encoder =
        SpringEncoder(messageConverters)
}
