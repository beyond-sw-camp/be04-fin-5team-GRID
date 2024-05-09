package org.highfives.grid.work.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.work.query.aggregate.AdTime;

import java.util.List;

@Mapper
public interface AdTimeMapper {

   AdTime selectAdTimeByEmployeeIdAndEndTime(AdTime adTime);

   List<AdTime> selectAdTimeByEmployee(int employeeId);

   List<AdTime> selectAdTimeAll();
}
