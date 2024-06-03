package org.highfives.grid.department.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.department.query.aggregate.Team;

import java.util.List;

@Mapper
public interface TeamMapper {
    Team findTeamById(int id);

    List<Team> findTeamList();

    List<Team> findSubDepartmentByDepartmentId(int id);
}
