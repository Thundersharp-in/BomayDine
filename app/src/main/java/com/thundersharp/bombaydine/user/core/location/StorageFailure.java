package com.thundersharp.bombaydine.user.core.location;

public class StorageFailure extends Exception{

    public StorageFailure(String message){
        super(message);
    }

    public StorageFailure(String message,Throwable c){
        super(message,c);
    }
}
