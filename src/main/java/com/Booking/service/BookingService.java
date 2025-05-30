package com.Booking.service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Booking.dto.BookingRequest;
import com.Booking.model.Booking;
import com.Booking.model.FlightBookingRequest;
import com.Booking.repository.BookingRepository;
import com.Booking.util.TicketGenerator;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private EmailService emailService;
    
    
    
    public Booking createBooking(BookingRequest request) {
        Booking booking = new Booking();
        booking.setFlightId(request.getFlightId());
        booking.setOrigin(request.getOrigin());
        booking.setDestination(request.getDestination());
        booking.setDepartureDate(request.getDepartureDate());
        booking.setPassengerName(request.getPassengerName());
        booking.setEmail(request.getEmail());
        booking.setSeats(request.getSeats());
        // Set bookingDate and status
        booking.setBookingDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        booking.setStatus("CONFIRMED");

        Booking savedBooking = bookingRepository.save(booking);

        // Send confirmation email
        String subject = "Flight Booking Confirmation";
        String message = "Dear " + booking.getPassengerName() + " " + ",\n\n"
                + "Your flight from " + booking.getOrigin() + " to " + booking.getDestination()
                + " has been confirmed!\n\nBooking ID: " + savedBooking.getId()
                + "\nStatus: " + booking.getStatus() + "\n\nThank you for booking with us.";

        emailService.sendBookingConfirmation(booking);
        
        return savedBooking;
    }
     
    public String bookFlight(FlightBookingRequest request) {
        Booking booking = new Booking();
        booking.setOrigin(request.getOrigin());
        booking.setDestination(request.getDestination());
        booking.setDepartureDate(request.getDepartureDate());
        booking.setFlightId(request.getFlightNumber());
        booking.setPassengerName(request.getPassengerName());

        booking.setEmail(request.getEmail());
        booking.setSeats(request.getAdults());
        booking.setBookingDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        booking.setStatus("CONFIRMED");

        Booking savedBooking = bookingRepository.save(booking);

        // Generate and save the ticket PDF
        try {
            generateAndSaveTicket(savedBooking);
        } catch (Exception e) {
            e.printStackTrace(); // Optional: log this properly
        }

        // Send email with the ticket
        emailService.sendBookingConfirmation(savedBooking);

        return "✅ Booking confirmed!\n\n" +
               "✈️ From: " + booking.getOrigin() + "\n" +
               "🏁 To: " + booking.getDestination() + "\n" +
               "📅 Date: " + booking.getDepartureDate() + "\n" +
               "👤 Passengers: " + booking.getSeats() + "\n" +
               "📧 Confirmation sent to: " + booking.getEmail() + "\n\n" +
               "📄 [Click here to download your ticket](http://localhost:8080/chat/download-ticket)";
    }

    	public File generateAndSaveTicket(Booking booking) throws Exception {
    	    File outputFile = new File(System.getProperty("java.io.tmpdir"), "ticket.pdf");
    	    return TicketGenerator.generateTicketPdf(booking, outputFile);
    	}

}
