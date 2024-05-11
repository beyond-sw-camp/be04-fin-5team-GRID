package org.highfives.grid.user.query.service;

import org.highfives.grid.user.query.dto.LeaderInfoDTO;

public interface UserService {
    void findAllUsers();

    LeaderInfoDTO findLeaderInfo(int id);
}
