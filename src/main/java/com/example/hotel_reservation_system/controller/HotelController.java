package com.example.hotel_reservation_system.controller;

import com.example.hotel_reservation_system.model.*;
import com.example.hotel_reservation_system.dto.*;
import com.example.hotel_reservation_system.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class HotelController {

    @Autowired private HotelService hotelService;

    @PostMapping("/users/register")
    public User register(@RequestBody User user) {
        return hotelService.registerUser(user);
    }

    @PostMapping("/rooms")
    public Room addRoom(@RequestBody Room room) {
        return hotelService.addRoom(room);
    }

    @GetMapping("/rooms/available")
    public List<Room> getAvailableRooms() {
        return hotelService.getAvailableRooms();
    }

    @PostMapping("/reservations")
    public ResponseEntity<?> makeBooking(@RequestBody ReservationRequestDTO requestDTO) {
        try {
            ReservationResponseDTO response = hotelService.createReservation(requestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/reservations")
    public List<ReservationResponseDTO> viewAllBookings() {
        return hotelService.getAllReservations();
    }
}