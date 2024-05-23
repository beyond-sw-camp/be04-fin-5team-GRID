package org.highfives.grid.user.query.service;

import org.highfives.grid.user.query.dto.LeaderInfoDTO;
import org.highfives.grid.user.query.dto.UserDTO;
import org.highfives.grid.user.query.repository.UserMapper;
import org.highfives.grid.user.query.repository.ImgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public UserDTO findUserByEmployeeNum(int eNum) {

        try {
            UserDTO result = userMapper.getUserInfo(eNum);
            result.setProfilePath(imgMapper.getProfileImg(result.getId()));
            result.setPosition(userMapper.getPosition(result.getId()));
            result.setDuties(userMapper.getDuties(result.getId()));

            System.out.println("result = " + result);
            return result;

        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public UserDTO findUserById(int id) {
        try {
            UserDTO result = userMapper.getUserInfoById(id);
            result.setProfilePath(imgMapper.getProfileImg(result.getId()));
            result.setPosition(userMapper.getPosition(result.getId()));
            result.setDuties(userMapper.getDuties(result.getId()));

            System.out.println("result = " + result);
            return result;

        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public List<UserDTO> findAllUsers() {

        // employee 테이블 정보 조회
        List<UserDTO> userList = userMapper.getUserList();

        // profile 이미지 조회 해서 입력
        for (int i = 0; i < userList.size(); i++) {
            UserDTO userDTO = userList.get(i);

            userDTO.setProfilePath(imgMapper.getProfileImg(userDTO.getId()));
            userDTO.setPosition(userMapper.getPosition(userDTO.getId()));
            userDTO.setDuties(userMapper.getDuties(userDTO.getId()));

            userList.set(i, userDTO);
        }

        return userList;
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


}
