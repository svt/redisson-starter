// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.redisson.starter

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import se.svt.oss.junit5.redis.EmbeddedRedisExtension
import se.svt.oss.redisson.starter.Assertions.assertThat
import se.svt.oss.redisson.starter.config.RedisProperties
import se.svt.oss.redisson.starter.testutil.createApplicationContext
import java.time.Duration

@ExtendWith(EmbeddedRedisExtension::class)
class RedissonPropertiesTest {

    @Test
    fun `all redis properties are set correctly`() {
        val lockWaitTime = "10s"
        val lockLeaseTime = "30s"
        val lockName = "the-lock"
        val queueName = "queue"
        val timeout = "5s"
        val context = createApplicationContext(
            OnlyPropertiesConfiguration::class.java,
            true,
            "redis.redisson.lock.wait-time" to lockWaitTime,
            "redis.redisson.lock.lease-time" to lockLeaseTime,
            "redis.redisson.lock.name-prefix" to lockName,
            "redis.redisson.queue.name" to queueName,
            "redis.redisson.timeout" to timeout
        )

        val redisProperties = context.getBean(RedisProperties::class.java)

        System.out.println(redisProperties)

        assertThat(redisProperties)
            .isNotNull

        assertThat(redisProperties.redisson)
            .hasTimeout(Duration.ofSeconds(5))
            .isNotNull

        assertThat(redisProperties.redisson.lock)
            .isNotNull
            .hasLeaseTime(Duration.ofSeconds(30))
            .hasWaitTime(Duration.ofSeconds(10))
            .hasNamePrefix(lockName)

        assertThat(redisProperties.redisson.queue)
            .isNotNull
            .hasName(queueName)
    }

    @Test
    fun `not all redis properties are set`() {
        val context = createApplicationContext(
            OnlyPropertiesConfiguration::class.java,
            true
        )

        val redisProperties = context.getBean(RedisProperties::class.java)

        System.out.println(redisProperties)

        assertThat(redisProperties)
            .isNotNull

        assertThat(redisProperties.redisson)
            .isNotNull
            .hasTimeout(Duration.ofSeconds(6))

        assertThat(redisProperties.redisson.lock)
            .isNotNull
            .hasNamePrefix(null)
            .hasLeaseTime(Duration.ofMillis(-1))
            .hasWaitTime(Duration.ZERO)

        assertThat(redisProperties.redisson.queue)
            .isNotNull
            .hasName(null)
    }

    @EnableConfigurationProperties(RedisProperties::class)
    @Configuration
    class OnlyPropertiesConfiguration
}
