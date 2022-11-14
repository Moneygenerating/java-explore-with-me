package ewm.user;

import ewm.user.dto.NewUserRequestDto;
import ewm.user.dto.UserDto;
import ewm.user.dto.UserShortDto;
import ewm.user.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }

    public static NewUserRequestDto toNewUserRequestDto(User user) {
        return new NewUserRequestDto(
                user.getId(),
                user.getName()
        );
    }

    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getName(),
                userDto.getEmail()
        );
    }
}
