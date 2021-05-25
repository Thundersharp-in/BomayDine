package com.thundersharp.billgenerator;

import android.net.Uri;

public interface InvoiceGenerateObserver {

    void pdfCreatedSuccess(Uri pdfLink);

}
