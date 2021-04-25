package oleh.study.spring.security.repository;

import oleh.study.spring.security.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, String> {

    Optional<Otp> findByUsername(String username);
}
