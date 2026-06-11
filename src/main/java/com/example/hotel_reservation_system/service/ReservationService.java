package com.example.hotel_reservation_system.service;

import com.example.hotel_reservation_system.model.Reservation;
import com.example.hotel_reservation_system.repository.ReservationRepository;
import com.example.hotel_reservation_system.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository repo;

    public ReservationService(ReservationRepository repo) {
        this.repo = repo;
    }

    // CREATE
    public Reservation create(Reservation r) {
        return repo.save(r);
    }

    // READ ALL
    public List<Reservation> getAll() {
        return repo.findAll();
    }

    // READ BY ID
    public Reservation getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reservation not found with id: " + id));
    }

    /*
    // FILTER BY STATUS (DISABLED)
    public List<Reservation> getByStatus(String status) {
        return repo.findByStatus(status);
    }
    */

    // ROOM AVAILABILITY (UPDATED - STATUS LOGIC DISABLED)
    public boolean isRoomAvailable(int roomNumber) {

        // OLD LOGIC (USES STATUS - DISABLED)
        // boolean exists = repo.existsByRoomNumberAndStatus(roomNumber, "CONFIRMED");

        // NEW SIMPLE LOGIC (NO STATUS)
        boolean exists = repo.existsByRoomNumber(roomNumber);

        return !exists;
    }

    // UPDATE (STATUS REMOVED)
    public Reservation update(Long id, Reservation newData) {

        Reservation existing = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reservation not found with id: " + id));

        existing.setCustomerName(newData.getCustomerName());
        existing.setEmail(newData.getEmail());
        existing.setRoomNumber(newData.getRoomNumber());
        existing.setCheckInDate(newData.getCheckInDate());
        existing.setCheckOutDate(newData.getCheckOutDate());

        /*
        existing.setStatus(newData.getStatus()); // DISABLED
        */

        return repo.save(existing);
    }

    // DELETE
    public void delete(Long id) {

        Reservation existing = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reservation not found with id: " + id));

        repo.delete(existing);
    }
}