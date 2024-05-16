package org.highfives.grid.user.service;

import lombok.extern.slf4j.Slf4j;
import org.highfives.grid.user.entity.Employee;
import org.highfives.grid.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<Employee> getAllUserinfo() {
        return userRepository.findAll();
    }

    @Override
    public Optional<Employee> getUserInfo(int userId) {
        return userRepository.findById(Long.valueOf(userId));
    }


}
