// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.redisson.starter.testutil

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.MapPropertySource

@Configuration
class ClientConfiguration {
    @Bean
    fun redissonClient(): RedissonClient = Redisson.create()
}

fun createApplicationContext(configuration: Class<*>, includeClientBean: Boolean, vararg properties: Pair<String, Any>) =
    AnnotationConfigApplicationContext()
        .apply {
            environment.propertySources.addFirst(MapPropertySource("testProperties", properties.toMap()))
            if (includeClientBean) register(ClientConfiguration::class.java)
            register(configuration)
            refresh()
        }
