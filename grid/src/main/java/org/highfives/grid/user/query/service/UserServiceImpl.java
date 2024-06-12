package org.highfives.grid.user.query.service;

import org.highfives.grid.approval.query.dto.EmpStatusDTO;
import org.highfives.grid.user.command.aggregate.YN;
import org.highfives.grid.user.exception.InvalidNumberException;
import org.highfives.grid.user.exception.UserNotFoundException;
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
import java.util.stream.Collectors;

@Service("QueryUserService")
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;
    private final ImgMapper imgMapper;

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
    public Page<UserDTO> findAllUsers(Pageable pageable, Map<Integer, EmpStatusDTO> ndAbsenceInfo, String auth) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        List<UserDTO> userList = userMapper.getUserList(offset, pageSize, auth);

        Map<Integer, EmpStatusDTO> absenceInfo = (ndAbsenceInfo != null) ? ndAbsenceInfo : new HashMap<>();

        userList.forEach(user -> {
            System.out.println("user.getId() + user.getResignYn() = " + user.getId() + user.getResignYn());
            if(user.getResignYn().equals(YN.N)) {
                EmpStatusDTO empStatusDTO = absenceInfo.get(user.getId());
                if (empStatusDTO != null) {
                    user.setAbsenceYn(YN.Y);
                    user.setAbsenceContent(empStatusDTO.getStatus());
                }
            }
            user.setProfilePath(imgMapper.getProfileImg(user.getId()));
        });

        long total = userMapper.countAllUsers();

        return new PageImpl<>(userList, pageable, total);
    }

    @Override
    public Page<UserDTO> findUsersByName(String name, Pageable pageable, Map<Integer, EmpStatusDTO> ndAbsenceInfo,
                                         String auth) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        List<UserDTO> userList = userMapper.getUserListByName(name, offset, pageSize, auth);

        Map<Integer, EmpStatusDTO> absenceInfo = (ndAbsenceInfo != null) ? ndAbsenceInfo : new HashMap<>();

        userList.forEach(user -> {
            if(user.getResignYn() == YN.N) {
                EmpStatusDTO empStatusDTO = absenceInfo.get(user.getId());
                if (empStatusDTO != null) {
                    user.setAbsenceYn(YN.Y);
                    user.setAbsenceContent(empStatusDTO.getStatus());
                }
            }
            user.setProfilePath(imgMapper.getProfileImg(user.getId()));
        });

        long total = userMapper.countUsersByName(name);

        return new PageImpl<>(userList, pageable, total);
    }
    @Autowired
    public UserServiceImpl(UserMapper userMapper, ImgMapper imgMapper)
    {
        this.userMapper = userMapper;
        this.imgMapper = imgMapper;
    }

    @Override
    public UserDTO findUserByEmployeeNum(String eNum, Map<Integer, EmpStatusDTO> ndAbsenceInfo) {

        if (eNum == null || eNum.trim().isEmpty() || !eNum.matches("\\d+") || eNum.length() != 7) {
            throw new InvalidNumberException("Invalid employee number: " + eNum);
        }
        Map<Integer, EmpStatusDTO> absenceInfo = (ndAbsenceInfo != null) ? ndAbsenceInfo : new HashMap<>();
        Map<String, Object> info = new HashMap<>();
        info.put("eNum", eNum);

        UserDTO result = userMapper.getUserInfo(info);
        if (result == null) {
            throw new UserNotFoundException("User not found with employee number: " + eNum);
        }

        if( result.getResignYn() == YN.N) {
            EmpStatusDTO absence = absenceInfo.get(result.getId());
            if (absence != null) {
                result.setAbsenceYn(YN.Y);
                result.setAbsenceContent(absence.getStatus());
                result.setAbsenceStartTime(absence.getStartTime());
                result.setAbsenceEndTime(absence.getEndTime());
            }
        }

        result.setProfilePath(imgMapper.getProfileImg(result.getId()));
        result.setSealPath(imgMapper.getSealImg(result.getId()));

        return result;
    }


    @Override
    public UserDTO findUserById(int id) {

        if (id <= 0) {
            throw new InvalidNumberException("Invalid user id: " + id);
        }

        Map<String, Object> info = new HashMap<>();
        info.put("id", id);

        UserDTO result = userMapper.getUserInfo(info);
        if(result == null) {
            throw new UserNotFoundException("User not found with user id: " + id);
        }

        result.setProfilePath(imgMapper.getProfileImg(result.getId()));
        result.setSealPath(imgMapper.getSealImg(result.getId()));
        return result;
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        Map<String, Object> info = new HashMap<>();
        info.put("email", email);

        UserDTO result = userMapper.getUserInfo(info);
        if(result == null) {
            throw new UserNotFoundException("User not found with email: " + email);
        }

        result.setProfilePath(imgMapper.getProfileImg(result.getId()));
        result.setSealPath(imgMapper.getSealImg(result.getId()));
        return result;
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

        Map<String, Object> result = userMapper.getUserInfoByEmail(email);
        if(result == null)
            throw new UserNotFoundException("User not found with email: " + email);

        return result;
    }

    @Override
    public List<UserDTO> findTeamList(int teamId) {

        if(teamId <= 0)
            throw new InvalidNumberException("Invalid team id: " + teamId);

        List<UserDTO> userDTOList = userMapper.findTeamList(teamId);
        if(userDTOList == null || userDTOList.isEmpty())
            throw new UserNotFoundException("User not found at team id: " + teamId);

        return userDTOList;
    }
}
