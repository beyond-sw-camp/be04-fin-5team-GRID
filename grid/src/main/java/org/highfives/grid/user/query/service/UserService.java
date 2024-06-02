package org.highfives.grid.user.query.service;

import org.highfives.grid.user.query.dto.DutiesDTO;
import org.highfives.grid.user.query.dto.LeaderInfoDTO;
import org.highfives.grid.user.query.dto.PositionDTO;
import org.highfives.grid.user.query.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface UserService {
    Page<UserDTO> findAllUsers(Pageable pageable);

    Page<UserDTO> findUsersByName(String name, Pageable pageable);

    LeaderInfoDTO findLeaderInfo(int id);

    UserDTO findUserByEmployeeNum(String eNum);

    UserDTO findUserById(int id);

    Map<String, Object> checkNameByEmail(String email);

    List<UserDTO> findTeamList(int teamId);

    UserDTO findUserByEmail(String email);

    List<PositionDTO> findPositions();

    List<DutiesDTO> findDuties();

    List<UserDTO> findList();
}
