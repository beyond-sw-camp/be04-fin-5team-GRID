package org.highfives.grid.user.query.service;

import org.highfives.grid.user.query.dto.DutiesDTO;
import org.highfives.grid.user.query.dto.LeaderInfoDTO;
import org.highfives.grid.user.query.dto.PositionDTO;
import org.highfives.grid.user.query.dto.UserDTO;
import org.highfives.grid.user.query.repository.UserMapper;
import org.highfives.grid.user.query.repository.ImgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("QueryUserService")
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;
    private final ImgMapper imgMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, ImgMapper imgMapper)
    {
        this.userMapper = userMapper;
        this.imgMapper = imgMapper;
    }

    @Override
    public UserDTO findUserByEmployeeNum(String eNum) {

        Map<String, Object> info = new HashMap<>();
        info.put("eNum", eNum);

        try {
            UserDTO result = userMapper.getUserInfo(info);
            result.setProfilePath(imgMapper.getProfileImg(result.getId()));
            return result;

        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public UserDTO findUserById(int id) {

        Map<String, Object> info = new HashMap<>();
        info.put("id", id);

        try {
            UserDTO result = userMapper.getUserInfo(info);
            result.setProfilePath(imgMapper.getProfileImg(result.getId()));
            return result;

        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        Map<String, Object> info = new HashMap<>();
        info.put("email", email);

        try {
            UserDTO result = userMapper.getUserInfo(info);
            result.setProfilePath(imgMapper.getProfileImg(result.getId()));
            return result;

        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public List<UserDTO> findList() {

        // employee 테이블 정보 조회
        List<UserDTO> userList = userMapper.getList();

        // profile 이미지 조회 해서 입력
        for (int i = 0; i < userList.size(); i++) {
            UserDTO userDTO = userList.get(i);
            userDTO.setProfilePath(imgMapper.getProfileImg(userDTO.getId()));
            userList.set(i, userDTO);
        }

        return userList;
    }

    @Override
    public Page<UserDTO> findAllUsers(Pageable pageable) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        List<UserDTO> userList = userMapper.getUserList(offset, pageSize);

        userList.forEach(userDTO -> {
            userDTO.setProfilePath(imgMapper.getProfileImg(userDTO.getId()));
        });

        long total = userMapper.countAllUsers(); // 총 사용자 수를 계산하는 메소드가 필요합니다.

        return new PageImpl<>(userList, pageable, total);
    }

    @Override
    public Page<UserDTO> findUsersByName(String name, Pageable pageable) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        List<UserDTO> userList = userMapper.getUserListByName(name, offset, pageSize);

        userList.forEach(userDTO -> {
            userDTO.setProfilePath(imgMapper.getProfileImg(userDTO.getId()));
        });

        long total = userMapper.countUsersByName(name); // 총 이름 검색 결과 수를 계산하는 메소드가 필요합니다.

        return new PageImpl<>(userList, pageable, total);
    }

    @Override
    public LeaderInfoDTO findLeaderInfo(int id) {

        LeaderInfoDTO result = new LeaderInfoDTO();
        LeaderInfoDTO info = userMapper.getDepInfo(id);
        result.setDepName(info.getDepName());
        result.setDepLeaderId(info.getDepLeaderId());

        info = userMapper.getDepLeaderInfo(info.getDepLeaderId());
        result.setDepLeaderName(info.getDepLeaderName());
        result.setDepLeaderPosition(info.getDepLeaderPosition());

        info = userMapper.getTeamInfo(id);
        result.setTeamName(info.getTeamName());
        result.setTeamLeaderId(info.getTeamLeaderId());

        info = userMapper.getTeamLeaderInfo(info.getTeamLeaderId());
        result.setTeamLeaderName(info.getTeamLeaderName());
        result.setTeamLeaderPosition(info.getTeamLeaderPosition());

        return result;
    }

    @Override
    public List<PositionDTO> findPositions() {

        return userMapper.findPositions();
    }

    @Override
    public List<DutiesDTO> findDuties() {

        return userMapper.findDuties();
    }

    @Override
    public Map<String, Object> checkNameByEmail(String email) {
        try {
            return userMapper.getUserInfoByEmail(email);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public List<UserDTO> findTeamList(int teamId) {

        List<UserDTO> userDTOList = userMapper.findTeamList(teamId);

        return userDTOList;
    }
}
