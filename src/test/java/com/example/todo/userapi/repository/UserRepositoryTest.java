package com.example.todo.userapi.repository;

import com.example.todo.userapi.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원가입 테스트")
    void saveTest() {
        //given
        User user = User.builder()
                .email("qwer@qwe.com")
                .password("1234")
                .userName("망망망")
                .build();
        //when
        User saved = userRepository.save(user);
        //then
        assertNotNull(saved);
    }

    @Test
    @DisplayName("이메일로 회원조회하기")
    void findEmailTest() {
        //given
        String email = "qwer@qwe.com";
        //when
        Optional<User> userOptional = userRepository.findByEmail(email);
        //then
        assertTrue(userOptional.isPresent());
        User user = userOptional.get();
        assertEquals("망망망",user.getUserName());

        System.out.println("\n\n\n");
        System.out.println("user = " + user);
        System.out.println("\n\n\n");

    }

    @Test
    @DisplayName("이메일 중복체크를 하면 중복이 되어야한다")
    void emailTest() {
        //given
        String email = "qwer@qwe.com";
        //when
        boolean flag = userRepository.existsByEmail(email);
        //then
        assertTrue(flag);
    }

    @Test
    @DisplayName("이메일 중복체크를 하면 중복이 flase한다")
    void emailFalseTest() {
        //given
        String email = "qweqqr@qwe.com";
        //when
        boolean flag = userRepository.existsByEmail(email);
        //then
        assertFalse(flag);
    }

}