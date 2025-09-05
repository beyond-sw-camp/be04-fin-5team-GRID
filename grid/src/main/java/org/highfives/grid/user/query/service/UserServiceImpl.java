package org.highfives.grid.user.query.service;

import org.highfives.grid.approval.query.dto.EmpStatusDTO;
import org.highfives.grid.user.command.aggregate.YN;
import org.highfives.grid.user.exception.InvalidNumberException;
import org.highfives.grid.user.exception.UserNotFoundException;
import org.highfives.grid.user.query.dto.*;
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

    @Autowired
    public UserServiceImpl(UserMapper userMapper, ImgMapper imgMapper)
    {
        this.userMapper = userMapper;
        this.imgMapper = imgMapper;
    }

    @Override
    public List<UserDTO> findList() {

        // employee 테이블 정보 조회
        List<UserDTO> userList = userMapper.getList();

        // profile 이미지 조회 해서 입력
        for (UserDTO user : userList) {
            user.setProfilePath(imgMapper.getProfileImg(user.getId()));
        }

        return userList;
    }

    @Override
    public Page<UserDTO> findAllUsers(Pageable pageable, Map<Integer, EmpStatusDTO> absenceInfo, String auth) {
        return findUsers(pageable, absenceInfo, auth, null);
    }

    @Override
    public Page<UserDTO> findUsersByName(String name, Pageable pageable, Map<Integer, EmpStatusDTO> absenceInfo, String auth) {
        return findUsers(pageable, absenceInfo, auth, name);
    }

    //중복된 코드를 최대한 빼보자.
    private Page<UserDTO> findUsers(Pageable pageable, Map<Integer, EmpStatusDTO> absenceInfo, String auth, String name) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        List<UserDTO> userList = (name == null)
                ? userMapper.getUserList(offset, pageSize, auth)
                : userMapper.getUserListByName(name, offset, pageSize, auth);

        Map<Integer, EmpStatusDTO> validAbsenceInfo = (absenceInfo != null) ? absenceInfo : new HashMap<>();

        if (userList != null && !userList.isEmpty()) {
            List<Integer> userIdList = userList.stream().map(UserDTO::getId).collect(Collectors.toList());
            Map<Integer, String> profileImages = imgMapper.getProfileImages(userIdList);
            userList.forEach(user -> updateDetail(user, validAbsenceInfo, profileImages));
        }

        long total = (name == null)
                ? userMapper.countAllUsers()
                : userMapper.countUsersByName(name);

        return new PageImpl<>(userList, pageable, total);
    }

    private void updateDetail(UserDTO user, Map<Integer, EmpStatusDTO> absenceInfo, Map<Integer, String> profileImages) {
        if (user.getResignYn().equals(YN.N)) {
            EmpStatusDTO empStatusDTO = absenceInfo.get(user.getId());
            if (empStatusDTO != null) {
                user.setAbsenceYn(YN.Y);
                user.setAbsenceContent(empStatusDTO.getStatus());
            }
        }
        if(profileImages != null)
            user.setProfilePath(profileImages.get(user.getId()));
    }

    @Override
    public UserDTO findUserByEmployeeNum(String eNum, Map<Integer, EmpStatusDTO> absenceInfo) {

        if (eNum == null || eNum.trim().isEmpty() || !eNum.matches("\\d+") || eNum.length() != 7) {
            throw new InvalidNumberException("Invalid employee number: " + eNum);
        }

        Map<Integer, EmpStatusDTO> validAbsenceInfo = (absenceInfo != null) ? absenceInfo : new HashMap<>();
        Map<String, Object> info = new HashMap<>();
        info.put("eNum", eNum);

        UserDTO result = userMapper.getUserInfo(info);
        if (result == null) {
            throw new UserNotFoundException("User not found with employee number: " + eNum);
        }

        if( result.getResignYn() == YN.N)
            inputAbsenceInfo(validAbsenceInfo, result);

        inputImagePaths(result);

        return result;
    }

    private void inputAbsenceInfo(Map<Integer, EmpStatusDTO> validAbsenceInfo, UserDTO result) {
        EmpStatusDTO absence = validAbsenceInfo.get(result.getId());
        if (absence != null) {
            result.setAbsenceYn(YN.Y);
            result.setAbsenceContent(absence.getStatus());
            result.setAbsenceStartTime(absence.getStartTime());
            result.setAbsenceEndTime(absence.getEndTime());
        }
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

        inputImagePaths(result);

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

        inputImagePaths(result);

        return result;
    }

    private void inputImagePaths(UserDTO result) {
        Map<Integer, String> paths = imgMapper.getPaths(result.getId());
        if(paths != null && !paths.isEmpty()) {
            result.setSealPath(paths.get(1));
            result.setProfilePath(paths.get(2));
        }
    }

    @Override
    public LeaderInfoDTO findLeaderInfo(int id) {

        LeaderInfoDTO info = userMapper.getTeamDepartmentInfo(id);
        Map<String, Integer> leadersId = new HashMap<>();
        leadersId.put("team", info.getTeamLeaderId());
        leadersId.put("dep", info.getDepLeaderId());

        List<NameAndPositionDTO> nameAndDutiesInfo = userMapper.getNameAndPositionAndEmail(leadersId);;

        nameAndDutiesInfo.forEach(
                leaderInfo -> {
                    if (leaderInfo.getId() == leadersId.get("team")) {
                        info.setTeamLeaderName(leaderInfo.getName());
                        info.setTeamLeaderPosition(leaderInfo.getPositionName());
                        info.setTeamLeaderEmail(leaderInfo.getEmail());
                    }
                    if (leaderInfo.getId() == leadersId.get("dep")) {
                        info.setDepLeaderName(leaderInfo.getName());
                        info.setDepLeaderPosition(leaderInfo.getPositionName());
                        info.setDepLeaderEmail(leaderInfo.getEmail());
                    }
                }
        );

        return info;
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
