package com.thundersharp.billgenerator;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Billing extends AsyncTask<ArrayList<InvoiceTableHolder>, String, Integer> {

    private static Billing billing;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private InvoiceGenerateObserver invoiceGenerateObserver;
    private InfoData infoData;

    public static Billing initializeBiller(Context activityContext) {
        billing = new Billing();
        context = activityContext;
        return billing;
    }

    public Billing setInfoData(InfoData infoData) {
        this.infoData = infoData;
        return billing;
    }

    public Billing attachObserver(InvoiceGenerateObserver invoiceGenerateObserver) {
        this.invoiceGenerateObserver = invoiceGenerateObserver;
        return billing;
    }

    public void createPdf(ArrayList<InvoiceTableHolder> data) throws Exception {
        if (billing == null) throw new Exception("initializeBiller() failed.");
        else if (invoiceGenerateObserver == null) throw new Exception("Observer not attached.");
        else if (infoData == null) throw new Exception("setInfoData() not called.");
        else billing.doInBackground(data);
    }

    private static BaseColor printPrimary = new BaseColor(0, 133, 119);//A = 1
    private static BaseColor printPrimary1 = new BaseColor(60, 92, 195);//A = 1
    private static BaseColor textColorp = new BaseColor(43, 43, 43);
    private static BaseColor cutomborder = new BaseColor(57, 57, 57);

    private static String FOLDER_PDF = Environment.getExternalStorageDirectory() + File.separator + "Thundersharp/Quotations";


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
        Font regularReport = new Font(baseFont, 11, Font.BOLD, BaseColor.BLACK);
        Font regularHeadquote = new Font(baseFont, 7, Font.NORMAL, textColorp);
        Font regularHeadquotel = new Font(baseFont, 7, Font.BOLD, BaseColor.BLACK);
        Font boldHeadSmall = new Font(baseFont, 7, Font.BOLD, BaseColor.BLACK);
        Font itemFont = new Font(baseFont, 7, Font.NORMAL, BaseColor.BLACK);
        Font regularSub = new Font(baseFont, 10);
        Font regularSubbold = new Font(baseFont, 10, Font.BOLD);
        Font smallfooter = new Font(baseFont, 9);
        Font regularTotal = new Font(baseFont, 11, Font.NORMAL, BaseColor.BLACK);
        Font regularTotalBold = new Font(baseFont, 13, Font.BOLD, BaseColor.BLACK);
        Font footerN = new Font(baseFont, 10, Font.BOLD, textColorp);
        Font footerE = new Font(baseFont, 10, Font.NORMAL, BaseColor.BLACK);


        Rectangle rectangle = new Rectangle(227, 1440);
        //A7 = 74mm in size
        Document document = new Document(rectangle, 7, 7, 25, 92);
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


        try {
            tableFooter.setWidths(new float[]{1.4f, 2});
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        //TODO REMOVE THIS
        HeaderFooter event = new HeaderFooter(tableFooter);

        writer.setPageEvent(event);

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
            Drawable d = context.getDrawable(infoData.getLogo());
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


        PdfPCell preAddress = new PdfPCell(new Phrase("Delivered to :" + infoData.getClientAddress(), regularHeadquotel));
        PdfPCell prePhone = new PdfPCell(new Phrase("Phone: " + infoData.getClientPhone(), regularHeadquotel));

        PdfPCell preBill = new PdfPCell(new Phrase("Bill no. #" + infoData.getOrderId(), regularHeadquotel));

        PdfPCell preDate = new PdfPCell(new Phrase("Bill Date\n" + TimeUtilities.getTimeFromTimeStamp(infoData.getOrderId()), regularHeadquotel));

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



        /*headDate.setPaddingLeft(5);
        headDate.setPaddingTop(5);
        headDate.setPaddingBottom(5);*/
        headDate.setPaddingBottom(5);
        headDate.setVerticalAlignment(Element.ALIGN_LEFT);

        /*headName.setPaddingTop(10);
        headName.setPaddingBottom(10);
        headName.setPaddingLeft(15);*/
        headName.setPaddingBottom(7);
        headName.setVerticalAlignment(Element.ALIGN_CENTER);


        /*headDis.setPaddingTop(10);
        headDis.setPaddingBottom(10);
        headDis.setPaddingLeft(10);*/
        headDis.setPaddingBottom(7);
        headDis.setVerticalAlignment(Element.ALIGN_CENTER);

      /*  headCr.setPaddingTop(10);
        headCr.setPaddingBottom(10);
        headCr.setPaddingLeft(10);*/
        headCr.setPaddingBottom(7);
        headCr.setHorizontalAlignment(Element.ALIGN_CENTER);
        headCr.setVerticalAlignment(Element.ALIGN_CENTER);

        /*headDe.setPaddingTop(10);
        headDe.setPaddingLeft(10);
        headDe.setPaddingBottom(10);*/
        headDe.setPaddingBottom(7);
        headDe.setHorizontalAlignment(Element.ALIGN_CENTER);
        headDe.setVerticalAlignment(Element.ALIGN_CENTER);


        /*headDate.setBackgroundColor(printPrimary1);

        headName.setBackgroundColor(printPrimary1);
        headDis.setBackgroundColor(printPrimary1);
        headCr.setBackgroundColor(printPrimary1);
        headDe.setBackgroundColor(printPrimary1);*/


        headDate.setBorder(Rectangle.NO_BORDER);

        headName.setBorder(Rectangle.NO_BORDER);
        headDis.setBorder(Rectangle.NO_BORDER);
        headCr.setBorder(Rectangle.NO_BORDER);
        headDe.setBorder(Rectangle.NO_BORDER);


/*        headName.setBorderColorBottom(BaseColor.BLACK);
        headDis.setBorderColorBottom(BaseColor.BLACK);
        headCr.setBorderColorBottom(BaseColor.BLACK);
        headDe.setBorderColorBottom(BaseColor.BLACK);
        headDate.setBorderColorBottom(BaseColor.BLACK);*/


        table.addCell(headDate);
        table.addCell(headName);
        table.addCell(headDis);
        table.addCell(headDe);
        table.addCell(headCr);


        Double amountFull = 0.0;

        for (int aw = 0; aw < arrayLists[0].size(); aw++) {

            InvoiceTableHolder listItem = arrayLists[0].get(aw);

            onProgressUpdate(String.valueOf(aw) + " items processing...");

            PdfPCell cellNo = new PdfPCell(new Phrase(String.valueOf(aw + 1), boldHeadSmall));
            PdfPCell cellName = new PdfPCell(new Phrase(listItem.getName().toLowerCase(), boldHeadSmall));
            PdfPCell cellCompany = new PdfPCell(new Phrase(String.valueOf(listItem.getQty()), boldHeadSmall));
            PdfPCell celldebit = new PdfPCell(new Phrase(String.valueOf(listItem.getUnitprice()), boldHeadSmall));
            PdfPCell cellAmount = new PdfPCell(new Phrase("" + (listItem.getQty() * listItem.getUnitprice()), boldHeadSmall));

            amountFull += (listItem.getQty() * listItem.getUnitprice());


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
        PdfPCell taxamtandrate = new PdfPCell(new Phrase("18%", regularSub));
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
        if (infoData.getSignPicUri().equals("")) {
            Drawable d = context.getDrawable(infoData.getLogo());
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            bmp = bitDw.getBitmap();
        } else {
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
