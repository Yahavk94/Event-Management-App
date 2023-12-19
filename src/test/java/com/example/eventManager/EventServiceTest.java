package com.example.eventManager;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void testCreate() {
        Event event = new Event();
        event.setName("New Event");
        Mockito.when(eventRepository.save(any(Event.class))).thenReturn(event);
        Event createdEvent = eventService.create(event);
        assertEquals("New Event", createdEvent.getName());
    }

    @Test
    void testCreateBatch() {
        Event event1 = new Event();
        event1.setName("Event 1");
        Event event2 = new Event();
        event2.setName("Event 2");
        List<Event> events = Arrays.asList(event1, event2);
        Mockito.when(eventRepository.saveAll(any())).thenReturn(events);
        List<Event> createdEvents = eventService.createBatch(events);
        assertEquals(2, createdEvents.size());
        assertEquals(events.get(0).getName(), createdEvents.get(0).getName());
        assertEquals(events.get(1).getName(), createdEvents.get(1).getName());
    }

    @Test
    void testGetById() {
        Long eventId = 1L;
        Event expectedEvent = new Event();
        expectedEvent.setId(eventId);
        expectedEvent.setName("Event");
        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(expectedEvent));
        Event retrievedEvent = eventService.getById(eventId);
        assertNotNull(retrievedEvent);
        assertEquals(expectedEvent.getName(), retrievedEvent.getName());
    }

    @Test
    void testGetSortedByStartDateTime() {
        Event event1 = new Event();
        event1.setName("Event 1");
        event1.setStartDateTime(LocalDateTime.now().minusHours(1));
        Event event2 = new Event();
        event2.setName("Event 2");
        event2.setStartDateTime(LocalDateTime.now().minusMinutes(30));
        List<Event> events = Arrays.asList(event1, event2);
        Mockito.when(eventRepository.findByOrderByStartDateTime()).thenReturn(events);
        List<Event> sortedEvents = eventService.getSortedByStartDateTime();
        assertEquals(2, sortedEvents.size());
        assertTrue(sortedEvents.get(0).getStartDateTime().isBefore(sortedEvents.get(1).getStartDateTime()));
    }

    @Test
    void testGetSortedByEndDateTime() {
        Event event1 = new Event();
        event1.setName("Event 1");
        event1.setEndDateTime(LocalDateTime.now().minusHours(1));
        Event event2 = new Event();
        event2.setName("Event 2");
        event2.setEndDateTime(LocalDateTime.now().minusMinutes(30));
        List<Event> events = Arrays.asList(event1, event2);
        Mockito.when(eventRepository.findByOrderByEndDateTime()).thenReturn(events);
        List<Event> sortedEvents = eventService.getSortedByEndDateTime();
        assertEquals(2, sortedEvents.size());
        assertTrue(sortedEvents.get(0).getEndDateTime().isBefore(sortedEvents.get(1).getEndDateTime()));
    }

    @Test
    void testGetByLocation() {
        String location = "Conference Room";
        Event event1 = new Event();
        event1.setName("Event 1");
        event1.setLocation(location);
        Event event2 = new Event();
        event2.setName("Event 2");
        event2.setLocation(location);
        List<Event> events = Arrays.asList(event1, event2);
        Mockito.when(eventRepository.findByLocation(location)).thenReturn(events);
        List<Event> eventsAtLocation = eventService.getByLocation(location);
        assertEquals(2, eventsAtLocation.size());
        assertEquals(location, eventsAtLocation.get(0).getLocation());
        assertEquals(location, eventsAtLocation.get(1).getLocation());
    }

    @Test
    void testGetAll() {
        Event event1 = new Event();
        event1.setName("Event 1");
        Event event2 = new Event();
        event2.setName("Event 2");
        List<Event> events = Arrays.asList(event1, event2);
        Mockito.when(eventRepository.findAll()).thenReturn(events);
        List<Event> retrievedEvents = eventService.getAll();
        assertEquals(2, retrievedEvents.size());
        assertEquals(events.get(0).getName(), retrievedEvents.get(0).getName());
        assertEquals(events.get(1).getName(), retrievedEvents.get(1).getName());
    }

    @Test
    void testUpdate() {
        Long eventId = 1L;
        Event existingEvent = new Event();
        existingEvent.setId(eventId);
        existingEvent.setName("Existing Event");
        Event inputEvent = new Event();
        inputEvent.setName("Updated Event");
        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        Mockito.when(eventRepository.save(any(Event.class))).thenReturn(existingEvent);
        Event updatedEvent = eventService.update(eventId, inputEvent);
        assertNotNull(updatedEvent);
        assertEquals("Updated Event", updatedEvent.getName());
    }

    @Test
    void testUpdateNonExistingEvent() {
        Long nonExistingEventId = -1000L;
        Event inputEvent = new Event();
        inputEvent.setName("Updated Event");
        Mockito.when(eventRepository.findById(nonExistingEventId)).thenReturn(Optional.empty());
        Event updatedEvent = eventService.update(nonExistingEventId, inputEvent);
        assertNull(updatedEvent);
    }

    @Test
    void testUpdateBatch() {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setName("Event 1");
        Event event2 = new Event();
        event2.setId(2L);
        event2.setName("Event 2");
        List<Event> inputEvents = Arrays.asList(event1, event2);
        Mockito.when(eventRepository.findById(1L)).thenReturn(Optional.of(event1));
        Mockito.when(eventRepository.findById(2L)).thenReturn(Optional.of(event2));
        Mockito.when(eventRepository.save(any(Event.class))).thenReturn(event1, event2);
        List<Event> updatedEvents = eventService.updateBatch(inputEvents);
        assertEquals(2, updatedEvents.size());
        assertEquals(inputEvents.get(0).getName(), updatedEvents.get(0).getName());
        assertEquals(inputEvents.get(1).getName(), updatedEvents.get(1).getName());
    }

    @Test
    void testDelete() {
        Long eventId = 1L;
        eventService.delete(eventId);
        verify(eventRepository, times(1)).deleteById(eventId);
    }

    @Test
    void testDeleteBatch() {
        List<Long> eventIds = Arrays.asList(1L, 2L, 3L);
        eventService.deleteBatch(eventIds);
        verify(eventRepository, times(1)).deleteAllById(eventIds);
    }
}
