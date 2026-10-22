package com.apexbank.util;

import com.apexbank.model.Receipt;
import com.apexbank.model.Transaction;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.OutputStream;
import java.util.List;

/**
 * Generates industry-grade ATM Receipts and Account Statements as PDF documents.
 * Demonstrates OOP File and Stream Handling.
 */
public class PDFReceiptGenerator {

    /**
     * Generates a realistic 80mm thermal receipt PDF.
     */
    public static void generateAtmReceipt(Receipt receipt, OutputStream out) throws Exception {
        // Standard 80mm POS thermal receipt slip dimension (approx 226 pt x 480 pt)
        Rectangle thermalSlip = new Rectangle(240, 480);
        Document document = new Document(thermalSlip, 15, 15, 15, 15);
        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.DARK_GRAY);
        Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.GRAY);
        Font boldFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 9, Color.BLACK);
        Font regularFont = FontFactory.getFont(FontFactory.COURIER, 8, Color.BLACK);
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 7, Color.GRAY);

        // Header
        Paragraph title = new Paragraph("APEX NATIONAL BANK", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph subtitle = new Paragraph("ATM TRANSACTION ADVICE", subTitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        document.add(subtitle);

        Paragraph branch = new Paragraph(receipt.getBranchName() != null ? receipt.getBranchName() : "Connaught Place Branch", subTitleFont);
        branch.setAlignment(Element.ALIGN_CENTER);
        document.add(branch);

        document.add(new Paragraph("------------------------------------------------", regularFont));

        // Metadata Table
        PdfPTable metaTable = new PdfPTable(2);
        metaTable.setWidthPercentage(100);
        metaTable.setWidths(new float[]{40, 60});

        addReceiptRow(metaTable, "DATE & TIME:", receipt.getFormattedDateTime(), regularFont);
        addReceiptRow(metaTable, "ATM ID:", receipt.getAtmId(), regularFont);
        addReceiptRow(metaTable, "TERMINAL ID:", receipt.getTerminalId(), regularFont);
        addReceiptRow(metaTable, "TXN REF:", receipt.getTransactionReference(), boldFont);
        addReceiptRow(metaTable, "CARD NO:", receipt.getMaskedCard(), regularFont);
        addReceiptRow(metaTable, "ACC NO:", receipt.getMaskedAccount(), regularFont);
        addReceiptRow(metaTable, "CARDHOLDER:", receipt.getCustomerName(), regularFont);
        addReceiptRow(metaTable, "TXN TYPE:", receipt.getTransactionType() != null ? receipt.getTransactionType().getDescription() : "TRANSACTION", boldFont);

        document.add(metaTable);
        document.add(new Paragraph("------------------------------------------------", regularFont));

        // Financial Amounts
        PdfPTable amountTable = new PdfPTable(2);
        amountTable.setWidthPercentage(100);
        amountTable.setWidths(new float[]{50, 50});

        addReceiptRow(amountTable, "AMOUNT (INR):", CurrencyUtil.format(receipt.getAmount()), boldFont);
        addReceiptRow(amountTable, "AVAIL BAL (INR):", CurrencyUtil.format(receipt.getAvailableBalance()), boldFont);

        document.add(amountTable);
        document.add(new Paragraph("------------------------------------------------", regularFont));

        // Amount in words
        Paragraph words = new Paragraph(CurrencyUtil.convertToWords(receipt.getAmount()), regularFont);
        words.setAlignment(Element.ALIGN_CENTER);
        document.add(words);

        document.add(new Paragraph("\n"));

        // Footer
        Paragraph footer1 = new Paragraph("TRANSACTION COMPLETED SUCCESSFULLY", boldFont);
        footer1.setAlignment(Element.ALIGN_CENTER);
        document.add(footer1);

        Paragraph footer2 = new Paragraph(receipt.getThankYouMessage(), footerFont);
        footer2.setAlignment(Element.ALIGN_CENTER);
        document.add(footer2);

        Paragraph footer3 = new Paragraph("Helpline: " + receipt.getCustomerCarePhone(), footerFont);
        footer3.setAlignment(Element.ALIGN_CENTER);
        document.add(footer3);

        document.close();
    }

    /**
     * Generates a formal Full Statement PDF document for an account.
     */
    public static void generateStatementPdf(String accountNumber, String customerName, double balance,
                                            List<Transaction> transactions, OutputStream out) throws Exception {
        Document document = new Document(PageSize.A4, 25, 25, 25, 25);
        PdfWriter.getInstance(document, out);
        document.open();

        Font bankTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new Color(13, 110, 253));
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.DARK_GRAY);
        Font boldRowFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.BLACK);

        document.add(new Paragraph("APEX NATIONAL BANKING CORPORATION", bankTitleFont));
        document.add(new Paragraph("Account Statement & Transaction Ledger", FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY)));
        document.add(new Paragraph("Account Number: " + accountNumber + " | Customer: " + customerName));
        document.add(new Paragraph("Available Ledger Balance: " + CurrencyUtil.format(balance)));
        document.add(new Paragraph("Generated On: " + new java.util.Date().toString()));
        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{18, 18, 16, 22, 13, 13});

        String[] headers = {"Date & Time", "Reference No", "Type", "Description", "Amount", "Balance After"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(new Color(13, 110, 253));
            cell.setPadding(6);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        for (Transaction txn : transactions) {
            table.addCell(new Phrase(txn.getFormattedDate(), rowFont));
            table.addCell(new Phrase(txn.getReferenceNumber(), rowFont));
            table.addCell(new Phrase(txn.getTransactionType().name(), boldRowFont));
            table.addCell(new Phrase(txn.getDescription() != null ? txn.getDescription() : "-", rowFont));
            
            PdfPCell amtCell = new PdfPCell(new Phrase(CurrencyUtil.format(txn.getAmount()), boldRowFont));
            amtCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(amtCell);

            PdfPCell balCell = new PdfPCell(new Phrase(CurrencyUtil.format(txn.getBalanceAfter()), rowFont));
            balCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(balCell);
        }

        document.add(table);
        document.close();
    }

    private static void addReceiptRow(PdfPTable table, String label, String value, Font font) {
        PdfPCell c1 = new PdfPCell(new Phrase(label, font));
        c1.setBorder(Rectangle.NO_BORDER);
        c1.setPadding(2);
        table.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase(value != null ? value : "", font));
        c2.setBorder(Rectangle.NO_BORDER);
        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c2.setPadding(2);
        table.addCell(c2);
    }
}
