package com.Booking.config;

import com.amadeus.Amadeus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmadeusConfig {

	
	 @Bean public Amadeus amadeus() { return
	  Amadeus.builder("APFh9RJ2009SAT3KDJHje1NIv2FcgmIG",
	 "j31vF1Bsn8KF63Sj").build(); }}
	 
    
   /* @Value("$RLmPdfmKhF7gVDvYz334aNnxAYNXp2L5")
    private String clientId;

    @Value("$zbkDAdtwNeP2pWig")
    private String clientSecret;

    @Bean
    public Amadeus amadeus() {
        return Amadeus.builder(clientId, clientSecret)
                      .setHostname(Amadeus.Environment.TEST) // for sandbox
                      .build();*/
    

