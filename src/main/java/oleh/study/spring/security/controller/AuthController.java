package oleh.study.spring.security.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oleh.study.spring.security.entity.Otp;
import oleh.study.spring.security.entity.User;
import oleh.study.spring.security.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@AllArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/user/add")
    public void addUser(@RequestBody User user) {
        userService.addUser(user);
        log.info("User " + user + " was added to db");
    }

    @PostMapping("/user/auth")
    public void auth(@RequestBody User user) {
        userService.auth(user);
        log.info("User " + user + " was authenticated");
    }

    @PostMapping("/otp/check")
    public void check(@RequestBody Otp otp, HttpServletResponse response) {
        if (userService.check(otp)) {
            response.setStatus(HttpServletResponse.SC_OK);
            log.info("otp is valid");
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            log.info("otp is not valid");
        }
    }
}
