package com.example.hotel_reservation_system.controller;

import com.example.hotel_reservation_system.model.Reservation;
import com.example.hotel_reservation_system.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public Reservation create(@Valid @RequestBody Reservation r) {
        return service.create(r);
    }

    // READ ALL
    @GetMapping
    public List<Reservation> getAll() {
        return service.getAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public Reservation getById(@PathVariable Long id) {
        return service.getById(id);
    }

    /*
    // FILTER BY STATUS (DISABLED)
    @GetMapping("/status/{status}")
    public List<Reservation> getByStatus(@PathVariable String status) {
        return service.getByStatus(status);
    }
    */

    // ROOM AVAILABILITY
    @GetMapping("/availability/{roomNumber}")
    public Map<String, Object> checkAvailability(@PathVariable int roomNumber) {
        boolean available = service.isRoomAvailable(roomNumber);

        Map<String, Object> response = new HashMap<>();
        response.put("roomNumber", roomNumber);
        response.put("available", available);

        return response;
    }

    // UPDATE
    @PutMapping("/{id}")
    public Reservation update(@PathVariable Long id, @RequestBody Reservation r) {
        return service.update(id, r);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Reservation deleted successfully";
    }
}