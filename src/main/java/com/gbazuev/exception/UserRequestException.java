package com.gbazuev.exception;

public class UserRequestException extends Exception {
    public UserRequestException(String userRequest) {
        super("\"" + userRequest + "\" - incorrect request!");
    }
}
