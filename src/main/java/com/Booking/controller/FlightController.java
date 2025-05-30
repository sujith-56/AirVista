package com.Booking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Booking.dto.FlightOfferDTO;
import com.Booking.service.FlightService;
import com.amadeus.exceptions.ResponseException;

import ch.qos.logback.core.model.Model;
 
@RestController
@RequestMapping("/api/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

	
	  @GetMapping("/search") public ResponseEntity<List<FlightOfferDTO>>
	  searchFlights(
	   
	  @RequestParam String origin,
	   
	  @RequestParam String destination,
	   
	  @RequestParam String date,
	  
	  @RequestParam int adults) { try { List<FlightOfferDTO> flightOffers =
	  flightService.searchFlights(origin, destination, date, adults); return
	  ResponseEntity.ok(flightOffers);
	   
	   
	 } catch (Exception e) { // Handle other exceptions, e.g., unexpected errors
	  return ResponseEntity.status(500).body(null); } }
	  
	/*  
	 * @GetMapping("/search") public String searchFlights(
	 * 
	 * @RequestParam String origin,
	 * 
	 * @RequestParam String destination,
	 * 
	 * @RequestParam String date,
	 * 
	 * @RequestParam int adults, Model model) throws ResponseException {
	 * 
	 * return "flightSearch"; // This corresponds to /WEB-INF/jsp/flightSearch.jsp }
	 */
}