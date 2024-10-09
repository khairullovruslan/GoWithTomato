package org.tomato.gowithtomato.service;

import org.modelmapper.ModelMapper;
import org.tomato.gowithtomato.dao.UserDaoImpl;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.entity.User;

public class AuthService {
    private final UserDaoImpl userDao;
    private final  ModelMapper mapper;


    public AuthService(UserDaoImpl userDao, ModelMapper mapper) {
        this.userDao = userDao;
        this.mapper = mapper;
    }
    public void registration(UserDTO userDTO){
        userDao.save(mapper.map(userDTO, User.class));
    }
}
