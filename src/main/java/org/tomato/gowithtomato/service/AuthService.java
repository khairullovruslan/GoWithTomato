package org.tomato.gowithtomato.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.tomato.gowithtomato.dao.UserDAOImpl;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.util.PasswordUtil;

@Slf4j
public class AuthService {
    private final UserDAOImpl userDao;
    private final  ModelMapper mapper;
    private final PasswordUtil passwordUtil;


    public AuthService() {
        this.passwordUtil = PasswordUtil.getInstance();
        this.userDao = UserDAOImpl.getInstance();
        this.mapper = new ModelMapper();
    }
    public void registration(UserDTO userDTO){
        String password = passwordUtil.hashPassword(userDTO.getPassword());
        userDTO.setPassword(password);
        userDao.save(mapper.map(userDTO, User.class));
        log.info("Пользователь успешно зарегестрирован");
    }
}
