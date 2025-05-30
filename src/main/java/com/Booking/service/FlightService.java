package com.Booking.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Booking.dto.FlightOfferDTO;
import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.resources.FlightOfferSearch;

@Service
public class FlightService {

    @Autowired
    private Amadeus amadeus;

    public List<FlightOfferDTO> searchFlights(String origin, String destination, String departureDate, int adults) {
        List<FlightOfferDTO> flightOfferDTOList = new ArrayList<>();

        try {
            FlightOfferSearch[] flightOffers = amadeus.shopping.flightOffersSearch.get(
                Params.with("originLocationCode", origin)
                      .and("destinationLocationCode", destination)
                      .and("departureDate", departureDate)
                      .and("adults", String.valueOf(adults)) 
                      .and("currencyCode", "INR")
                      .and("max", 10)
                      
            );

            System.out.println("Number of flight offers received: " + 
                (flightOffers != null ? flightOffers.length : "null"));

            if (flightOffers != null) {
                for (FlightOfferSearch flightOffer : flightOffers) {
                    // Safety check for null or empty itinerary/segments
                    if (flightOffer.getItineraries() != null &&
                        flightOffer.getItineraries().length > 0 &&
                        flightOffer.getItineraries()[0].getSegments() != null &&
                        flightOffer.getItineraries()[0].getSegments().length > 0) {

                        FlightOfferDTO dto = new FlightOfferDTO();

                        dto.setFlightNumber(
                            flightOffer.getItineraries()[0].getSegments()[0].getCarrierCode() +
                            flightOffer.getItineraries()[0].getSegments()[0].getNumber()
                        );
                        dto.setDepartureTime(
                            flightOffer.getItineraries()[0].getSegments()[0].getDeparture().getAt()
                        );
                        dto.setArrivalTime(
                            flightOffer.getItineraries()[0].getSegments()[0].getArrival().getAt()
                        );
                        dto.setDuration(flightOffer.getItineraries()[0].getDuration());
                        dto.setPrice(flightOffer.getPrice().getTotal());

                        flightOfferDTOList.add(dto);
                    } else {
                        System.out.println("Skipping a flight offer due to missing itinerary/segment info.");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();  // logs the exact error in the console
            throw new RuntimeException("Error searching flights: " + e.getMessage(), e);
        }

        return flightOfferDTOList;
    }
}
