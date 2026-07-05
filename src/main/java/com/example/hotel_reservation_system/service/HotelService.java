package com.example.hotel_reservation_system.service;

import com.example.hotel_reservation_system.model.*;
import com.example.hotel_reservation_system.dto.*;
import com.example.hotel_reservation_system.repository.*;
import com.example.hotel_reservation_system.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelService {

    @Autowired private UserRepository userRepo;
    @Autowired private RoomRepository roomRepo;
    @Autowired private ReservationRepository reservationRepo;

    // =========================================================================
    // --- 1. USER & ROOM OPERATIONS ---
    // =========================================================================

    public User registerUser(User user) {
        return userRepo.save(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Room addRoom(Room room) {
        return roomRepo.save(room);
    }

    public List<Room> getAllRooms() {
        return roomRepo.findAll();
    }

    public List<Room> getAvailableRooms() {
        return roomRepo.findByStatus("AVAILABLE");
    }

    // =========================================================================
    // --- 2. CORE CUSTOMER BOOKING LOGIC ---
    // =========================================================================

    @Transactional
    public ReservationResponseDTO createReservation(ReservationRequestDTO dto) {
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found with ID: " + dto.getUserId()));
        Room room = roomRepo.findById(dto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Target room not found with ID: " + dto.getRoomId()));

        if (!"AVAILABLE".equalsIgnoreCase(room.getStatus())) {
            throw new IllegalStateException("Room is occupied or undergoing maintenance.");
        }

        // Calculate nights stayed
        long days = ChronoUnit.DAYS.between(dto.getCheckInDate(), dto.getCheckOutDate());
        if (days <= 0) days = 1;

        // Map incoming DTO structural properties to internal Database Entity
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setCheckInDate(dto.getCheckInDate());
        reservation.setCheckOutDate(dto.getCheckOutDate());
        reservation.setTotalPrice(days * room.getPricePerNight() * 1.12); // Base price + 12% automated luxury tax
        reservation.setStatus("CONFIRMED");

        // Toggle room status automatically inside a transactional scope
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

    // MANDATORY REQUIREMENT: Core logic to look up an entity by its Primary Key
    public Reservation getReservationEntityById(Long id) {
        return reservationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with ID: " + id));
    }

    // =========================================================================
    // --- 3. RECEPTIONIST STATUS CORRECTION ACTION ---
    // =========================================================================

    @Transactional
    public ReservationResponseDTO updateStatus(Long reservationId, String newStatus) {
        Reservation res = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with ID: " + reservationId));

        res.setStatus(newStatus);

        // If guest checks out or cancels, make the room inventory AVAILABLE again
        if ("CHECKED_OUT".equalsIgnoreCase(newStatus) || "CANCELLED".equalsIgnoreCase(newStatus)) {
            Room room = res.getRoom();
            if (room != null) {
                room.setStatus("AVAILABLE");
                roomRepo.save(room);
            }
        }

        Reservation saved = reservationRepo.save(res);
        return convertToResponseDTO(saved);
    }

    // =========================================================================
    // --- 4. ADMIN ADMINISTRATIVE DELETION ---
    // =========================================================================

    @Transactional
    public void deleteReservation(Long id) {
        Reservation res = reservationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with ID: " + id));

        // If an administrator drops an active record, free up the room inventory block first
        if ("CONFIRMED".equalsIgnoreCase(res.getStatus()) || "BOOKED".equalsIgnoreCase(res.getStatus())) {
            Room room = res.getRoom();
            if (room != null) {
                room.setStatus("AVAILABLE");
                roomRepo.save(room);
            }
        }
        reservationRepo.delete(res);
    }

    // =========================================================================
    // --- PUBLIC UTILITY MAPPERS ---
    // =========================================================================

    public ReservationResponseDTO convertToResponseDTO(Reservation res) {
        ReservationResponseDTO response = new ReservationResponseDTO();
        response.setId(res.getId());

        if (res.getUser() != null) {
            response.setCustomerName(res.getUser().getName());
        }
        if (res.getRoom() != null) {
            response.setRoomNumber(res.getRoom().getRoomNumber());
        }

        response.setCheckInDate(res.getCheckInDate());
        response.setCheckOutDate(res.getCheckOutDate());
        response.setTotalPrice(res.getTotalPrice());
        response.setStatus(res.getStatus());
        return response;
    }
}