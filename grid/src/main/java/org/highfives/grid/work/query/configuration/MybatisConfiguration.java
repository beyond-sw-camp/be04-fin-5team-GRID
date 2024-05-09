package org.highfives.grid.work.query.configuration;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "org.highfives.grid.work.query", annotationClass = Mapper.class)
public class MybatisConfiguration {
}
