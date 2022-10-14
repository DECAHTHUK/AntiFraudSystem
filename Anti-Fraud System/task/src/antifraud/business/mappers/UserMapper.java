package antifraud.business.mappers;

import antifraud.business.User;
import antifraud.business.dto.UserDto;
import antifraud.persistence.service.LoggingController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    @Autowired
    private LoggingController log;
    public User toUser(UserDto userDto, int size) {
        User user = new User();
        user.setRole("ROLE_MERCHANT");
        user.setLocked(true);
        user.setUsername(userDto.getUsername().toLowerCase());
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        if (size == 0) {
            user.setRole("ROLE_ADMINISTRATOR");
            user.setLocked(false);
            log.info("Admin created: " + user);
        }
        return user;
    }
}
