package org.highfives.grid.user.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.user.query.dto.DutiesDTO;
import org.highfives.grid.user.query.dto.LeaderInfoDTO;
import org.highfives.grid.user.query.dto.PositionDTO;
import org.highfives.grid.user.query.dto.UserDTO;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    LeaderInfoDTO getDepInfo(int id);

    LeaderInfoDTO getTeamInfo(int id);

    LeaderInfoDTO getDepLeaderInfo(int depLeaderId);

    LeaderInfoDTO getTeamLeaderInfo(int teamLeaderId);

    List<UserDTO> getUserList();

    List<UserDTO> getUserListByName(String name);

    UserDTO getUserInfo(Map<String, Object> info);

    Map<String, Object> getUserInfoByEmail(String email);

    List<PositionDTO> findPositions();

    List<UserDTO> findTeamList(int teamId);

    List<DutiesDTO> findDuties();
}
