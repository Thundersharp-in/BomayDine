package com.thundersharp.bombaydine.user.core.Data;

import java.util.List;

public interface OfferListner {

    void fetchAllOffers();

    interface getOfferListner{
        void OnGetOfferSuccess(List<Object> data);
        void OnOfferFetchFailure(Exception e);
    }
}
