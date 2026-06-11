package com.example.hotel_reservation_system.repository;

import com.example.hotel_reservation_system.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // ROOM CHECK (UPDATED - NO STATUS)
    boolean existsByRoomNumber(int roomNumber);
}