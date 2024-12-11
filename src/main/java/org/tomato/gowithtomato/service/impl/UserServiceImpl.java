package org.tomato.gowithtomato.service.impl;


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

    private final UserMapper mapper;

    public UserServiceImpl() {
        this.mapper = UserMapper.getInstance();
        this.userDao = DaoFactory.getUserDAO();
    }

    public static UserServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override

    public UserDTO findUserByLogin(String login) {
        Optional<User> user = userDao.findByLogin(login);
        if (user.isPresent()) {
            return mapper.convertUserToDTO(user.get());
        }
        throw new UnauthorizedException("Нет доступа к странице");
    }

    @Override

    public String getPasswordByLogin(String login) {
        return userDao.getPasswordByLogin(login).orElseThrow(UserNotFoundException::new);
    }

    @Override

    public UserDTO findByEmail(String email) {
        Optional<User> user = userDao.findByEmail(email);
        if (user.isPresent()) {
            return mapper.convertUserToDTO(user.get());
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

    @Override
    public UserDTO updatePhotoUrl(UserDTO origUser, String url, Long userId) {
        userDao.updatePhotoUrl(url, userId);
        origUser.setAvatarUrl(url);
        return origUser;
    }

    @Override
    public void delete(Long id) {
        userDao.delete(id);
    }

    @Override
    public Optional<UserDTO> getByTripId(Long tripId) {

        Optional<User> optionalUser = userDao.getByTripId(tripId);
        return optionalUser.map(mapper::convertUserToDTO);
    }

    @Override
    public Optional<UserDTO> getByRouteId(Long routeId) {
        Optional<User> optionalUser = userDao.getByRouteId(routeId);
        return optionalUser.map(mapper::convertUserToDTO);
    }

}
