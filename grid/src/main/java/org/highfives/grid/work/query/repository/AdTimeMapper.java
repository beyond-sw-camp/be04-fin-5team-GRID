package org.highfives.grid.work.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.work.query.dto.AdTimeDTO;

import java.util.List;

@Mapper
public interface AdTimeMapper {

//   AdTime selectAdTimeByEmployeeIdAndEndTime(AdTimeDTO adTime);

   List<AdTimeDTO> selectAdTimeByEmployee(int employeeId);

   List<AdTimeDTO> selectAdTimeAll();
}
