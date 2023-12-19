package com.example.eventManager;

import jakarta.persistence.*;
import lombok.Data;

import java.time.DateTimeException;
import java.time.LocalDateTime;

/**
 * This class represents an event with the relevant details.
 */
@Data
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // primary key

    private String name;
    private String location;
    private int popularity; // number of participants

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    /**
     * This method validates that endDateTime is after startDateTime before persisting or updating the entity.
     * Throws DateTimeException if the validation fails.
     */
    @PrePersist
    @PreUpdate
    private void validate() {
        if (startDateTime != null && endDateTime != null && !endDateTime.isAfter(startDateTime)) {
            throw new DateTimeException("End date and time must be after start date and time");
        }
    }
}
