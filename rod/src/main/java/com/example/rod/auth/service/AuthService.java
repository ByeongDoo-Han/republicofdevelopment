package com.example.rod.auth.service;

import com.example.rod.auth.dto.SigninRequestDto;
import com.example.rod.auth.dto.SignupRequestDto;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


public interface AuthService {


    void signUp(SignupRequestDto signupRequestDto);

    void signIn(SigninRequestDto signinRequestDto, HttpServletResponse response);

    Optional<Object> findByProviderAndOauthEmail(String provider, String email);

//    void validatePassword(String password, String encodedPassword);
}
