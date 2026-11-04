package com.site.churaibe.domain.user.repository;

import com.site.churaibe.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    // 닉네임 변경 시 본인 닉네임은 중복으로 보지 않기 위해 사용 (대소문자만 바꾸는 경우 포함)
    boolean existsByNicknameAndIdNot(String nickname, Long id);
}
