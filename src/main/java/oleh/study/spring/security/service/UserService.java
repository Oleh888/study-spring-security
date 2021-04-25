package oleh.study.spring.security.service;

import lombok.AllArgsConstructor;
import oleh.study.spring.security.entity.Otp;
import oleh.study.spring.security.entity.User;
import oleh.study.spring.security.repository.OtpRepository;
import oleh.study.spring.security.repository.UserRepository;
import oleh.study.spring.security.util.GenerateCodeUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void auth(User user) {
        Optional<User> userFromDb = userRepository
                .findByUsername(user.getUsername());

        if (userFromDb.isPresent()) {
            User u = userFromDb.get();

            if (passwordEncoder.matches(user.getPassword(), u.getPassword())) {
                renewOtp(u);
            } else {
                throw new BadCredentialsException("Bad credentials.");
            }
        } else {
            throw new BadCredentialsException("Bad credentials.");
        }
    }

    private void renewOtp(User user) {
        String code = GenerateCodeUtil.generateCode();

        Optional<Otp> userOtp = otpRepository.findByUsername(user.getUsername());

        if (userOtp.isPresent()) {
            Otp otp = userOtp.get();
            otp.setCode(code);
        } else {
            Otp otp = new Otp(user.getUsername(), code);
            otpRepository.save(otp);
        }
    }

    public boolean check(Otp otpToValidate) {
        Optional<Otp> userOtp = otpRepository.findByUsername(otpToValidate.getUsername());

        if (userOtp.isPresent()) {
            Otp otp = userOtp.get();

            if (otpToValidate.getCode().equals(otp.getCode())) {
                return true;
            }
        }

        return false;
    }
}
