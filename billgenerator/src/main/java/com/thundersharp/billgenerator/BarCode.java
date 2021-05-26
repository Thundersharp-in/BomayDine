package com.thundersharp.billgenerator;


import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class BarCode {

    public static Image getBarCodeFromData(String data, PdfWriter pdfWriter){
        BarCode barCode = new BarCode();
        return barCode.generateBarCode(data, pdfWriter);
    }

    public static Image geQrCodeFromData(String data) throws BadElementException {
        BarCode barCode = new BarCode();
        return barCode.generateQr(data);
    }

    private Image generateBarCode(String data, PdfWriter pdfWriter){
        PdfContentByte pdfContentByte = pdfWriter.getDirectContent();
        BarcodeEAN barcodeEAN = new BarcodeEAN();
        barcodeEAN.setCodeType(BarcodeEAN.EAN13);
        barcodeEAN.setCode(data);
        Image codeEANImage = barcodeEAN.createImageWithBarcode(pdfContentByte, null, null);
        codeEANImage.scaleAbsoluteWidth(220);
        codeEANImage.setAbsolutePosition(20, 900);
        codeEANImage.scalePercent(100);
        return codeEANImage;
    }

    private Image generateQr(String data) throws BadElementException {
        BarcodeQRCode barcodeQrcode = new BarcodeQRCode(data, 7, 7, null);
        Image qrcodeImage = barcodeQrcode.getImage();
        qrcodeImage.setAbsolutePosition(20, 500);
        qrcodeImage.scalePercent(100);
        return qrcodeImage;
    }

}
