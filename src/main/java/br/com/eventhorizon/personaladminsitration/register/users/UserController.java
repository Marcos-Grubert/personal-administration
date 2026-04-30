package br.com.eventhorizon.personaladminsitration.register.users;

import br.com.eventhorizon.personaladminsitration.register.users.dto.UserCreateDto;
import br.com.eventhorizon.personaladminsitration.register.users.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> findAllUsers(){
        List<UserResponseDto> userResponseDto = userService.findAll();
        return ResponseEntity.ok(userResponseDto);
    }
}
