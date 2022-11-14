package ewm.user.service;

import ewm.errors.ValidationException;
import ewm.user.UserMapper;
import ewm.user.UserRepository;
import ewm.user.dto.UserDto;
import ewm.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Override
    //generate response users by id,ids,all
    public List<UserDto> getUsers(Pageable pageable, List<Long> ids) {
        if (ids.size() == 0) {
            return userRepository.findAll(pageable)
                    .stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAllById(ids)
                    .stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    //polymorphic method getUsers for one user
    @Override
    public List<UserDto> getUsers(Long ids) {
        return List.of(UserMapper.toUserDto(userRepository.getReferenceById(ids)));

    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public UserDto createUser(UserDto userDto) {
        if (validateUser(userDto)) {
            User user = UserMapper.toUser(userDto); //id=null исправление
            return UserMapper.toUserDto(userRepository.save(user));
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public UserDto updateUser(Long userId, UserDto userDto) {
        //getUsers(byId)
        UserDto userDtoCheck = getUsers(userId).get(0);

        if (userDtoCheck != null) {
            if (userDto.getName() != null) {
                userDtoCheck.setName(userDto.getName());
            }
            if (userDto.getEmail() != null) {
                validateUser(userDto);
                userDtoCheck.setEmail(userDto.getEmail());
            }

            User user = UserMapper.toUser(userDtoCheck);
            user.setId(userDtoCheck.getId());
            userRepository.save(user);
        }
        return userDtoCheck;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    private boolean validateUser(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isBlank() || userDto.getEmail().isEmpty()) {
            throw new ValidationException("Отсутствует email");
        }

        if (!userDto.getEmail().endsWith(".com") || !userDto.getEmail().contains("@")) {
            throw new ValidationException("Передан неверный email");
        }
        return true;
    }

}
