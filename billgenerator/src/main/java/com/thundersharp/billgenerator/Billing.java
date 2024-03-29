package com.thundersharp.billgenerator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Billing {

    @SuppressLint("StaticFieldLeak")
    private static Billing billing;
    public static Activity context;

    public static InvoiceGenerateObserver invoiceGenerateObserver;
    public static InfoData infoData;

    public static Billing initializeBiller(Activity activityContext) {
        billing = new Billing();
        context = activityContext;
        return billing;
    }

    public Billing setInfoData(InfoData infoData) {
        Billing.infoData = infoData;
        return billing;
    }

    public Billing attachObserver(InvoiceGenerateObserver invoiceGenerateObserver) {
        Billing.invoiceGenerateObserver = invoiceGenerateObserver;
        return billing;
    }

    public void createPdf(ArrayList<InvoiceTableHolder> data) throws Exception {
        if (billing == null)
            throw new Exception("initializeBiller() failed.");
        else if (invoiceGenerateObserver == null)
            throw new Exception("Observer not attached.");
        else if (infoData == null)
            throw new Exception("setInfoData() not called.");
        else
            new BillCore().doInBackground(data);
    }

}

class BillCore extends AsyncTask<ArrayList<InvoiceTableHolder>, String, Integer> {

    private static BaseColor printPrimary = new BaseColor(0, 133, 119);//A = 1
    private static BaseColor printPrimary1 = new BaseColor(60, 92, 195);//A = 1
    private static BaseColor textColorp = new BaseColor(43, 43, 43);
    private static BaseColor cutomborder = new BaseColor(57, 57, 57);

    private int itemCounter = 0;
    private double totalprice;

    //Environment.getExternalStorageDirectory()
    public static String FOLDER_PDF = Environment.getExternalStorageDirectory().getPath() + File.separator + "BombayDine/Orders";


    String path = FOLDER_PDF + "/BD"+Billing.infoData.getOrderId() + ".pdf";
    PdfWriter writer;

