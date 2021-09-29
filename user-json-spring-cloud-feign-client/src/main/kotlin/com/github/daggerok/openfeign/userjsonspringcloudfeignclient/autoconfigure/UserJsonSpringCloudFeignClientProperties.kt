package com.github.daggerok.openfeign.userjsonspringcloudfeignclient.autoconfigure

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * Configuration to connect user-json-openfeign-client to user-json-server.
 */
@ConstructorBinding
@ConfigurationProperties("user-json-spring-cloud-feign-client")
data class UserJsonSpringCloudFeignClientProperties(
    val host: String = "undefined",
    val port: Int = -1,
    val contextPath: String = "/",
    val protocol: String = "http",
    val url: String = "${protocol}://${host}:${port}${contextPath}",
)
