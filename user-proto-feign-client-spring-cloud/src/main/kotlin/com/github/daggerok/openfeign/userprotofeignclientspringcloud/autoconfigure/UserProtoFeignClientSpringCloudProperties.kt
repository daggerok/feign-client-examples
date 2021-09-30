package com.github.daggerok.openfeign.userprotofeignclientspringcloud.autoconfigure

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("user-proto-feign-client-spring-cloud")
data class UserProtoFeignClientSpringCloudProperties(
    val host: String = "undefined",
    val port: Int = -1,
    val contextPath: String = "/",
    val protocol: String = "http",
    val url: String = "${protocol}://${host}:${port}${contextPath}",
)
