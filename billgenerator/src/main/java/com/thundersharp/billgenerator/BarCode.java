package com.thundersharp.billgenerator;


import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class BarCode {

    public static Image getBarCodeFromData(String data, PdfWriter pdfWriter){
        BarCode barCode = new BarCode();
        return barCode.generateBarCode(data, pdfWriter);
    }

    public static Bitmap getBarCodeFromData(String data){
        BarCode barCode = new BarCode();
        return barCode.generateBarCode(data);
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
        codeEANImage.scaleAbsoluteWidth(270);
        codeEANImage.setAbsolutePosition(20, 900);
        codeEANImage.scalePercent(100);
        return codeEANImage;
    }

    private Image generateQr(String data) throws BadElementException {
        BarcodeQRCode barcodeQrcode = new BarcodeQRCode(data, 10, 10, null);
        Image qrcodeImage = barcodeQrcode.getImage();
        qrcodeImage.setAbsolutePosition(20, 500);
        qrcodeImage.scalePercent(100);
        return qrcodeImage;
    }

    private Bitmap generateBarCode(String data){
        EAN13Writer multiFormatWriter = new EAN13Writer();
        try {

            BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.EAN_13,600,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }

    }

}
