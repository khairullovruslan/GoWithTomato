package org.tomato.gowithtomato.mapper;
import org.tomato.gowithtomato.dto.PointDTO;
import org.tomato.gowithtomato.dto.UserDTO;
import org.tomato.gowithtomato.entity.User;


public class UserMapper {
    private static final UserMapper INSTANCE = new UserMapper();
    private UserMapper(){

    }

    public static UserMapper getInstance() {
        return INSTANCE;
    }
    public User convertDTOToUser(UserDTO userDTO){
        return User
                .builder()
                .id(userDTO.getId())
                .login(userDTO.getLogin())
                .password(userDTO.getPassword())
                .phoneNumber(userDTO.getPhoneNumber())
                .email(userDTO.getEmail())
                .build();

    }
    public UserDTO convertUserToDTO(User user){
        return UserDTO
                .builder()
                .id(user.getId())
                .login(user.getLogin())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .build();

    }

}
