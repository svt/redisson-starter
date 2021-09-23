// SPDX-FileCopyrightText: 2020 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.redisson.starter

import org.redisson.api.RedissonClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import se.svt.oss.redisson.starter.config.RedisProperties
import se.svt.oss.redisson.starter.lock.RedissonLockService
import se.svt.oss.redisson.starter.queue.QueueItem
import se.svt.oss.redisson.starter.queue.RedissonLibQueue

@EnableConfigurationProperties(RedisProperties::class)
@Configuration
class RedissonAutoConfiguration {

    @Bean
    @ConditionalOnProperty("redis.redisson.lock.name-prefix")
    @ConditionalOnBean(RedissonClient::class)
    fun redissonLockService(redissonClient: RedissonClient, redisProperties: RedisProperties) =
        RedissonLockService(redissonClient, redisProperties)

    @Bean
    @ConditionalOnProperty("redis.redisson.queue.name")
    @ConditionalOnBean(RedissonClient::class)
    fun redissonPriorityQueue(redisson: RedissonClient, redisProperties: RedisProperties): RedissonLibQueue {
        val priorityQueue = redisson.getPriorityBlockingQueue<QueueItem>(redisProperties.redisson.queue.name)
        return RedissonLibQueue(priorityQueue)
    }
}
