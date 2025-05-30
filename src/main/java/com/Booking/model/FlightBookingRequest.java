package com.Booking.model;



public class FlightBookingRequest {
      
    private String origin;
    private String destination;
    private String departureDate;
    private int adults;
    private String email;
    private String passengerName;
    private boolean paymentConfirmed;
    private String FlightNumber;
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}
	public int getAdults() {
		return adults;
	}
	public void setAdults(int adults) {
		this.adults = adults;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassengerName() {
		return passengerName;
	}
	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}
	
	  
	 public String getFlightNumber() {
		return FlightNumber;
	}
	public void setFlightNumber(String flightNumber) {
		FlightNumber = flightNumber;
	}
	public boolean isPaymentConfirmed() { return paymentConfirmed; }
	 
	  public void setPaymentConfirmed(boolean paymentConfirmed) {
		  this.paymentConfirmed = paymentConfirmed; }

}
