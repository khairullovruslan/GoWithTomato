package org.tomato.gowithtomato.service;


import org.modelmapper.ModelMapper;
import org.tomato.gowithtomato.dao.UserDAOImpl;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.UserNotFoundException;

import java.util.Optional;

public class UserService {
    private final static UserService INSTANCE = new UserService();
    private final UserDAOImpl userDao;
    private final ModelMapper mapper;

    public UserService() {
        this.mapper = new ModelMapper();
        this.userDao = UserDAOImpl.getInstance();
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    public UserDTO findUserByLogin(String login){
        Optional<User> user = userDao.findByLogin(login);
        if (user.isPresent()){
            return mapper.map(user.get(), UserDTO.class);
        }
        throw new UserNotFoundException();
    }

}
