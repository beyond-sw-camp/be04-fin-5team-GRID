package org.highfives.grid.user.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.user.query.dto.DutiesDTO;
import org.highfives.grid.user.query.dto.LeaderInfoDTO;
import org.highfives.grid.user.query.dto.PositionDTO;
import org.highfives.grid.user.query.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    LeaderInfoDTO getDepInfo(int id);

    LeaderInfoDTO getTeamInfo(int id);

    LeaderInfoDTO getDepLeaderInfo(int depLeaderId);

    LeaderInfoDTO getTeamLeaderInfo(int teamLeaderId);

    List<UserDTO> getList();
//
//    List<UserDTO> getUserListByName(String name);

    List<UserDTO> getUserList(@Param("offset") long offset, @Param("pageSize") int pageSize,
                              @Param("auth") String auth);

    List<UserDTO> getUserListByName(@Param("name") String name, @Param("offset") long offset,
                                    @Param("pageSize") int pageSize, @Param("auth") String auth);

    long countAllUsers(); // 총 사용자 수를 계산하는 메소드

    long countUsersByName(@Param("name") String name);

    UserDTO getUserInfo(Map<String, Object> info);

    Map<String, Object> getUserInfoByEmail(String email);

    List<PositionDTO> findPositions();

    List<UserDTO> findTeamList(int teamId);

    List<DutiesDTO> findDuties();
}
