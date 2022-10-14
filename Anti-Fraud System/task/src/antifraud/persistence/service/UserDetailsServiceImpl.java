package antifraud.persistence.service;

import antifraud.business.User;
import antifraud.business.UserDetailsImpl;
import antifraud.business.dto.UsernameRole;
import antifraud.business.dto.UsernameStatus;
import antifraud.persistence.UserRepository;
import antifraud.business.response.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    private LoggingController log;

    @Autowired
    public UserDetailsServiceImpl (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username.toLowerCase());

        if (user == null) {
            throw new UsernameNotFoundException("Not found: " + username);
        }

        return new UserDetailsImpl(user);
    }

    public User save(User user) {
        userRepository.save(user);
        return userRepository.findUserByUsername(user.getUsername());
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username.toLowerCase());
    }

    public User updateUserByUsername(Optional<UsernameRole> body1, Optional<UsernameStatus> body2) {
        if (body1.isPresent()) {
            UsernameRole body = body1.get();
            if (!body.getRole().equals("SUPPORT") && !body.getRole().equals("MERCHANT")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            User user = userRepository.findUserByUsername(body.getUsername().toLowerCase());
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            if (user.getStrRole().equals(body.getRole())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }

            user.setRole("ROLE_" + body.getRole());
            userRepository.save(user);
            log.info("User role changed: " + user.getRole());
            return userRepository.findUserByUsername(body.getUsername().toLowerCase());
        } else {
            UsernameStatus body = body2.get();

            User user = userRepository.findUserByUsername(body.getUsername().toLowerCase());
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            if (user.getStrRole().equals("ADMINISTRATOR")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            user.setLocked(body.getOperation().equals("LOCK"));
            userRepository.save(user);
            log.info("User status changed: locked-" + user.isLocked());
            return userRepository.findUserByUsername(body.getUsername());
        }
    }

    public Delete deleteUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username.toLowerCase());

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        userRepository.deleteUserByUsername(username.toLowerCase());
        return new Delete(username, "Deleted successfully!");
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