    @SuppressLint("WrongThread")
    @Override
    protected Integer doInBackground(ArrayList<InvoiceTableHolder>... arrayLists) {

        /**
         * Creating Document for report
         */

        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont("res/font/montserratregular.ttf", "UTF-8", BaseFont.EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        Font regularHead = new Font(baseFont, 11, Font.BOLD, BaseColor.WHITE);
        Font regularReport = new Font(baseFont, 11, Font.BOLD, BaseColor.BLACK);
        Font regularHeadquote = new Font(baseFont, 7, Font.NORMAL, textColorp);
        Font regularHeadquotel = new Font(baseFont, 7, Font.BOLD, BaseColor.BLACK);
        Font boldHeadSmall = new Font(baseFont, 7, Font.BOLD, BaseColor.BLACK);
        Font itemFont = new Font(baseFont, 7, Font.NORMAL, BaseColor.BLACK);


        Rectangle rectangle = new Rectangle(227, 1440);
        //A7 = 74mm in size
        Document document = new Document(rectangle, 3, 3, 15, 15);
        document.addCreationDate();
        document.addAuthor("Thundersharp");
        document.addCreator("thundersharp.in");

        try {

            writer = PdfWriter.getInstance(document, new FileOutputStream(path));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            Log.d("msg", e.getCause().getMessage());
        }


        document.open();

        onProgressUpdate("Please wait...");


        //5

        CustomDashedLineSeperator customDashedLineSeperator = new CustomDashedLineSeperator();
        customDashedLineSeperator.setDash(10);
        customDashedLineSeperator.setGap(7);
        customDashedLineSeperator.setLineWidth(3);


        PdfPCell line = new PdfPCell();
        line.setPaddingTop(5);
        line.setMinimumHeight(5);
        line.setBorder(Rectangle.BOTTOM);
        line.setBorderColorBottom(cutomborder);
        line.setBorderWidthBottom(0.5f);


        PdfPCell preReport = new PdfPCell(new Phrase("Bombay dine resturant", regularReport));

        preReport.setPaddingTop(5);
        preReport.setHorizontalAlignment(Element.ALIGN_CENTER);
        preReport.setBorder(Rectangle.NO_BORDER);


        PdfPCell masci = new PdfPCell(new Phrase("helpdesk@bombaydine.in", regularHeadquote));

        masci.setPaddingTop(5);
        masci.setHorizontalAlignment(Element.ALIGN_CENTER);
        masci.setBorder(Rectangle.NO_BORDER);


        PdfPCell mascig = new PdfPCell(new Phrase("GSTN: 20AAQFT3191F1Z7", regularHeadquote));

        mascig.setPaddingTop(2);
        mascig.setHorizontalAlignment(Element.ALIGN_CENTER);
        mascig.setBorder(Rectangle.NO_BORDER);


        PdfPCell quotationhead = new PdfPCell(new Phrase("Invoice", regularReport));


        quotationhead.setPaddingTop(3);
        quotationhead.setHorizontalAlignment(Element.ALIGN_CENTER);
        quotationhead.setBorder(Rectangle.NO_BORDER);


        PdfPCell footeraddress = new PdfPCell(new Phrase(Resturant.resturantAddress, regularHeadquote));
        PdfPCell footerEmail = new PdfPCell(new Phrase("Phone :" + Resturant.resturantcontact, regularHeadquote));


        footeraddress.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
        footeraddress.setPaddingTop(5);
        footeraddress.setBorder(Rectangle.NO_BORDER);


        footerEmail.setBorder(Rectangle.NO_BORDER);
        footerEmail.setHorizontalAlignment(Element.ALIGN_CENTER);
        footerEmail.setPaddingTop(2);


        PdfPTable tableHeader = new PdfPTable(1);


//        try {
//            tableHeader.setWidths(new float[]{1, 3});
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }

        try {
            Drawable d = Billing.context.getDrawable(Billing.infoData.getLogo());
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 60, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.scaleToFit(40, 40);
            PdfPCell preImage = new PdfPCell(image, false);
            //preImage.setVerticalAlignment(Element.ALIGN_TOP);
            preImage.setHorizontalAlignment(Element.ALIGN_CENTER);
            preImage.setBorder(Rectangle.NO_BORDER);

            //TODO UPDATE DIRECTLY LAYOUT
            tableHeader.addCell(preImage);
            tableHeader.addCell(preReport);

            tableHeader.addCell(line);

            tableHeader.addCell(masci);
            tableHeader.addCell(mascig);

            tableHeader.addCell(line);

            tableHeader.addCell(footeraddress);
            tableHeader.addCell(footerEmail);

            tableHeader.addCell(line);

            tableHeader.addCell(quotationhead);
            //tableHeader.addCell(pdfPCell);

            document.add(tableHeader);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }


        PdfPTable tableHeading = new PdfPTable(2);
        tableHeading.setSpacingBefore(0);


        PdfPCell preAddress = new PdfPCell(new Phrase("Delivered to :" + Billing.infoData.getClientAddress(), regularHeadquotel));
        PdfPCell prePhone = new PdfPCell(new Phrase("Phone: " + Billing.infoData.getClientPhone(), regularHeadquotel));

        PdfPCell preBill = new PdfPCell(new Phrase("Bill no. #" + Billing.infoData.getOrderId(), regularHeadquotel));

        PdfPCell preDate = new PdfPCell(new Phrase("Bill Date\n" + TimeUtilities.getTimeFromTimeStamp(Billing.infoData.getOrderId()), regularHeadquotel));

        PdfPCell lines = new PdfPCell();
        lines.setColspan(2);
        lines.setPaddingTop(5);
        lines.setMinimumHeight(5);
        lines.setBorder(Rectangle.BOTTOM);
        lines.setBorderColorBottom(cutomborder);
        lines.setBorderWidthBottom(0.5f);


        preBill.setHorizontalAlignment(Element.ALIGN_LEFT);
        preDate.setHorizontalAlignment(Element.ALIGN_RIGHT);

        preAddress.setBorder(Rectangle.NO_BORDER);
        preAddress.setPaddingTop(3);
        prePhone.setPaddingTop(3);
        preAddress.setColspan(2);
        prePhone.setColspan(2);
        prePhone.setBorder(Rectangle.NO_BORDER);
        preDate.setBorder(Rectangle.NO_BORDER);
        preBill.setBorder(Rectangle.NO_BORDER);

        try {
            tableHeading.addCell(preBill);
            tableHeading.addCell(preDate);

            tableHeading.addCell(preAddress);

            tableHeading.addCell(prePhone);

            tableHeading.addCell(lines);

            document.add(tableHeading);
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        PdfPTable table = new PdfPTable(5);
        table.setSpacingBefore(4);
        try {
            table.setWidths(new float[]{0.7f, 3.2f, 0.8f, 1.6f, 1.6f});
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        table.setHeaderRows(1);

        table.setSplitRows(false);
        table.setComplete(false);


        PdfPCell headDate = new PdfPCell(new Phrase("No", boldHeadSmall));
        PdfPCell headName = new PdfPCell(new Phrase("Particulars", boldHeadSmall));
        PdfPCell headDis = new PdfPCell(new Phrase("Qty", boldHeadSmall));
        PdfPCell headCr = new PdfPCell(new Phrase("Value", boldHeadSmall));
        PdfPCell headDe = new PdfPCell(new Phrase("Rate", boldHeadSmall));


        headDate.setPaddingBottom(5);
        headDate.setVerticalAlignment(Element.ALIGN_LEFT);


        headName.setPaddingBottom(7);
        headName.setVerticalAlignment(Element.ALIGN_CENTER);


        headDis.setPaddingBottom(7);
        headDis.setVerticalAlignment(Element.ALIGN_CENTER);

        headCr.setPaddingBottom(7);
        headCr.setHorizontalAlignment(Element.ALIGN_CENTER);
        headCr.setVerticalAlignment(Element.ALIGN_CENTER);


        headDe.setPaddingBottom(7);
        headDe.setHorizontalAlignment(Element.ALIGN_CENTER);
        headDe.setVerticalAlignment(Element.ALIGN_CENTER);


        headDate.setBorder(Rectangle.NO_BORDER);

        headName.setBorder(Rectangle.NO_BORDER);
        headDis.setBorder(Rectangle.NO_BORDER);
        headCr.setBorder(Rectangle.NO_BORDER);
        headDe.setBorder(Rectangle.NO_BORDER);


        table.addCell(headDate);
        table.addCell(headName);
        table.addCell(headDis);
        table.addCell(headDe);
        table.addCell(headCr);


        Double amountFull = 0.0;

        for (int aw = 0; aw < arrayLists[0].size(); aw++) {

            InvoiceTableHolder listItem = arrayLists[0].get(aw);

            onProgressUpdate(String.valueOf(aw) + " items processing...");

            itemCounter += listItem.getQty();

            PdfPCell cellNo = new PdfPCell(new Phrase(String.valueOf(aw + 1), boldHeadSmall));
            PdfPCell cellName = new PdfPCell(new Phrase(listItem.getName().toLowerCase(), boldHeadSmall));
            PdfPCell cellCompany = new PdfPCell(new Phrase(String.valueOf(listItem.getQty()), boldHeadSmall));
            PdfPCell celldebit = new PdfPCell(new Phrase(String.valueOf(listItem.getUnitprice()), boldHeadSmall));
            PdfPCell cellAmount = new PdfPCell(new Phrase("" + (listItem.getQty() * listItem.getUnitprice()), boldHeadSmall));

            amountFull = (listItem.getQty() * listItem.getUnitprice());
            totalprice += amountFull;


            if (aw == 0) {
                cellNo.setBorder(Rectangle.TOP);
                cellName.setBorder(Rectangle.TOP);
                celldebit.setBorder(Rectangle.TOP);
                cellCompany.setBorder(Rectangle.TOP);
                cellAmount.setBorder(Rectangle.TOP);


                cellNo.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellNo.setVerticalAlignment(Element.ALIGN_CENTER);


                cellName.setVerticalAlignment(Element.ALIGN_MIDDLE);


                celldebit.setHorizontalAlignment(Element.ALIGN_CENTER);
                celldebit.setVerticalAlignment(Element.ALIGN_CENTER);

                cellCompany.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellCompany.setVerticalAlignment(Element.ALIGN_CENTER);


                cellAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellAmount.setVerticalAlignment(Element.ALIGN_CENTER);
            } else {

                cellNo.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellNo.setVerticalAlignment(Element.ALIGN_MIDDLE);


                cellName.setVerticalAlignment(Element.ALIGN_MIDDLE);


                celldebit.setHorizontalAlignment(Element.ALIGN_CENTER);

                cellCompany.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellCompany.setVerticalAlignment(Element.ALIGN_MIDDLE);


                cellAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellAmount.setVerticalAlignment(Element.ALIGN_MIDDLE);


                cellNo.setBorder(Rectangle.NO_BORDER);
                cellName.setBorder(Rectangle.NO_BORDER);
                celldebit.setBorder(Rectangle.NO_BORDER);
                cellCompany.setBorder(Rectangle.NO_BORDER);
                cellAmount.setBorder(Rectangle.NO_BORDER);

            }

            table.addCell(cellNo);
            table.addCell(cellName);
            table.addCell(cellCompany);
            table.addCell(celldebit);
            table.addCell(cellAmount);
        }

        PdfPTable bottomRow = new PdfPTable(3);

        try {
            bottomRow.setWidths(new float[]{3, 3, 1.9f});
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        bottomRow.setSplitRows(false);
        bottomRow.setComplete(false);

        PdfPCell items = new PdfPCell(new Phrase("Items " + arrayLists[0].size(), boldHeadSmall));
        PdfPCell Qty = new PdfPCell(new Phrase("Qty " + itemCounter, boldHeadSmall));
        PdfPCell total = new PdfPCell(new Phrase("Rs " + totalprice, boldHeadSmall));


        items.setBorder(Rectangle.TOP);
        Qty.setBorder(Rectangle.TOP);
        total.setBorder(Rectangle.TOP);


        items.setPaddingTop(5);
        Qty.setPaddingTop(5);
        total.setPaddingTop(5);

        items.setPaddingBottom(5);
        Qty.setPaddingBottom(5);
        total.setPaddingBottom(5);

        total.setHorizontalAlignment(Element.ALIGN_CENTER);

        bottomRow.addCell(items);
        bottomRow.addCell(Qty);
        bottomRow.addCell(total);
        bottomRow.setComplete(true);




            /*
              Created a new table here
              @new
             */
        PdfPTable table1 = new PdfPTable(5);
        try {
            table1.setWidths(new float[]{1f, 2.7f, 1.5f, 1.5f, 1.9f});
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        table1.setSplitRows(false);
        table1.setComplete(false);


        PdfPCell delivery = new PdfPCell(new Phrase("Delivery charges", boldHeadSmall));
        PdfPCell deliverycharge = new PdfPCell(new Phrase("FREE", boldHeadSmall));

        delivery.setPaddingTop(2);
        deliverycharge.setPaddingTop(2);
        delivery.setPaddingBottom(2);
        deliverycharge.setPaddingBottom(2);

        delivery.setBorder(Rectangle.TOP);
        deliverycharge.setBorder(Rectangle.TOP);
        delivery.setHorizontalAlignment(Element.ALIGN_RIGHT);
        deliverycharge.setHorizontalAlignment(Element.ALIGN_RIGHT);
        delivery.setColspan(3);
        deliverycharge.setColspan(2);
        delivery.setPaddingRight(10);

        PdfPCell subTotal = new PdfPCell(new Phrase(Billing.infoData.getPromoName(), boldHeadSmall));
        PdfPCell subtotalamt = new PdfPCell(new Phrase("- Rs " + Billing.infoData.getDiscAmt(), boldHeadSmall));

        subTotal.setPaddingTop(2);
        subtotalamt.setPaddingTop(2);
        subTotal.setBorder(Rectangle.TOP);
        subtotalamt.setBorder(Rectangle.TOP);
        subtotalamt.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subTotal.setVerticalAlignment(Element.ALIGN_CENTER);
        subTotal.setPaddingRight(10);
        subTotal.setColspan(3);
        subtotalamt.setColspan(2);

        subTotal.setPaddingBottom(2);
        subtotalamt.setPaddingBottom(2);


        PdfPCell preTotal = new PdfPCell(new Phrase("TOTAL", boldHeadSmall));
        PdfPCell preTotalAmount = new PdfPCell(new Phrase("Rs " + (amountFull - Billing.infoData.getDiscAmt()), boldHeadSmall));

        preTotalAmount.setPaddingLeft(10);
        preTotalAmount.setBorder(Rectangle.TOP);
        preTotalAmount.setHorizontalAlignment(Element.ALIGN_RIGHT);
        preTotal.setBorder(Rectangle.TOP);
        preTotal.setVerticalAlignment(Element.ALIGN_CENTER);
        preTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        preTotal.setPaddingRight(15);
        preTotal.setColspan(3);
        preTotalAmount.setColspan(2);

        preTotal.setPaddingTop(2);
        preTotalAmount.setPaddingTop(2);
        preTotal.setPaddingBottom(7);
        preTotalAmount.setPaddingBottom(7);

        PdfPCell terms = new PdfPCell(new Phrase("* Terms : " + Billing.infoData.getTerms() + "\nAll prices in the bill are inclusive of all taxes.", regularHeadquote));
        terms.setPadding(5);
        terms.setColspan(2);
        terms.setBorder(Rectangle.TOP);

        //BarCode.getBarCodeFromData(infoData.getOrderId(),writer);

        PdfPCell barCode = new PdfPCell(BarCode.getBarCodeFromData(Billing.infoData.getOrderId(),writer));
        barCode.setBorder(Rectangle.NO_BORDER);
        barCode.setPaddingTop(20);
        barCode.setColspan(2);
        barCode.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell qrCode = null;
        try {
            qrCode = new PdfPCell(BarCode.geQrCodeFromData("Download from : https://play.google.com/store/apps/details?id=" + Billing.context.getPackageName()));
        } catch (BadElementException e) {
            e.printStackTrace();
        }
        qrCode.setBorder(Rectangle.NO_BORDER);
        qrCode.setPaddingTop(20);
        qrCode.setPaddingRight(10);
        qrCode.setHorizontalAlignment(Element.ALIGN_RIGHT);


        PdfPCell qrdesc = new PdfPCell(new Phrase("Scan the Qr code to get the application now from the android store", regularHeadquote));
        qrdesc.setBorder(Rectangle.NO_BORDER);
        qrdesc.setPaddingTop(20);
        qrdesc.setVerticalAlignment(Rectangle.ALIGN_CENTER);
        qrdesc.setHorizontalAlignment(Rectangle.LEFT);

        PdfPTable bottomTerms = new PdfPTable(2);
        try {
            bottomTerms.setWidths(new float[]{1, 1});
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        bottomTerms.addCell(terms);
        bottomTerms.addCell(barCode);
        if (qrCode != null) {
            bottomTerms.addCell(qrCode);
            bottomTerms.addCell(qrdesc);
        }


        table.setComplete(true);

        table1.addCell(delivery);
        table1.addCell(deliverycharge);

        table1.addCell(subTotal);
        table1.addCell(subtotalamt);


        table1.addCell(preTotal);
        table1.addCell(preTotalAmount);


        //table1.addCell(quotedesc);


        //table1.addCell(blankspace);
        //table1.addCell(sign);

        table1.setComplete(true);


        try {
            document.add(table);
            document.add(bottomRow);
            document.add(table1);
            document.add(bottomTerms);

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        onProgressUpdate("Finishing up...");
        document.close();
        writer.close();
        onPostExecute(1);
        return 1;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("XXX", "Pre execute");

        File file = new File(FOLDER_PDF);
        if (!file.exists()) {
            file.mkdir();
        }

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        final String a = values[0].toString();
        Log.e("UP ", a);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        File file = new File(path);
        Uri pdfURI = FileProvider.getUriForFile(Billing.context, Billing.context.getPackageName() + ".provider", file);
        //path1 = pdfURI;
        Billing.invoiceGenerateObserver.pdfCreatedSuccess(pdfURI);

        Billing.invoiceGenerateObserver.pdfCreatedSuccess(pdfURI);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(pdfURI, "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


        Intent intent = Intent.createChooser(target, "Open File");
        try {
            Billing.context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }





        //File file = new File(path);
        //Uri pdfURI = FileProvider.getUriForFile(Billing.context, Billing.context.getPackageName() + ".provider", file);
        //Log.e("Path", pdfURI.getPath());
        //Billing.invoiceGenerateObserver.pdfCreatedSuccess(pdfURI);
        // path1 = pdfURI;
        //uploadtofirebase(QUOTATION_NUMBER, CLIENT_NAME, CLIENT_ADDRESS, VALID_FROM, VALID_TILL, itemholder);
        // target.setDataAndType(Uri.fromFile(file),"application/pdf");

        //txtUpdate.setText("Finished");

 /*       Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }*/

    }
}

