package org.highfives.grid.work.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.work.query.aggregate.QueryAdTime;

import java.util.List;

@Mapper
public interface AdTimeMapper {

   QueryAdTime selectAdTimeByEmployeeIdAndEndTime(QueryAdTime queryAdTime);

   List<QueryAdTime> selectAdTimeByEmployee(int employeeId);

   List<QueryAdTime> selectAdTimeAll();
}
