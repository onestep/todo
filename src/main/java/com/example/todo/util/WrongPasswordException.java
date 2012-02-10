package com.example.todo.util;

/**
 *
 * @author Val
 */
public class WrongPasswordException extends Exception {

    public WrongPasswordException() {
        super();
    }

    public WrongPasswordException(String message) {
        super(message);
    }
}
