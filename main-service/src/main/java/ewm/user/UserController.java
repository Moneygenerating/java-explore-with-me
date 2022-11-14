package ewm.user;

import ewm.helper.Update;
import ewm.user.dto.UserDto;
import ewm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(value = "from", required = false, defaultValue = "0")
                                     @PositiveOrZero int from,
                                     @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                     @RequestParam(value = "ids") List<Long> ids) {
        log.info("Запрос user Get getAll /admin/users");
        return userService.getUsers(PageRequest.of(from / size, size), ids);
    }

    @PostMapping
    public UserDto createNewUser(@RequestBody UserDto user) {
        log.info("Запрос user Post createNewUser /admin/users");
        return userService.createUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@Validated({Update.class}) @RequestBody UserDto userDto,
                              @PathVariable Long userId) {
        log.info("Запрос user Update updateUser /admin/users");
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Запрос user Delete deleteUser /admin/users");
        userService.deleteUserById(userId);
    }
}
