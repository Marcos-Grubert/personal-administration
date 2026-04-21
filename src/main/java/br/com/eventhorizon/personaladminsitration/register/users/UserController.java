package br.com.eventhorizon.personaladminsitration.register.users;

import br.com.eventhorizon.personaladminsitration.register.users.dto.UserCreateDto;
import br.com.eventhorizon.personaladminsitration.register.users.dto.UserResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/createUser")
    public UserResponseDto createUser(@RequestBody UserCreateDto userCreateDto) {
        return userService.create(userCreateDto);
    }
}
