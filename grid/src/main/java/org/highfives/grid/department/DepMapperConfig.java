package org.highfives.grid.department;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "org.highfives.grid.department.dao", annotationClass = Mapper.class)
public class DepMapperConfig {
}
