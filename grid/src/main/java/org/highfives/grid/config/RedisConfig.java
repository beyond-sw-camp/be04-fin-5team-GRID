package org.highfives.grid.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {

    @Value("${application.redis.host}")
    private String host;

    @Value("${application.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory (){
        return new LettuceConnectionFactory(host, port);
    }
}
