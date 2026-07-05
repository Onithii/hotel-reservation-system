package com.example.hotel_reservation_system.controller;

import com.example.hotel_reservation_system.model.*;
import com.example.hotel_reservation_system.dto.*;
import com.example.hotel_reservation_system.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    // =========================================================================
    // --- 1. USER & ROOM ENDPOINTS ---
    // =========================================================================

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

    // =========================================================================
    // --- 2. RESERVATION CRUD ENDPOINTS (MANDATORY COURSEWORK FEATURES) ---
    // =========================================================================

    // CREATE - Make a new booking (Added @Valid for input verification)
    @PostMapping("/reservations")
    public ResponseEntity<?> makeBooking(@Valid @RequestBody ReservationRequestDTO requestDTO) {
        try {
            ReservationResponseDTO response = hotelService.createReservation(requestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // READ ALL - View all records
    @GetMapping("/reservations")
    public List<ReservationResponseDTO> viewAllBookings() {
        return hotelService.getAllReservations();
    }

    // READ BY ID - Search a record by ID (Mandatory Requirement)
    @GetMapping("/reservations/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable Long id) {
        // Calls the mapper/lookup chain in your service layer
        ReservationResponseDTO response = hotelService.convertToResponseDTO(
                hotelService.getReservationEntityById(id)
        );
        return ResponseEntity.ok(response);
    }

    // UPDATE - Modify an existing record status (Mandatory Requirement)
    @PutMapping("/reservations/{id}/status")
    public ResponseEntity<ReservationResponseDTO> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        ReservationResponseDTO response = hotelService.updateStatus(id, status);
        return ResponseEntity.ok(response);
    }

    // DELETE - Remove a record (Mandatory Requirement)
    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        hotelService.deleteReservation(id);
        return ResponseEntity.ok("Reservation record with ID " + id + " has been successfully dropped.");
    }
}