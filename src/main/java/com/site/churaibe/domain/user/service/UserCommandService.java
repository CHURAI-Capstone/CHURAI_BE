package com.site.churaibe.domain.user.service;

import com.site.churaibe.domain.user.converter.UserConverter;
import com.site.churaibe.domain.user.dto.request.UserReqDTO;
import com.site.churaibe.domain.user.dto.response.UserResDTO;
import com.site.churaibe.domain.user.entity.User;
import com.site.churaibe.domain.user.exception.UserException;
import com.site.churaibe.domain.user.exception.code.error.UserErrorCode;
import com.site.churaibe.domain.user.repository.UserRepository;
import com.site.churaibe.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserResDTO.SignUpResDTO signUp(UserReqDTO.SignUpDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }

        User user = UserConverter.toUser(dto, passwordEncoder.encode(dto.password()));
        userRepository.saveAndFlush(user);

        return UserConverter.toSignUpResDTO(user);
    }

    @Transactional(readOnly = true)
    public UserResDTO.LoginResDTO login(UserReqDTO.LoginDTO dto) {
        // 이메일 존재 여부와 비밀번호 불일치를 구분하지 않고 동일한 에러로 응답
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new UserException(UserErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new UserException(UserErrorCode.LOGIN_FAILED);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        return UserConverter.toLoginResDTO(user, accessToken);
    }
}
