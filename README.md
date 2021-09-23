![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/svt/redisson-starter)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![REUSE status](https://api.reuse.software/badge/git.fsfe.org/reuse/api)](https://api.reuse.software/info/git.fsfe.org/reuse/api)


# Redisson-starter

Spring boot starter that given a [redisson](https://github.com/redisson/redisson) client bean provides additional services for working with
redisson locks and queues:
 
 - **RedissonLockService**
 	A service that provied a ```tryWithLock``` method that uses a redisson lock. 
	A bean will be provided  if `redis.redisson.lock.name-prefix` property is set.
 - **RedissonLibQeueue**
	If `redis.redisson.queue.name property` is set, a redisson priority queue will be provided.

This library is written in kotlin.

## Usage ##

Add the lib as a dependency to your Gradle build file.

```kotlin
implementation("se.svt.oss:redisson-starter:2.0.0")
```

Configure in `application.yml` (or other property source) :

```
redis:
  uri: redis://localhost:6379
  db: 0
  redisson:
    lock:
      name: ${service.name}-lock
      wait-time: 0s
      lease-time: 60m
    queue:
      name: ${service.name}-queue
```

For the lock service, name, wait-time and lease-time only configures default values that can be overriden when calling 
the lock service:

```
redissonLockService.tryWithLock(name = "my-other-lock", 
                                waitTime = Duration.ofSeconds(10),
                                leaseTime = Duration.ofHours(1)) {
   // DO SOME STUFF WITH LOCK
}
```

# Mocking the RedissonLockService with mockk

Calling `RedissonLockService.tryWithLock` on a mockk object without specifiying all parameters will fail, because
the default parameter values reference an instance field that will not be available. 
To get around this, a spy can be used instead:

```kotlin
private val redissonLockService = spyk(RedissonLockService(mockk(), mockk(relaxed = true)))
``` 

#### Tests ####

run `./gradlew clean check` for unit tests and code quality checks
  
## License

Copyright 2020 Sveriges Television AB

This software is released under the Apache 2.0 License.

## Primary Maintainers

SVT Videocore team <videocore@teams.svt.se>
