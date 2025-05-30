package com.Booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Booking.dto.BookingRequest;
import com.Booking.model.Booking;
import com.Booking.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

	/*
	 * @PostMapping("/create") public ResponseEntity<String> bookFlight(@RequestBody
	 * BookingRequest request) { Booking booking =
	 * bookingService.bookFlight(request); return
	 * ResponseEntity.ok("Booking confirmed for " + booking.getPassengerName() +
	 * " with Booking ID: " + booking.getId()); }
	 */
    
    @PostMapping("/book")
    public ResponseEntity<Booking> bookFlight(@RequestBody BookingRequest request) {
        Booking booking = bookingService.createBooking(request);
        return ResponseEntity.ok(booking);
    }

}
