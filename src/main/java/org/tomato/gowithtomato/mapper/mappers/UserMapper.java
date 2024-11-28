package org.tomato.gowithtomato.mapper.mappers;

import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.mapper.RouteMapper;
import org.tomato.gowithtomato.mapper.RowMapper;
import org.tomato.gowithtomato.mapper.TripMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    private static final UserMapper INSTANCE = new UserMapper();

    private UserMapper(){
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
}
