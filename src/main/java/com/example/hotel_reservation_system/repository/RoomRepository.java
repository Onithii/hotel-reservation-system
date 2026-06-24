package com.example.hotel_reservation_system.repository;

import com.example.hotel_reservation_system.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    // Custom query method derived automatically by Spring based on the field name
    List<Room> findByStatus(String status);
}