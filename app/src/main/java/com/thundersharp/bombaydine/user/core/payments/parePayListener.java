package com.thundersharp.bombaydine.user.core.payments;

public interface parePayListener{

    void addSuccess();

    void addFailure(Exception exception);

}
