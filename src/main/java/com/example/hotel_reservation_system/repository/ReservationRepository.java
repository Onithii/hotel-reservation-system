package com.example.hotel_reservation_system.repository;

import com.example.hotel_reservation_system.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Custom query method to find all reservations belonging to a specific User ID
    List<Reservation> findByUserId(Long userId);
}