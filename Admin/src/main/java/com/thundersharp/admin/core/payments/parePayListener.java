package com.thundersharp.admin.core.payments;

public interface parePayListener{

    void addSuccess();

    void addFailure(Exception exception);

}
