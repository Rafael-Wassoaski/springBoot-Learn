package com.springboot.rafael.exception;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(){
        super("Usuário ou senha incorretos");
    }
    
}
