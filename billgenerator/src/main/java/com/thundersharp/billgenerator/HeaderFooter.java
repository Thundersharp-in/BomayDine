package com.thundersharp.billgenerator;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;


public class HeaderFooter extends PdfPageEventHelper {

    private PdfPTable footer;

    public HeaderFooter(PdfPTable footer) {
        this.footer = footer;
    }
    public void onEndPage(PdfWriter writer, Document document) {
        footer.writeSelectedRows(0, -1, 36, 64, writer.getDirectContent());

    }

}
