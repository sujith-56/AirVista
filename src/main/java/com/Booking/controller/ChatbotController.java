package com.Booking.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import com.Booking.dto.FlightOfferDTO;
import com.Booking.model.FlightBookingRequest;
import com.Booking.service.BookingService;
import com.Booking.service.FlightService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/chat")
@SessionAttributes("bookingRequest")
public class ChatbotController {

    private final BookingService bookingService;
    private final FlightService flightService;

    private final Map<String, String> bookingInputs = new LinkedHashMap<>(Map.of(
        "passengerName", "What is your name?",
        "origin", "Where are you flying from?",
        "destination", "Where do you want to go?",
        "departureDate", "What is your departure date? (YYYY-MM-DD)",
        "adults", "How many adults are flying?",
        "email", "Please provide your email address."
    ));

    private List<FlightOfferDTO> lastFlightOptions = new ArrayList<>();

    public ChatbotController(BookingService bookingService, FlightService flightService) {
        this.bookingService = bookingService;
        this.flightService = flightService;
    }

    @PostMapping
    public String chat(@RequestBody String userMessage, HttpSession session) {
        FlightBookingRequest bookingRequest = (FlightBookingRequest) session.getAttribute("bookingRequest");

        if (bookingRequest == null) {
            bookingRequest = new FlightBookingRequest();
            session.setAttribute("bookingRequest", bookingRequest);
        }

        userMessage = userMessage.replace("\"", "");
        if (userMessage.isEmpty() || userMessage.equalsIgnoreCase("hi") || userMessage.equalsIgnoreCase("hello")) {
            String next = getNextMissingField(bookingRequest);
            return bookingInputs.getOrDefault(next, "Let's get started!");
        }
        String nextField = getNextMissingField(bookingRequest);

        if (nextField != null) {
            setBookingField(nextField, userMessage, bookingRequest);

            // After all flight input fields are gathered, show flights
            if (getNextMissingField(bookingRequest) == null) {
                lastFlightOptions = flightService.searchFlights(
                    bookingRequest.getOrigin(),
                    bookingRequest.getDestination(),
                    bookingRequest.getDepartureDate(),
                    bookingRequest.getAdults()
                );

                if (lastFlightOptions.isEmpty()) {
                    return "❌ No flights found. Please restart and try again.";
                }

                StringBuilder response = new StringBuilder("✈️ Available flights:\n");
                for (int i = 0; i < lastFlightOptions.size(); i++) {
                    FlightOfferDTO f = lastFlightOptions.get(i);
                    response.append(i + 1)
                        .append(". ")
                        .append(f.getFlightNumber())
                        .append(" | ₹")
                        .append(f.getPrice())
                        .append(" | ")
                        .append(f.getDepartureTime())
                        .append(" to ")
                        .append(f.getArrivalTime())
                        .append("\n");
                }
                response.append("\nPlease select a flight by entering the number (e.g., 1):");
                return response.toString();
            }
            return bookingInputs.get(getNextMissingField(bookingRequest));
        }

        // User is selecting a flight now
        try {
            int selectedIndex = Integer.parseInt(userMessage.trim()) - 1;
            if (selectedIndex < 0 || selectedIndex >= lastFlightOptions.size()) {
                return "❗ Invalid flight selection. Please enter a number between 1 and " + lastFlightOptions.size();
            }

            FlightOfferDTO selectedFlight = lastFlightOptions.get(selectedIndex);
            bookingRequest.setFlightNumber(selectedFlight.getFlightNumber());

            // 🔐 Payment step would go here
            // You can integrate Razorpay/Stripe here and wait for success

            bookingService.bookFlight(bookingRequest);
            session.removeAttribute("bookingRequest");
            return "✅ Booking complete for " + selectedFlight.getFlightNumber()
                + "! Ticket sent to " + bookingRequest.getEmail();
        } catch (NumberFormatException e) {
            return "❗ Please enter a valid number to select a flight.";
        }
    }

    private String getNextMissingField(FlightBookingRequest request) {
        if (request.getPassengerName() == null) return "passengerName";
        if (request.getOrigin() == null) return "origin";
        if (request.getDestination() == null) return "destination";
        if (request.getDepartureDate() == null) return "departureDate";
        if (request.getAdults() == 0) return "adults";
        if (request.getEmail() == null) return "email";
        return null;
    }

    private void setBookingField(String field, String value, FlightBookingRequest request) {
        switch (field) {
            case "passengerName" -> request.setPassengerName(value);
            case "origin" -> request.setOrigin(value.toUpperCase());
            case "destination" -> request.setDestination(value.toUpperCase());
            case "departureDate" -> request.setDepartureDate(value);
            case "adults" -> request.setAdults(Integer.parseInt(value));
            case "email" -> request.setEmail(value);
        }
    }

    @GetMapping("/download-ticket")
    public ResponseEntity<Resource> downloadTicket() throws FileNotFoundException {
        File ticket = new File(System.getProperty("java.io.tmpdir"), "ticket.pdf");
        if (!ticket.exists()) return ResponseEntity.notFound().build();

        InputStreamResource resource = new InputStreamResource(new FileInputStream(ticket));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=Ticket.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .contentLength(ticket.length())
            .body(resource);
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetChat(HttpSession session) {
        session.invalidate(); // Fully resets the session
        lastFlightOptions.clear();
        return ResponseEntity.ok("🔄 Booking session has been reset.");
    }
	

}
