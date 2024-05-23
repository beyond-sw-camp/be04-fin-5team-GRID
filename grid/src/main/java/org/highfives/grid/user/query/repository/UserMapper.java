package org.highfives.grid.user.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.user.query.dto.LeaderInfoDTO;
import org.highfives.grid.user.query.dto.UserDTO;

import java.util.List;

@Mapper
public interface UserMapper {

    LeaderInfoDTO getDepInfo(int id);

    LeaderInfoDTO getTeamInfo(int id);

    LeaderInfoDTO getDepLeaderInfo(int depLeaderId);

    LeaderInfoDTO getTeamLeaderInfo(int teamLeaderId);

    List<UserDTO> getUserList();

    UserDTO getUserInfoById(int id);

    UserDTO getUserInfo(int eNum);

    String getPosition(int id);

    String getDuties(int id);
}
