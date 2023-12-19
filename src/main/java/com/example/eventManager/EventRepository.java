package com.example.eventManager;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This repository interface provides methods for accessing and managing event data in the database.
 */
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * This method retrieves a list of events with the specified name.
     *
     * @param name The name to filter events.
     * @return A list of events with the specified name.
     */
    List<Event> findByName(String name);

    /**
     * This method retrieves a list of events at a specific location.
     *
     * @param location The location to filter events.
     * @return A list of events at the specified location.
     */
    List<Event> findByLocation(String location);

    /**
     * This method retrieves a list of events ordered by popularity in descending order.
     *
     * @return A list of events ordered by popularity.
     */
    List<Event> findByOrderByPopularityDesc();

    /**
     * This method retrieves a list of events ordered by start date and time in ascending order.
     *
     * @return A list of events ordered by start date and time.
     */
    List<Event> findByOrderByStartDateTime();

    /**
     * This method retrieves a list of events ordered by end date and time in ascending order.
     *
     * @return A list of events ordered by end date and time.
     */
    List<Event> findByOrderByEndDateTime();

    /**
     * This method retrieves a list of events that start between the given start and end range.
     *
     * @param startDateTime The start date and time to filter events.
     * @param endDateTime   The end date and time to filter events.
     * @return A list of events starting within the specified range.
     */
    List<Event> findByStartDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
