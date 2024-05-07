package org.highfives.grid.work.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.work.query.aggregate.QueryAdTime;

import java.util.Map;

@Mapper
public interface AdTimeMapper {

   QueryAdTime selectAdTimeByEmployeeIdAndEndTime(QueryAdTime queryAdTime);

   QueryAdTime selectAdTimeByEmployeeIdAndEndTime(Map<String, Object> objMap);
}
