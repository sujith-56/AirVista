package com.Booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.Booking.model.Booking;
import com.Booking.util.TicketGenerator;
import com.Booking.util.TicketPdfGenerator;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendBookingConfirmation(Booking booking) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // multipart
            helper.setFrom("airvista445@gmail.com");
            helper.setTo(booking.getEmail());
            
            helper.setSubject("Booking Confirmation - Flight Ticket");

            String content = "<h2>Booking Confirmed!</h2>"
                    + "<p>Dear " + booking.getPassengerName() + ",</p>"
                    + "<p>Your flight booking is confirmed. Please find your ticket attached.</p>"
                    + "<p><b>Flight:</b> " + booking.getFlightId() + "<br>"
                    + "<b>From:</b> " + booking.getOrigin() + "<br>"
                    + "<b>To:</b> " + booking.getDestination() + "<br>"
                    + "<b>Date:</b> " + booking.getDepartureDate() + "</p>";

            helper.setText(content, true);

            // PDF attachment
            byte[] pdfBytes = TicketGenerator.generateTicketPdf(booking);
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
            helper.addAttachment("FlightTicket.pdf", resource);

            mailSender.send(message);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}