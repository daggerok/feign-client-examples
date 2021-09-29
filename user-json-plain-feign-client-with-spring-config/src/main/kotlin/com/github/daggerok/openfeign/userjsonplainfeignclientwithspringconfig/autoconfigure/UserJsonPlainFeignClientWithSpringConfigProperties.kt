package com.github.daggerok.openfeign.userjsonplainfeignclientwithspringconfig.autoconfigure

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * Configuration to connect user-json-openfeign-client to user-json-server.
 */
@ConstructorBinding
@ConfigurationProperties("user-json-plain-feign-client-with-spring-config")
data class UserJsonPlainFeignClientWithSpringConfigProperties(
    val host: String = "undefined",
    val port: Int = -1,
    val contextPath: String = "/",
    val protocol: String = "http",
    val url: String = "${protocol}://${host}:${port}${contextPath}",
)
