package antifraud.presentation;

import antifraud.business.User;
import antifraud.persistence.service.UserDetailsServiceImpl;
import antifraud.business.dto.UserDto;
import antifraud.business.mappers.UserMapper;
import antifraud.business.dto.UsernameRole;
import antifraud.business.dto.UsernameStatus;
import antifraud.business.response.Delete;
import antifraud.business.response.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class RegistrationController {
    @Autowired
    UserDetailsServiceImpl userRepo;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping(value = "api/auth/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@Valid @RequestBody UserDto userDto) throws JsonProcessingException {
        if (userRepo.findUserByUsername(userDto.getUsername().toLowerCase()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        User user1 = userRepo.save(userMapper.toUser(userDto, userRepo.findAll().size()));
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(user1);
    }

    @GetMapping("/api/auth/list")
    public String getAuthList() throws JsonProcessingException {
        List<User> userList = userRepo.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(userList);
    }

    @DeleteMapping(value = "/api/auth/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Delete deleteUser(@PathVariable String username) {
        return userRepo.deleteUserByUsername(username);
    }

    @PutMapping("/api/auth/role")
    public String updateUserRole(@Valid @RequestBody UsernameRole body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(userRepo.updateUserByUsername(Optional.ofNullable(body), Optional.empty()));
    }

    @PutMapping(value = "/api/auth/access", produces = MediaType.APPLICATION_JSON_VALUE)
    public Status updateUserStatus(@Valid @RequestBody UsernameStatus body) {
        User user = userRepo.updateUserByUsername(Optional.empty(), Optional.ofNullable(body));
        return new Status(user);
    }
}
