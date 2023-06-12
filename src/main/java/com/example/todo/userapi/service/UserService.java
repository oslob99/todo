package com.example.todo.userapi.service;

import com.example.todo.auth.TokenProvider;
import com.example.todo.userapi.dto.request.LoginRequestDTO;
import com.example.todo.userapi.dto.request.UserRequestSignUpDTO;
import com.example.todo.userapi.dto.response.LoginResponseDTO;
import com.example.todo.userapi.dto.response.UserSignUpResponseDTO;
import com.example.todo.userapi.entity.User;
import com.example.todo.userapi.repository.UserRepository;
import com.sun.xml.bind.v2.runtime.output.Encoded;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final TokenProvider tokenProvider;

    // 회원가입 처리
    public UserSignUpResponseDTO create(final UserRequestSignUpDTO dto){


        if (dto == null){
            throw new RuntimeException("가입정보가 없습니다");
        }
        String email = dto.getEmail();

//        if (){}

        if (userRepository.existsByEmail(email)){
            log.warn("이메일이 중복되었습니다. - {}",email);
            throw new RuntimeException("가입정보가 없습니다");
        }

        // 패스워드 인코딩
        String encoded = encoder.encode(dto.getPassword());
        dto.setPassword(encoded);

        // 유저 엔터티로 변환
        User user = dto.toEntity();
        User saved = userRepository.save(user);

        log.info("회원가입 정상 수행됨! -saved user - {}",saved);

        return new UserSignUpResponseDTO(saved);

    }


    public boolean isDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    // 회원 인증
    public LoginResponseDTO authenticate(final LoginRequestDTO dto){

        // 이메일을 통해 회원 정보 조회
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(
                () -> new RuntimeException("가입된 회원이 아닙니다!")
        );

        // 패스워드 검증
        String rawPassword = dto.getPassword(); // 입력 비번
        String encodedPassword = user.getPassword(); // DB에 저장된 비번
        if (!encoder.matches(rawPassword,encodedPassword)){
            throw new RuntimeException("비밀번호가 틀렸습니다");
        }

        log.info("{}님 로그인 성공!!",user.getUserName());

        // 로그인 성공 후에 클라이언트에 뭘 리턴할 것인가?
        // -> JWT를 클라이언트에게 발급해줘야체
        String token = tokenProvider.createToken(user);

        return new LoginResponseDTO(user, token);

    }


}
