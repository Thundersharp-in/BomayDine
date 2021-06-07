package com.thundersharp.admin.core.Errors;

public class ModuleError extends Error {

    public ModuleError(String message){
        super(message);
    }

    public ModuleError(String message, Throwable cause){
        super(message, cause);
    }

}
