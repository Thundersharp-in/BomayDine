package com.thundersharp.admin.ui.RestStatus;

import androidx.annotation.NonNull;

public interface RestStatus {

    void isOpen();

    void setRestStatus(boolean isOpen);

    interface updateRestStatus{
        void onSuccess(@NonNull Boolean isOpen);
        void onFailure(Exception e);
    }
}
