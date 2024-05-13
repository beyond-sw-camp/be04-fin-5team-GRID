package org.highfives.grid.approval_chain.query.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "org.highfives.grid.approval_chain.query", annotationClass = Mapper.class)
public class MybatisConfiguration {

}
