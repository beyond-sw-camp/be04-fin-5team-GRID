package org.highfives.grid.user.service;


import org.highfives.grid.user.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<Employee> getAllUserinfo();

    Optional<Employee> getUserInfo(int userId);
}
