package com.Booking.util;

import com.Booking.model.Booking;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
 
import com.itextpdf.text.Image;
import javax.imageio.ImageIO;

public class TicketPdfGenerator {
	 
    public static byte[] generateTicketPdf(Booking booking) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();
        

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        document.add(new Paragraph("Flight Ticket Confirmation", titleFont));
        document.add(new Paragraph(" ")); // Empty line

        document.add(new Paragraph("Passenger Name: " + booking.getPassengerName(), bodyFont));
        document.add(new Paragraph("Flight Number: " + booking.getFlightId(), bodyFont));
        document.add(new Paragraph("Origin: " + booking.getOrigin(), bodyFont));
        document.add(new Paragraph("Destination: " + booking.getDestination(), bodyFont));
        document.add(new Paragraph("Departure Date: " + booking.getDepartureDate(), bodyFont));
     //   document.add(new Paragraph("Return Date: " + booking.getReturnDate(), bodyFont));
        document.add(new Paragraph("Seat Number: " + booking.getSeats(), bodyFont));
     //   document.add(new Paragraph("Price: $" + booking.getPrice(), bodyFont));
        document.add(new Paragraph("Status: " + booking.getStatus(), bodyFont));
        document.add(new Paragraph("Booking Date: " + booking.getBookingDate(), bodyFont));
        BufferedImage qrImage = QrCodeGenerator.generateQRCodeImage("Booking ID: " + booking.getId());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", baos);
		/*
		 * Image qrPdfImage = Image.getInstance(baos.toByteArray());
		 * qrPdfImage.setAlignment(Image.ALIGN_CENTER); qrPdfImage.scaleToFit(100, 100);
		 */ 
        // Example QR content: booking ID and passenger name
        
        String qrContent = "Booking ID: " + booking.getId()
                + "\nPassenger: " + booking.getPassengerName()
                + "\nFlight: " + booking.getFlightId();

        BufferedImage qrImage1 = generateQRCodeImage(qrContent);
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        ImageIO.write(qrImage1, "PNG", baos1);
 
        Image qrPdfImage = Image.getInstance(baos1.toByteArray());
        qrPdfImage.setAlignment(Image.ALIGN_CENTER);
        qrPdfImage.scaleToFit(100, 100); 

        document.add(qrPdfImage); // Add QR image to the PDF

        document.close();
        return out.toByteArray(); 
    }
    public static BufferedImage generateQRCodeImage(String text) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 150, 150);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

	  
}
 
 class QrCodeGenerator {

    public static BufferedImage generateQRCodeImage(String text) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 150, 150);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}