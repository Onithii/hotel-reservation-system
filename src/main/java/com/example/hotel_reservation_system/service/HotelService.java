package com.example.hotel_reservation_system.service;

import com.nibm.hotel.model.*;
import com.nibm.hotel.dto.*;
import com.nibm.hotel.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelService {

    @Autowired private UserRepository userRepo;
    @Autowired private RoomRepository roomRepo;
    @Autowired private ReservationRepository reservationRepo;

    // --- USER / ROOM CRUD PASSTHROUGHS ---
    public User registerUser(User user) { return userRepo.save(user); }
    public List<User> getAllUsers() { return userRepo.findAll(); }
    public Room addRoom(Room room) { return roomRepo.save(room); }
    public List<Room> getAvailableRooms() { return roomRepo.findByStatus("AVAILABLE"); }

    // --- BOOKING LOGIC WITH DTO MAPPING ---
    public ReservationResponseDTO createReservation(ReservationRequestDTO dto) {
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User profile not found."));
        Room room = roomRepo.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Target room not found."));

        if (!"AVAILABLE".equalsIgnoreCase(room.getStatus())) {
            throw new IllegalStateException("Room is occupied.");
        }

        // Calculate nights stayed
        long days = ChronoUnit.DAYS.between(dto.getCheckInDate(), dto.getCheckOutDate());
        if (days <= 0) days = 1;

        // Map DTO payload to internal Database Entity
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setCheckInDate(dto.getCheckInDate());
        reservation.setCheckOutDate(dto.getCheckOutDate());
        reservation.setTotalPrice(days * room.getPricePerNight() * 1.12); // Base price + 12% automated tax
        reservation.setStatus("CONFIRMED");

        // Update room availability
        room.setStatus("BOOKED");
        roomRepo.save(room);

        Reservation savedReservation = reservationRepo.save(reservation);
        return convertToResponseDTO(savedReservation);
    }

    public List<ReservationResponseDTO> getAllReservations() {
        return reservationRepo.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Helper method to convert Entity -> Response DTO
    private ReservationResponseDTO convertToResponseDTO(Reservation res) {
        ReservationResponseDTO response = new ReservationResponseDTO();
        response.setId(res.getId());
        response.setCustomerName(res.getUser().getName());
        response.setRoomNumber(res.getRoom().getRoomNumber());
        response.setCheckInDate(res.getCheckInDate());
        response.setCheckOutDate(res.getCheckOutDate());
        response.setTotalPrice(res.getTotalPrice());
        response.setStatus(res.getStatus());
        return response;
    }
}