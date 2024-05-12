package org.highfives.grid.user.query.service;

import org.highfives.grid.user.query.dto.LeaderInfoDTO;
import org.highfives.grid.user.query.dto.UserDTO;
import org.highfives.grid.user.query.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("QueryUserService")
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDTO> findAllUsers() {

        List<UserDTO> test = userMapper.getUserList();
        System.out.println("test = " + test);
        return null;
    }

    @Override
    public LeaderInfoDTO findLeaderInfo(int id) {

        LeaderInfoDTO result = new LeaderInfoDTO();
        LeaderInfoDTO info = userMapper.getDepInfo(id);
        result.setDepName(info.getDepName());

        info = userMapper.getDepLeaderInfo(info.getDepLeaderId());
        result.setDepLeaderName(info.getDepLeaderName());
        result.setDepLeaderPosition(info.getDepLeaderPosition());

        info = userMapper.getTeamInfo(id);
        result.setTeamName(info.getTeamName());

        info = userMapper.getTeamLeaderInfo(info.getTeamLeaderId());
        result.setTeamLeaderName(info.getTeamLeaderName());
        result.setTeamLeaderPosition(info.getTeamLeaderPosition());

        System.out.println("returnInfo = " + result);

        return result;
    }
}
