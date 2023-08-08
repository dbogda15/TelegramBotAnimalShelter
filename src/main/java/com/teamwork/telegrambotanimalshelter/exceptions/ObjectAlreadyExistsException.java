package com.teamwork.telegrambotanimalshelter.exceptions;

public class ObjectAlreadyExistsException extends RuntimeException{
    public ObjectAlreadyExistsException(String message){
        super(message);
    }
}
