package com.example.eventManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller class provides RESTful endpoints for managing and accessing event data.
 */
@RestController
@RequestMapping("/v1/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<Event> create(@RequestBody Event event) {
        Event createdEvent = eventService.create(event);
        return ResponseEntity.ok(createdEvent);
    }

    @PostMapping("/batch/create")
    public ResponseEntity<List<Event>> createBatch(@RequestBody List<Event> events) {
        List<Event> createdEvents = eventService.createBatch(events);
        return ResponseEntity.ok(createdEvents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable Long id) {
        Event event = eventService.getById(id);
        return event != null ? ResponseEntity.ok(event) : ResponseEntity.notFound().build();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Event>> getByName(@PathVariable String name) {
        List<Event> events = eventService.getByName(name);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<Event>> getByLocation(@PathVariable String location) {
        List<Event> events = eventService.getByLocation(location);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/sorted/popularity")
    public ResponseEntity<List<Event>> getSortedByPopularity() {
        List<Event> events = eventService.getSortedByPopularity();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/sorted/startDateTime")
    public ResponseEntity<List<Event>> getSortedByStartDateTime() {
        List<Event> events = eventService.getSortedByStartDateTime();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/sorted/endDateTime")
    public ResponseEntity<List<Event>> getSortedByEndDateTime() {
        List<Event> events = eventService.getSortedByEndDateTime();
        return ResponseEntity.ok(events);
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAll() {
        List<Event> events = eventService.getAll();
        return ResponseEntity.ok(events);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> update(@PathVariable Long id, @RequestBody Event inputEvent) {
        Event event = eventService.update(id, inputEvent);
        return event != null ? ResponseEntity.ok(event) : ResponseEntity.notFound().build();
    }

    @PutMapping("/batch/update")
    public ResponseEntity<List<Event>> updateBatch(@RequestBody List<Event> inputEvents) {
        List<Event> events = eventService.updateBatch(inputEvents);
        return ResponseEntity.ok(events);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        eventService.delete(id);
    }

    @DeleteMapping("/batch/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBatch(@RequestBody List<Long> eventIds) {
        eventService.deleteBatch(eventIds);
    }

    @GetMapping("/send")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendReminders() {
        eventService.sendReminders();
    }
}
