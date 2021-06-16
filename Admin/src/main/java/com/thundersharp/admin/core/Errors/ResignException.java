package com.thundersharp.admin.core.Errors;

public class ResignException extends Exception{

    public ResignException(String exception){
        super(exception);
    }

    public ResignException(String message,Throwable throwable){
        super(message,throwable);
    }
}
