package com.animal.animalservice.exception;

public class AnimalProfileNotFoundException extends RuntimeException{

    public AnimalProfileNotFoundException(String message) {
        super(message);
    }
}
