package com.example.eventManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This service class provides methods for managing and accessing event data.
 */
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final SimpMessagingTemplate messagingTemplate; // for sending messages to WebSocket destinations

    private final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    public EventService(EventRepository eventRepository,
                        SimpMessagingTemplate messagingTemplate) {

        this.eventRepository = eventRepository;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * This method creates or updates an event.
     *
     * @param event The event to be created or updated.
     * @return The created or updated event.
     */
    public Event create(Event event) {
        return eventRepository.save(event);
    }

    /**
     * This method creates or updates a list of events.
     *
     * @param events The list of events to be created or updated.
     * @return The list of created or updated events.
     */
    public List<Event> createBatch(List<Event> events) {
        return eventRepository.saveAll(events);
    }

    /**
     * This method retrieves an event by its unique identifier.
     *
     * @param id The unique identifier of the event.
     * @return The event with the specified identifier, or null if not found.
     */
    public Event getById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    /**
     * This method retrieves a list of events by an event name.
     *
     * @param name The event name.
     * @return A list of events with the name.
     */
    public List<Event> getByName(String name) {
        return eventRepository.findByName(name);
    }

    /**
     * This method retrieves a list of events at a specific location.
     *
     * @param location The location to filter events.
     * @return A list of events at the specified location.
     */
    public List<Event> getByLocation(String location) {
        return eventRepository.findByLocation(location);
    }

    /**
     * This method retrieves a list of events ordered by popularity in descending order.
     *
     * @return A list of events ordered by popularity.
     */
    public List<Event> getSortedByPopularity() {
        return eventRepository.findByOrderByPopularityDesc();
    }

    /**
     * This method retrieves a list of events ordered by start date and time in ascending order.
     *
     * @return A list of events ordered by start date and time.
     */
    public List<Event> getSortedByStartDateTime() {
        return eventRepository.findByOrderByStartDateTime();
    }

    /**
     * This method retrieves a list of events ordered by end date and time in ascending order.
     *
     * @return A list of events ordered by end date and time.
     */
    public List<Event> getSortedByEndDateTime() {
        return eventRepository.findByOrderByEndDateTime();
    }

    /**
     * This method retrieves a list of all events.
     *
     * @return A list of all events.
     */
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    /**
     * This method updates an existing event using the provided event.
     *
     * @param id         The unique identifier of the event to be updated.
     * @param inputEvent The event provided for the update.
     * @return The updated event, or null if the event with the specified identifier is not found.
     */
    public Event update(Long id, Event inputEvent) {
        Event existingEvent = getById(id);
        if (existingEvent != null) {
            existingEvent.setStartDateTime(inputEvent.getStartDateTime());
            existingEvent.setEndDateTime(inputEvent.getEndDateTime());
            existingEvent.setName(inputEvent.getName());
            existingEvent.setLocation(inputEvent.getLocation());
            existingEvent.setPopularity(inputEvent.getPopularity());
            return create(existingEvent);
        }

        return null;
    }

    /**
     * This method updates a list of existing events using the provided events.
     *
     * @param inputEvents The list of events provided for the update.
     * @return The list of updated events.
     */
    public List<Event> updateBatch(List<Event> inputEvents) {
        List<Event> updatedEvents = new ArrayList<>();
        for (Event event : inputEvents) {
            if (event.getId() != null) {
                Event updatedEvent = update(event.getId(), event);
                if (updatedEvent != null) {
                    updatedEvents.add(updatedEvent);
                }
            }
        }

        return updatedEvents;
    }

    /**
     * This method deletes an event by its unique identifier.
     *
     * @param id The unique identifier of the event to be deleted.
     */
    public void delete(Long id) {
        eventRepository.deleteById(id);
    }

    /**
     * This method deletes a list of events by their unique identifiers.
     *
     * @param eventIds The list of unique identifiers of events to be deleted.
     */
    public void deleteBatch(List<Long> eventIds) {
        eventRepository.deleteAllById(eventIds);
    }

    /**
     * This method sends event reminders for upcoming events every 30 minutes.
     */
    @Scheduled(fixedRate = 1800000) // every 30 minutes
    public void sendReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<Event> upcomingEvents = eventRepository.findByStartDateTimeBetween(now, now.plusMinutes(30));
        for (Event event : upcomingEvents) {
            String name = event.getName();
            messagingTemplate.convertAndSend("/topic/event-reminders", "Event Reminder: " + name);
            logger.info("Sent reminder for event: {}", name);
        }
    }
}
