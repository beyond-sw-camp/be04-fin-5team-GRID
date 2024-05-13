package org.highfives.grid.user.query.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "org.highfives.grid.user.query.repository", annotationClass = Mapper.class)
public class UserMapperConfig {
}
