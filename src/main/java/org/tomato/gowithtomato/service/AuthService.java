package org.tomato.gowithtomato.service;

import org.modelmapper.ModelMapper;
import org.tomato.gowithtomato.dao.UserDAOImpl;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.entity.User;

public class AuthService {
    private final UserDAOImpl userDao;
    private final  ModelMapper mapper;


    public AuthService() {
        this.userDao = UserDAOImpl.getInstance();
        this.mapper = new ModelMapper();
    }
    public void registration(UserDTO userDTO){
        userDao.save(mapper.map(userDTO, User.class));
    }
}
