package org.highfives.grid.department.query.configuration;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages  = "org.highfives.grid.department.query", annotationClass = Mapper.class)
public class MapperConfig {

}
