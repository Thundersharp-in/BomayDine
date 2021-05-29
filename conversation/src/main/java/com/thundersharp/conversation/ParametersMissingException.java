package com.thundersharp.conversation;

public class ParametersMissingException extends Exception {

    public ParametersMissingException(String message){
        super(message);
    }

    public ParametersMissingException(String message,Throwable cause){
        super(message, cause);
    }
}
