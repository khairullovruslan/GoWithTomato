package org.tomato.gowithtomato.service;

import org.modelmapper.ModelMapper;
import org.tomato.gowithtomato.dao.UserDaoImpl;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.entity.User;

public class AuthService {
    private final UserDaoImpl userDao;
    private final  ModelMapper mapper;


    public AuthService() {
        this.userDao = UserDaoImpl.getInstance();
        this.mapper = new ModelMapper();
    }
    public void registration(UserDTO userDTO){
        userDao.save(mapper.map(userDTO, User.class));
    }
}
