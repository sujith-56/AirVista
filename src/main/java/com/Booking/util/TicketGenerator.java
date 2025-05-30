package com.Booking.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

import com.Booking.model.Booking;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class TicketGenerator {
	
	private static Image loadTemplateImage() throws Exception {
	    return Image.getInstance("src/main/resources/Ticket_Template-.png"); // Update the path accordingly
	}
	
	public static byte[] generateTicketPdf(Booking booking) throws Exception {

	    Document document = new Document();
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    PdfWriter writer = PdfWriter.getInstance(document, out);
	    
	    // Open the document before accessing its content
	    document.open();

	    // Load the template image
	    Image templateImage = loadTemplateImage();
	    templateImage.scaleToFit(document.getPageSize().getWidth(), document.getPageSize().getHeight());
	    templateImage.setAlignment(Image.ALIGN_CENTER);
	    document.add(templateImage); // Add the template image to the PDF

	    // Now you can get the direct content
	    PdfContentByte canvas = writer.getDirectContent();
	    PdfGState gState = new PdfGState();
	    gState.setFillOpacity(1f); // Set opacity if needed
	    canvas.setGState(gState);

	    // Set font for overlay text
	    Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
	    BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
	    canvas.beginText();
	    canvas.setFontAndSize(bf, 12);

	    // Overlay text on the template
	    canvas.showTextAligned(Element.ALIGN_LEFT,  " " + booking.getPassengerName(), 310, 730, 0);
	    canvas.setFontAndSize(bf, 11);
	    canvas.showTextAligned(Element.ALIGN_LEFT, "" + booking.getFlightId(), 284, 645, 0);
	    canvas.setFontAndSize(bf, 12);
	    canvas.showTextAligned(Element.ALIGN_LEFT, "" + booking.getOrigin(), 460, 716, 0);
	    canvas.showTextAligned(Element.ALIGN_LEFT, "" + booking.getDestination(), 535, 716, 0);
	    canvas.setFontAndSize(bf, 9);
	    canvas.setColorFill(BaseColor.WHITE);
	    canvas.showTextAligned(Element.ALIGN_LEFT, "" + booking.getDepartureDate(), 285, 682, 0);
	    canvas.setFontAndSize(bf, 12);
	    canvas.setColorFill(BaseColor.BLACK);
	   // canvas.showTextAligned(Element.ALIGN_LEFT, "" + booking.getSeats(), 365, 645, 0);
	   // canvas.showTextAligned(Element.ALIGN_LEFT, "Status: " + booking.getStatus(), 100, 580, 0);
	   // canvas.showTextAligned(Element.ALIGN_LEFT, "Booking Date: " + booking.getBookingDate(), 100, 560, 0);
        
	    canvas.setFontAndSize(bf, 11);
	    canvas.showTextAligned(Element.ALIGN_LEFT, "" + booking.getFlightId(), 19, 690, 0);
	    canvas.setFontAndSize(bf, 12);
	    canvas.showTextAligned(Element.ALIGN_LEFT, "" + booking.getOrigin(), 16, 660, 0);
	    canvas.showTextAligned(Element.ALIGN_LEFT, "" + booking.getDestination(), 95, 660, 0);
	   // canvas.showTextAligned(Element.ALIGN_LEFT, "" + booking.getSeats(), 100, 690, 0);
	    canvas.setFontAndSize(bf, 7);
	    canvas.setColorFill(BaseColor.WHITE);
	    canvas.showTextAligned(Element.ALIGN_LEFT, "" + booking.getDepartureDate(), 19, 728, 0);
	    
	    
	    
	    // Add QR code image
	    BufferedImage qrImage = generateQRCodeImage("Booking ID: " + booking.getId());
	    
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageIO.write(qrImage, "PNG", baos);
	    Image qrPdfImage = Image.getInstance(baos.toByteArray());
	    qrPdfImage.setAlignment(Image.ALIGN_RIGHT);
	    qrPdfImage.scaleToFit(100, 100);
	    document.add(qrPdfImage); // Add QR image to the PDF
	    qrPdfImage.scaleToFit(50, 50);
	    qrPdfImage.setAbsolutePosition(document.getPageSize().getWidth() - 120, 660); // Adjust X and Y here
	    document.add(qrPdfImage);

	    canvas.endText();
	    document.close(); // Close the document
	    return out.toByteArray();
	}
    public static BufferedImage generateQRCodeImage(String text) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 150, 150);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

public static File generateTicketPdf(Booking booking, File outputFile) throws Exception {
    byte[] pdfBytes = generateTicketPdf(booking); // reuse existing method
    try (OutputStream os = new FileOutputStream(outputFile)) {
        os.write(pdfBytes);
    }
    return outputFile;
}

} 
 
