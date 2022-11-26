package ewm.user.controllers.admin;

import ewm.helper.AbstractController;
import ewm.helper.Create;
import ewm.helper.Update;
import ewm.user.dto.UserDto;
import ewm.user.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@Slf4j
@Validated
public class UserController extends AbstractController<UserDto> {
    @Autowired
    UserServiceImpl userService;

    @GetMapping
    public List<UserDto> get(@RequestParam(value = "from", required = false, defaultValue = "0")
                             @PositiveOrZero int from,
                             @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                             @RequestParam(value = "ids") List<Long> ids) {
        log.info("Запрос user Get getAll /admin/users");
        return userService.get(PageRequest.of(from / size, size), ids);
    }

    @PostMapping
    public UserDto create(@RequestBody @Validated({Create.class}) UserDto user) {
        log.info("Запрос user Post createNewUser /admin/users");
        return userService.create(user);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@Validated({Update.class}) @RequestBody UserDto userDto,
                          @PathVariable Long userId) {
        log.info("Запрос user Update updateUser /admin/users");
        return userService.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("Запрос user Delete deleteUser /admin/users");
        userService.delete(userId);
    }
}
