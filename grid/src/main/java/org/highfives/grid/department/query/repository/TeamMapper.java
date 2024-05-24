package org.highfives.grid.department.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.department.query.aggregate.Team;

@Mapper
public interface TeamMapper {
    Team findTeamById(int id);
}
