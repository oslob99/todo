package com.example.todo.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoRegisterArgumentException extends RuntimeException{

    // 기본 생성자 + 에러메세지를
    public NoRegisterArgumentException(String message){
        super(message);
    }
}
