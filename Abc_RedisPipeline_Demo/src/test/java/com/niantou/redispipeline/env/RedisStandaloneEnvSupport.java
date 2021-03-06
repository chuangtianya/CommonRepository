package com.niantou.redispipeline.env;

import com.niantou.redispipeline.author.JustryDeng;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * 单机redis环境支持
 *
 * @author {@link JustryDeng}
 * @since 2020/11/13 16:14:18
 */
@Slf4j
@Testcontainers
@ContextConfiguration(initializers = RedisStandaloneEnvSupport.Initializer.class)
public class RedisStandaloneEnvSupport implements RedisEnvSupport {
    
    /**
     * 标准的docker镜像(即${镜像名}:${tag名})
     * <p>
     * 提示: image&tag可去https://hub.docker.com/u/library搜索
     */
    private static final String DOCKER_IMAGE_NAME = "redis:5.0.3-alpine";
    
    /** docker开启的端口 */
    private static final int CONTAINER_PORT = 6379;
    
    @Container
    public static GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse(DOCKER_IMAGE_NAME)).withExposedPorts(CONTAINER_PORT);
    
    /**
     * init application context
     *
     * @author {@link JustryDeng}
     * @since 2020/11/14 19:22:23
     */
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            String redisIpAddress = redisContainer.getContainerIpAddress();
            log.info("redisIpAddress is [{}]", redisIpAddress);
            System.setProperty("spring.redis.host", redisIpAddress);
            Integer redisPort = redisContainer.getMappedPort(CONTAINER_PORT);
            log.info("redisPort is [{}]", redisPort);
            Assert.assertNotNull("redisPort is null", redisPort);
            System.setProperty("spring.redis.port", redisPort.toString());
        }
    }
    
}