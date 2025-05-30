package com.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Booking.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
