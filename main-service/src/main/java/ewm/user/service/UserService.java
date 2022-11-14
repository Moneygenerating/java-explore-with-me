package ewm.user.service;

import ewm.user.dto.UserDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(Pageable pageable, List<Long> ids);

    List<UserDto> getUsers(Long ids);

    UserDto createUser(UserDto user);

    UserDto updateUser(Long userId, UserDto userDto);

    void deleteUserById(Long userId);
}
