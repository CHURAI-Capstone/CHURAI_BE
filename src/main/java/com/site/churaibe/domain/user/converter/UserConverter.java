package com.site.churaibe.domain.user.converter;

import com.site.churaibe.domain.user.dto.request.UserReqDTO;
import com.site.churaibe.domain.user.dto.response.UserResDTO;
import com.site.churaibe.domain.user.entity.User;

public class UserConverter {

    public static User toUser(UserReqDTO.SignUpDTO dto, String encodedPassword) {
        return User.builder()
                .email(dto.email())
                .password(encodedPassword)
                .nickname(dto.nickname())
                .build();
    }

    public static UserResDTO.SignUpResDTO toSignUpResDTO(User user) {
        return UserResDTO.SignUpResDTO.builder()
                .userId(user.getId())
                .build();
    }

    public static UserResDTO.LoginResDTO toLoginResDTO(User user, String accessToken) {
        return UserResDTO.LoginResDTO.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .build();
    }

    public static UserResDTO.MyInfoResDTO toMyInfoResDTO(User user) {
        return UserResDTO.MyInfoResDTO.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
