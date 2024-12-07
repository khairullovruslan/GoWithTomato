package org.tomato.gowithtomato.mapper;

import org.tomato.gowithtomato.dto.user.UserDTO;
import org.tomato.gowithtomato.dto.user.UserEditDTO;
import org.tomato.gowithtomato.dto.user.UserRegistrationDTO;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.util.PasswordUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    private static final UserMapper INSTANCE = new UserMapper();

    private UserMapper() {
    }

    public static UserMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public User mapRow(ResultSet result) throws SQLException {
        return User
                .builder()
                .id(result.getLong("id"))
                .login(result.getString("login"))
                .password(result.getString("password"))
                .email(result.getString("email"))
                .phoneNumber(result.getString("phone_number"))
                .build();
    }

    public User convertDTOToUser(UserDTO userDTO) {
        return User
                .builder()
                .id(userDTO.getId())
                .login(userDTO.getLogin())
                .phoneNumber(userDTO.getPhoneNumber())
                .email(userDTO.getEmail())
                .build();

    }

    public UserDTO convertUserToDTO(User user) {
        return UserDTO
                .builder()
                .id(user.getId())
                .login(user.getLogin())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .build();

    }

    public User convertUserRegistrationDTOToUser(UserRegistrationDTO user) {
        return User
                .builder()
                .login(user.login())
                .phoneNumber(user.phoneNumber())
                .email(user.email())
                .password(PasswordUtil.hashPassword(user.password()))
                .build();
    }

    public User convertUserEditDTOToUser(UserEditDTO userEditDTO, long userId) {
        return User
                .builder()
                    .id(userId)
                    .phoneNumber(userEditDTO.phoneNumber())
                    .email(userEditDTO.email())
                    .login(userEditDTO.login())
                .build();
    }
}
