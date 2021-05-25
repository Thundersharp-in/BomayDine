package com.thundersharp.billgenerator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.itextpdf.text.PageSize;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Billing extends AsyncTask<ArrayList<InvoiceTableHolder>, String, Integer> {

    private static Billing billing;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private InvoiceGenerateObserver invoiceGenerateObserver;
    private InfoData infoData;

    public static Billing initializeBiller(Context activityContext){
        billing = new Billing();
        context = activityContext;
        return billing;
    }

    public Billing setInfoData(InfoData infoData){
        this.infoData = infoData;
        return billing;
    }

    public Billing attachObserver(InvoiceGenerateObserver invoiceGenerateObserver){
        this.invoiceGenerateObserver = invoiceGenerateObserver;
        return billing;
    }

    public void createPdf(ArrayList<InvoiceTableHolder> data) throws Exception{
        if (billing == null) throw new Exception("initializeBiller() failed.");
        else if (invoiceGenerateObserver == null) throw new Exception("Observer not attached.");
        else if (infoData == null) throw new Exception("setInfoData() not called.");
        else billing.doInBackground(data);
    }

    private static BaseColor printPrimary = new BaseColor(0, 133, 119);//A = 1
    private static BaseColor printPrimary1 = new BaseColor(60, 92, 195);//A = 1
    private static BaseColor printAccent = new BaseColor(216, 27, 96);
    private static BaseColor cutomborder = new BaseColor(221, 221, 221);

    private static String FOLDER_PDF= Environment.getExternalStorageDirectory() + File.separator+"Thundersharp/Quotations";


    String path = FOLDER_PDF + "/" + "BILL_NO" + ".pdf";
    PdfWriter writer;

    @SuppressLint("WrongThread")
    @Override
    protected Integer doInBackground(ArrayList<InvoiceTableHolder>... arrayLists) {

        /**
         * Creating Document for report
         */
        //dialog.setCancelable(false);
        //dialog.show();
        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont("res/font/montserratregular.ttf", "UTF-8", BaseFont.EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        Font regularHead = new Font(baseFont, 11, Font.BOLD, BaseColor.WHITE);
        Font regularReport = new Font(baseFont, 23, Font.BOLD, BaseColor.BLACK);
        Font regularHeadquote = new Font(baseFont, 20, Font.BOLD, printAccent);
        Font regularName = new Font(baseFont, 12, Font.BOLD, BaseColor.BLACK);
        Font regularAddress = new Font(baseFont, 10, Font.NORMAL, BaseColor.BLACK);
        Font regularSub = new Font(baseFont, 10);
        Font regularSubbold = new Font(baseFont, 10, Font.BOLD);
        Font smallfooter = new Font(baseFont, 9);
        Font regularTotal = new Font(baseFont, 11, Font.NORMAL, BaseColor.BLACK);
        Font regularTotalBold = new Font(baseFont, 13, Font.BOLD, BaseColor.BLACK);
        Font footerN = new Font(baseFont, 10, Font.BOLD, printAccent);
        Font footerE = new Font(baseFont, 10, Font.NORMAL, BaseColor.BLACK);


        Rectangle rectangle = new Rectangle(250,1440);
        //A7 = 74mm in size
        Document document = new Document(rectangle, 7, 7, 20, 92);
        document.addCreationDate();
        document.addAuthor("Thundersharp");
        document.addCreator("thundersharp.in");
        //document.setMargins(0,0,0,0);

// Location to save
        try {

            writer = PdfWriter.getInstance(document, new FileOutputStream(path));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            Log.d("msg", e.getCause().getMessage());
        }


        PdfPTable tableFooter = new PdfPTable(2);

        tableFooter.setTotalWidth(523);

        //PdfPCell footerName = new PdfPCell(new Phrase("Thundersharp",footerN)

        PdfPCell footerEmail = new PdfPCell(new Phrase("support@thundersharp.in\n+91 7004963142 / +91 9798105830", regularSub));
        PdfPCell footerblank = new PdfPCell(new Phrase("", smallfooter));
        PdfPCell footeraddress = new PdfPCell(new Phrase("#315D Bari Co-operative Bokaro Steel City PIN:827012", regularSub));
        PdfPCell footerreg = new PdfPCell(new Phrase("support@thundersharp.in", smallfooter));


        //PdfPCell footerEmpty = new PdfPCell(new Phrase(""));

        //footerName.setBorder(Rectangle.NO_BORDER);
        //footerEmpty.setBorder(Rectangle.NO_BORDER);
        footerEmail.setBorder(Rectangle.NO_BORDER);
        footeraddress.setBorder(Rectangle.NO_BORDER);
        footeraddress.setVerticalAlignment(Element.ALIGN_TOP);
        footerreg.setBorder(Rectangle.NO_BORDER);
        footerblank.setBorder(Rectangle.NO_BORDER);

        footerEmail.setHorizontalAlignment(Element.ALIGN_RIGHT);
        footerEmail.setVerticalAlignment(Element.ALIGN_TOP);


        PdfPCell preBorderBlue = new PdfPCell(new Phrase("Thundersharp"));
        preBorderBlue.setMinimumHeight(5f);
        preBorderBlue.setUseVariableBorders(true);
        preBorderBlue.setBorder(Rectangle.TOP);
        preBorderBlue.setBorderColorTop(printPrimary);
        preBorderBlue.setBorderWidthTop(3);


        PdfPCell preBorderBlue1 = new PdfPCell(new Phrase("Regn : SEA1935500125217"));

        preBorderBlue1.setMinimumHeight(5f);
        preBorderBlue1.setUseVariableBorders(true);
        preBorderBlue1.setBorder(Rectangle.TOP);
        preBorderBlue1.setBorderColorTop(printPrimary);
        preBorderBlue1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        preBorderBlue1.setBorderWidthTop(3);


        tableFooter.addCell(preBorderBlue);
        tableFooter.addCell(preBorderBlue1);
        //tableFooter.addCell(footerName);


        tableFooter.addCell(footeraddress);
        tableFooter.addCell(footerEmail);


        tableFooter.addCell(footerreg);

        try {
            tableFooter.setWidths(new float[]{1.4f, 2});
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        HeaderFooter event = new HeaderFooter(tableFooter);

        writer.setPageEvent(event);

        document.open();

        onProgressUpdate("Please wait...");


        //5

        PdfPTable pdfPTable = new PdfPTable(1);

        pdfPTable.setSplitRows(false);
        pdfPTable.setComplete(false);


        PdfPCell preReport = new PdfPCell(new Phrase("Thundersharp", regularReport));

        preReport.setHorizontalAlignment(Element.ALIGN_RIGHT);
        preReport.setVerticalAlignment(Element.ALIGN_TOP);
        preReport.setBorder(Rectangle.NO_BORDER);


        PdfPCell tagline = new PdfPCell(new Phrase("Target Future", smallfooter));


        tagline.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tagline.setVerticalAlignment(Element.ALIGN_TOP);
        tagline.setBorder(Rectangle.NO_BORDER);


        PdfPCell masci = new PdfPCell(new Phrase("thundersharp.in\n#GSTN: 20AAQFT3191F1Z7", footerE));

        masci.setPaddingTop(10);
        masci.setHorizontalAlignment(Element.ALIGN_RIGHT);
        masci.setVerticalAlignment(Element.ALIGN_TOP);
        masci.setBorder(Rectangle.NO_BORDER);


        PdfPCell quotationhead = new PdfPCell(new Phrase("Invoice", regularHeadquote));


        quotationhead.setPaddingTop(10);
        quotationhead.setHorizontalAlignment(Element.ALIGN_RIGHT);
        quotationhead.setVerticalAlignment(Element.ALIGN_TOP);
        quotationhead.setBorder(Rectangle.NO_BORDER);


        pdfPTable.addCell(preReport);
        pdfPTable.addCell(tagline);
        pdfPTable.addCell(masci);
        pdfPTable.addCell(quotationhead);

        pdfPTable.setComplete(true);

        PdfPCell pdfPCell = new PdfPCell(pdfPTable);
        pdfPCell.setBorder(Rectangle.NO_BORDER);

        PdfPTable tableHeader = new PdfPTable(2);

        try {
            tableHeader.setWidths(new float[]{1, 3});
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        try {
            Drawable d = context.getDrawable(infoData.getLogo());
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.scaleToFit(120, 120);
            PdfPCell preImage = new PdfPCell(image, false);
            //preImage.setVerticalAlignment(Element.ALIGN_TOP);
            //preImage.setHorizontalAlignment(Element.ALIGN_LEFT);
            preImage.setBorder(Rectangle.NO_BORDER);

            tableHeader.addCell(preImage);
            tableHeader.addCell(pdfPCell);

            document.add(tableHeader);

        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        PdfPTable tableHeading = new PdfPTable(2);
        tableHeading.setSpacingBefore(15);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);



        PdfPCell preName = new PdfPCell(new Phrase(infoData.getClientName(), regularName));
        PdfPCell preAddress = new PdfPCell(new Phrase(infoData.getClientAddress() + "\n\nPhone no: " + infoData.getClientPhone(), regularAddress));
        PdfPCell prePhone = new PdfPCell(new Phrase("Phone: " + infoData.getClientPhone(), regularAddress));

        PdfPCell preBill = new PdfPCell(new Phrase("Bill no. #" + infoData.getOrderId(), regularAddress));

        PdfPCell preDate = new PdfPCell(new Phrase("Bill Date: "  + "\nDue Date: " , regularTotal));

        preBill.setVerticalAlignment(Element.ALIGN_BOTTOM);
        preBill.setHorizontalAlignment(Element.ALIGN_RIGHT);

        preDate.setVerticalAlignment(Element.ALIGN_BOTTOM);
        preDate.setHorizontalAlignment(Element.ALIGN_RIGHT);
        preName.setBorder(Rectangle.NO_BORDER);
        preAddress.setBorder(Rectangle.NO_BORDER);
        prePhone.setBorder(Rectangle.NO_BORDER);
        preDate.setBorder(Rectangle.NO_BORDER);
        preBill.setBorder(Rectangle.NO_BORDER);

        try {
            tableHeading.addCell(preName);
            tableHeading.addCell(preBill);
            tableHeading.addCell(preAddress);
            tableHeading.addCell(preDate);
            //tableHeading.addCell(prePhone);

            document.add(tableHeading);
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        PdfPTable table = new PdfPTable(6);
        table.setSpacingBefore(20);
        try {
            table.setWidths(new float[]{0.9f, 3.2f, 0.7f, 1.6f, 1.0f, 1.7f});
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        table.setHeaderRows(1);

        table.setSplitRows(false);
        table.setComplete(false);


        PdfPCell headDate = new PdfPCell(new Phrase("NO", regularHead));
        PdfPCell headName = new PdfPCell(new Phrase("ITEM", regularHead));
        PdfPCell headDis = new PdfPCell(new Phrase("QTY", regularHead));
        PdfPCell headCr = new PdfPCell(new Phrase("TOTAL", regularHead));
        PdfPCell headDe = new PdfPCell(new Phrase("PER UNIT", regularHead));
        PdfPCell headtax = new PdfPCell(new Phrase("TAX", regularHead));

        headtax.setPaddingLeft(15);
        headtax.setPaddingTop(10);
        headtax.setPaddingBottom(10);
        headtax.setVerticalAlignment(Element.ALIGN_MIDDLE);

        headDate.setPaddingLeft(15);
        headDate.setPaddingTop(10);
        headDate.setPaddingBottom(10);
        headDate.setVerticalAlignment(Element.ALIGN_MIDDLE);

        headName.setPaddingTop(10);
        headName.setPaddingBottom(10);
        headName.setVerticalAlignment(Element.ALIGN_CENTER);
        headName.setPaddingLeft(15);
        headName.setHorizontalAlignment(Element.ALIGN_LEFT);

        headDis.setPaddingTop(10);
        headDis.setPaddingBottom(10);
        headDis.setPaddingLeft(10);
        headDis.setVerticalAlignment(Element.ALIGN_MIDDLE);

        headCr.setPaddingTop(10);
        headCr.setPaddingBottom(10);
        headCr.setPaddingLeft(10);
        headCr.setVerticalAlignment(Element.ALIGN_MIDDLE);

        headDe.setPaddingTop(10);
        headDe.setPaddingLeft(10);
        headDe.setPaddingBottom(10);
        headDe.setVerticalAlignment(Element.ALIGN_MIDDLE);


        headDate.setBackgroundColor(printPrimary1);
        headtax.setBackgroundColor(printPrimary1);
        headName.setBackgroundColor(printPrimary1);
        headDis.setBackgroundColor(printPrimary1);
        headCr.setBackgroundColor(printPrimary1);
        headDe.setBackgroundColor(printPrimary1);


        headDate.setBorder(Rectangle.NO_BORDER);
        headtax.setBorder(Rectangle.NO_BORDER);
        headName.setBorder(Rectangle.NO_BORDER);
        headDis.setBorder(Rectangle.NO_BORDER);
        headCr.setBorder(Rectangle.NO_BORDER);
        headDe.setBorder(Rectangle.NO_BORDER);


        table.addCell(headDate);
        table.addCell(headName);
        table.addCell(headDis);
        table.addCell(headDe);
        table.addCell(headtax);
        table.addCell(headCr);


        Double amountFull = 0.0;

        for (int aw = 0; aw < arrayLists[0].size(); aw++) {

            InvoiceTableHolder listItem = arrayLists[0].get(aw);

            onProgressUpdate(String.valueOf(aw) + " items processing...");

            PdfPCell cellNo = new PdfPCell(new Phrase(String.valueOf(aw + 1), regularSub));
            PdfPCell cellName = new PdfPCell(new Phrase(listItem.getName(), regularSub));
            PdfPCell cellCompany = new PdfPCell(new Phrase(String.valueOf(listItem.getQty()), regularSub));
            PdfPCell celldebit = new PdfPCell(new Phrase("Rs. " + listItem.getUnitprice(), regularSub));
            PdfPCell celltax = new PdfPCell(new Phrase(0+"%", regularSub));
            PdfPCell cellAmount = new PdfPCell(new Phrase("Rs. " + (listItem.getQty() * listItem.getUnitprice()), regularSub));

            amountFull += (listItem.getQty() * listItem.getUnitprice());

            cellNo.setPaddingLeft(15);
            cellNo.setPaddingBottom(8);
            cellNo.setPaddingTop(5);
            cellNo.setBorderColor(cutomborder);
            cellNo.setVerticalAlignment(Element.ALIGN_MIDDLE);

            celltax.setPaddingLeft(15);
            celltax.setPaddingBottom(8);
            celltax.setPaddingTop(5);
            celltax.setBorderColor(cutomborder);
            celltax.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cellName.setPaddingBottom(8);
            cellName.setPaddingTop(5);
            cellName.setPaddingLeft(10);
            cellName.setBorderColor(cutomborder);
            cellName.setVerticalAlignment(Element.ALIGN_MIDDLE);

            celldebit.setPaddingBottom(8);
            celldebit.setPaddingTop(5);
            celldebit.setPaddingLeft(10);
            celldebit.setBorderColor(cutomborder);
            celldebit.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cellCompany.setPaddingBottom(8);
            cellCompany.setPaddingTop(5);
            cellCompany.setPaddingLeft(10);
            cellCompany.setBorderColor(cutomborder);
            cellCompany.setVerticalAlignment(Element.ALIGN_MIDDLE);


            cellAmount.setPaddingBottom(8);
            cellAmount.setPaddingTop(5);
            cellAmount.setPaddingLeft(10);
            cellAmount.setBorderColor(cutomborder);
            cellAmount.setVerticalAlignment(Element.ALIGN_MIDDLE);
/*
                cellNo.setBorder(Rectangle.NO_BORDER);
                cellName.setBorder(Rectangle.NO_BORDER);
                celldebit.setBorder(Rectangle.NO_BORDER);
                cellCompany.setBorder(Rectangle.NO_BORDER);
                cellAmount.setBorder(Rectangle.NO_BORDER);*/

            if (aw % 2 == 0) {
                cellNo.setBackgroundColor(BaseColor.WHITE);
                celltax.setBackgroundColor(BaseColor.WHITE);
                cellName.setBackgroundColor(BaseColor.WHITE);
                celldebit.setBackgroundColor(BaseColor.WHITE);
                cellCompany.setBackgroundColor(BaseColor.WHITE);
                cellAmount.setBackgroundColor(BaseColor.WHITE);
            } else {
                cellNo.setBackgroundColor(BaseColor.WHITE);
                celltax.setBackgroundColor(BaseColor.WHITE);
                cellName.setBackgroundColor(BaseColor.WHITE);
                celldebit.setBackgroundColor(BaseColor.WHITE);
                cellCompany.setBackgroundColor(BaseColor.WHITE);
                cellAmount.setBackgroundColor(BaseColor.WHITE);
            }

            table.addCell(cellNo);
            table.addCell(cellName);
            table.addCell(cellCompany);
            table.addCell(celldebit);
            table.addCell(celltax);
            table.addCell(cellAmount);
        }


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
        table.setSplitRows(false);
        table.setComplete(false);


        PdfPCell preBorderGray = new PdfPCell(new Phrase(""));
        preBorderGray.setPaddingTop(5);
        preBorderGray.setMinimumHeight(10f);
        preBorderGray.setUseVariableBorders(true);
        preBorderGray.setBorder(Rectangle.BOTTOM);
        preBorderGray.setBorderColorBottom(BaseColor.GRAY);
        preBorderGray.setBorderWidthBottom(3);
        preBorderGray.setColspan(5);

        table.addCell(preBorderGray);

        //TODO CHK1
        float gsttotal = 10.2f;
        gsttotal = (float) (0.18 * amountFull);

        String totalamountdisplay = "Rs. " + amountFull;
        //Toast.makeText(MainActivity.this, totalamountdisplay, Toast.LENGTH_SHORT).show();
        PdfPCell subTotal = new PdfPCell(new Phrase("SUBTOTAL", regularSub));
        PdfPCell subtotalamt = new PdfPCell(new Phrase(totalamountdisplay, regularSub));

        subTotal.setPaddingTop(5);
        subtotalamt.setPaddingTop(5);
        subTotal.setBorderColor(cutomborder);
        subtotalamt.setBorderColor(cutomborder);
        subtotalamt.setPaddingLeft(10);
        subTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subTotal.setVerticalAlignment(Element.ALIGN_CENTER);
        subTotal.setPaddingRight(15);
        subTotal.setColspan(4);


        PdfPCell discount = new PdfPCell(new Phrase("DISCOUNT", regularSub));
        PdfPCell discountamt = new PdfPCell(new Phrase("Rs. 5", regularSub));

        discount.setPaddingTop(5);
        discountamt.setPaddingTop(5);
        discount.setBorderColor(cutomborder);
        discountamt.setBorderColor(cutomborder);
        discountamt.setPaddingLeft(10);
        discount.setHorizontalAlignment(Element.ALIGN_RIGHT);
        discount.setVerticalAlignment(Element.ALIGN_CENTER);
        discount.setPaddingRight(15);
        discount.setColspan(4);

        //TODO UPDATE HERE
        PdfPCell tax = new PdfPCell(new Phrase("GST ", regularSub));
        PdfPCell taxamtandrate = new PdfPCell(new Phrase( "18%", regularSub));
            /*if (GST_CHK) {
                taxamtandrate = new PdfPCell(new Phrase("Rs. " + taxrate.getEditText().getText().toString(), regularSub));
            } else taxamtandrate = new PdfPCell(new Phrase("n/a", regularSub));*/

        //tax.setPaddingTop(5);
        //taxamtandrate.setPaddingTop(5);
        taxamtandrate.setVerticalAlignment(Element.ALIGN_CENTER);
        taxamtandrate.setBorderColor(cutomborder);
        tax.setBorderColor(cutomborder);
        taxamtandrate.setPaddingLeft(10);
        tax.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tax.setVerticalAlignment(Element.ALIGN_CENTER);
        tax.setPaddingRight(15);
        tax.setColspan(4);


        PdfPCell preTotal = new PdfPCell(new Phrase("TOTAL", regularSub));
        PdfPCell preTotalAmount = new PdfPCell(new Phrase("0099", regularSub));
            /*if (GST_CHK) {
                float total = 12.2f;
                total = (float) (amountFull + gsttotal);
                preTotalAmount = new PdfPCell(new Phrase("Rs. " + String.valueOf(total), regularSub));
            } else {
                preTotalAmount = new PdfPCell(new Phrase("Rs. " + String.valueOf(amountFull), regularSub));
            }*/

        //preTotal.setPaddingTop(5);
        //preTotalAmount.setPaddingTop(5);
        preTotalAmount.setPaddingLeft(10);
        preTotalAmount.setBorderColor(cutomborder);
        preTotalAmount.setVerticalAlignment(Element.ALIGN_CENTER);
        preTotal.setBorderColor(cutomborder);
        preTotal.setVerticalAlignment(Element.ALIGN_CENTER);
        preTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        preTotal.setPaddingRight(15);


        PdfPTable duetable = new PdfPTable(3);

        duetable.setSplitRows(false);
        duetable.setComplete(false);


        PdfPCell paid = new PdfPCell(new Phrase("PAID", footerN));
        PdfPCell paidamt = new PdfPCell(new Phrase("Rs. " + "43321", footerN));

        //preTotal.setPaddingTop(5);
        //preTotalAmount.setPaddingTop(5);

        paidamt.setPaddingLeft(10);
        paidamt.setBorderColor(cutomborder);
        paidamt.setVerticalAlignment(Element.ALIGN_CENTER);
        paid.setBorderColor(cutomborder);
        paid.setVerticalAlignment(Element.ALIGN_CENTER);
        paid.setHorizontalAlignment(Element.ALIGN_RIGHT);
        paid.setPaddingRight(15);
        paid.setColspan(4);

        //TODO update data here.
        PdfPCell duetotal = new PdfPCell(new Phrase("BALANCE", footerN));
        PdfPCell dueTotalAmount = new PdfPCell(new Phrase("Rs. " + "43321", footerN));

        //preTotal.setPaddingTop(5);
        //preTotalAmount.setPaddingTop(5);

        dueTotalAmount.setPaddingLeft(10);
        dueTotalAmount.setBorderColor(cutomborder);
        dueTotalAmount.setVerticalAlignment(Element.ALIGN_CENTER);
        duetotal.setBorderColor(cutomborder);
        duetotal.setVerticalAlignment(Element.ALIGN_CENTER);
        duetotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        duetotal.setPaddingRight(15);
        duetotal.setColspan(4);

        PdfPCell quotedesc = new PdfPCell(new Phrase(infoData.getTerms(), regularSub));
        quotedesc.setHorizontalAlignment(Element.ALIGN_LEFT);
        quotedesc.setBorder(Rectangle.NO_BORDER);
        quotedesc.setPaddingRight(10);
        quotedesc.setPaddingTop(20);
        quotedesc.setColspan(5);


        PdfPCell customeracceptance = new PdfPCell(new Phrase("Customer acceptance.", regularSubbold));
        PdfPCell forth = new PdfPCell(new Phrase("For Thundersharp", regularSubbold));

        customeracceptance.setPaddingTop(20);
        forth.setPaddingTop(20);
        forth.setHorizontalAlignment(Element.ALIGN_RIGHT);
        forth.setBorder(Rectangle.NO_BORDER);
        customeracceptance.setBorder(Rectangle.NO_BORDER);
        customeracceptance.setHorizontalAlignment(Element.ALIGN_LEFT);
        customeracceptance.setPaddingRight(15);
        customeracceptance.setColspan(2);
        forth.setColspan(3);


        PdfPCell blankspace = null, sign = null;

        //Drawable d = getResources().getDrawable();
        //BitmapDrawable bitDw = ((BitmapDrawable) d);
        Bitmap bmp;
        if (infoData.getSignPicUri().equals("")){
            Drawable d = context.getDrawable(infoData.getLogo());
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            bmp = bitDw.getBitmap();
        }else {
             bmp = BitmapFactory.decodeFile(infoData.getSignPicUri());
        }


      /*      Drawable d = getResources().getDrawable(R.drawable.logo);
            BitmapDrawable bitDw = ((BitmapDrawable) d);*/
        //Bitmap bmp = bitDw.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 10, stream);
        try {
            Image image = Image.getInstance(stream.toByteArray());
            image.scaleToFit(100, 100);
            blankspace = new PdfPCell(new Phrase("", regularSubbold));
            sign = new PdfPCell(image, false);

            blankspace.setPaddingTop(0);
            sign.setPaddingTop(-15);
            sign.setHorizontalAlignment(Element.ALIGN_RIGHT);
            sign.setBorder(Rectangle.NO_BORDER);
            blankspace.setBorder(Rectangle.NO_BORDER);
            blankspace.setHorizontalAlignment(Element.ALIGN_LEFT);
            blankspace.setPaddingRight(15);
            blankspace.setColspan(2);
            sign.setColspan(3);


        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        PdfPCell customeracceptancebottom = new PdfPCell(new Phrase("Sign above to Accept.", regularSubbold));
        PdfPCell forthbottom = new PdfPCell(new Phrase("Authorized Signatory", regularSubbold));

        customeracceptancebottom.setPaddingTop(5);
        forthbottom.setPaddingTop(5);
        forthbottom.setHorizontalAlignment(Element.ALIGN_RIGHT);
        forthbottom.setBorder(Rectangle.NO_BORDER);
        customeracceptancebottom.setBorder(Rectangle.NO_BORDER);
        customeracceptancebottom.setHorizontalAlignment(Element.ALIGN_LEFT);
        customeracceptancebottom.setPaddingRight(15);
        customeracceptancebottom.setColspan(2);
        forthbottom.setColspan(3);


        //TODO 1
        //preTotal.setBorder(Rectangle.NO_BORDER);
        //preTotalAmount.setBorder(Rectangle.NO_BORDER);

        preTotal.setColspan(4);
        table.setComplete(true);

        table1.addCell(subTotal);
        table1.addCell(subtotalamt);

        table1.addCell(tax);
        table1.addCell(taxamtandrate);

        table1.addCell(discount);
        table1.addCell(discountamt);

        table1.addCell(preTotal);
        table1.addCell(preTotalAmount);

        table1.addCell(duetotal);
        table1.addCell(dueTotalAmount);


        table1.addCell(quotedesc);

        table1.addCell(customeracceptance);
        table1.addCell(forth);
        table1.addCell(blankspace);
        table1.addCell(sign);
        table1.addCell(customeracceptancebottom);
        table1.addCell(forthbottom);
        table1.setComplete(true);


        try {
            document.add(table);
            document.add(table1);
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        onProgressUpdate("Finishing up...");
        document.close();
        return 1;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        File file = new File(FOLDER_PDF);
        if (file.exists()) {
            file.mkdir();
        }

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        final String a = values[0].toString();
/*
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //txtUpdate.setText(a);
            }
        });
*/
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        File file = new File(path);
        Uri pdfURI = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
        invoiceGenerateObserver.pdfCreatedSuccess(pdfURI);
        // path1 = pdfURI;
        //uploadtofirebase(QUOTATION_NUMBER, CLIENT_NAME, CLIENT_ADDRESS, VALID_FROM, VALID_TILL, itemholder);
        // target.setDataAndType(Uri.fromFile(file),"application/pdf");
        /*Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(pdfURI, "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);*/
        //txtUpdate.setText("Finished");

 /*       Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }*/

    }

}
