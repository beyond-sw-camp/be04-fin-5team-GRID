package org.highfives.grid.user.query.repository;

import org.apache.ibatis.annotations.Mapper;
import org.highfives.grid.user.query.dto.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    LeaderInfoDTO getTeamDepartmentInfo(int id);

    List<NameAndPositionDTO> getNameAndPositionAndEmail(@Param("leadersId") Map<String, Integer> leadersId);

//    LeaderInfoDTO getDepInfo(int id);
//
//    LeaderInfoDTO getTeamInfo(int id);
//
//    LeaderInfoDTO getDepLeaderInfo(int depLeaderId);
//
//    LeaderInfoDTO getTeamLeaderInfo(int teamLeaderId);

    List<UserDTO> getList();

    List<UserDTO> getUserList(@Param("offset") long offset, @Param("pageSize") int pageSize,
                              @Param("auth") String auth);

    List<UserDTO> getUserListByName(@Param("name") String name, @Param("offset") long offset,
                                    @Param("pageSize") int pageSize, @Param("auth") String auth);

    long countAllUsers(); // 총 사용자 수를 계산하는 메소드 (페이징 용)

    long countUsersByName(@Param("name") String name);

    UserDTO getUserInfo(Map<String, Object> info);

    Map<String, Object> getUserInfoByEmail(String email);

    List<PositionDTO> findPositions();

    List<UserDTO> findTeamList(int teamId);

    List<DutiesDTO> findDuties();

}
