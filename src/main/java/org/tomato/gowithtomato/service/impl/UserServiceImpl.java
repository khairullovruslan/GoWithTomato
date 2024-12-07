package org.tomato.gowithtomato.service.impl;


import org.modelmapper.ModelMapper;
import org.tomato.gowithtomato.dao.daoInterface.UserDAO;
import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.dto.user.UserEditDTO;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.auth.UnauthorizedException;
import org.tomato.gowithtomato.exception.db.UserNotFoundException;
import org.tomato.gowithtomato.factory.DaoFactory;
import org.tomato.gowithtomato.mapper.UserMapper;
import org.tomato.gowithtomato.service.UserService;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final static UserServiceImpl INSTANCE = new UserServiceImpl();
    private final UserDAO userDao;
    private final ModelMapper mapper;

    public UserServiceImpl() {
        this.mapper = new ModelMapper();
        this.userDao = DaoFactory.getUserDAO();
    }

    public static UserServiceImpl getInstance() {
        return INSTANCE;
    }

    public UserDTO findUserByLogin(String login) {
        Optional<User> user = userDao.findByLogin(login);
        if (user.isPresent()) {
            return mapper.map(user.get(), UserDTO.class);
        }
        throw new UnauthorizedException("Нет доступа к странице");
    }

    public String getPasswordByLogin(String login) {
        return userDao.getPasswordByLogin(login).orElseThrow(UserNotFoundException::new);
    }

    public UserDTO findByEmail(String email) {
        Optional<User> user = userDao.findByEmail(email);
        if (user.isPresent()) {
            return mapper.map(user.get(), UserDTO.class);
        }
        throw new UserNotFoundException();

    }

    @Override
    public UserDTO edit(UserDTO origUser, UserEditDTO userEditDTO) {
        userDao.update(UserMapper.getInstance().convertUserEditDTOToUser(userEditDTO, origUser.getId()));
        origUser.setEmail(userEditDTO.email());
        origUser.setLogin(userEditDTO.login());
        origUser.setPhoneNumber(userEditDTO.phoneNumber());
        return origUser;
    }
}
